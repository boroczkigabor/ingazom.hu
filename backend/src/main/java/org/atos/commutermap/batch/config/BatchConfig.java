package org.atos.commutermap.batch.config;

import com.google.common.collect.ImmutableMap;
import org.atos.commutermap.batch.JobStatistics;
import org.atos.commutermap.batch.steps.*;
import org.atos.commutermap.dao.RouteRepository;
import org.atos.commutermap.dao.StationRepository;
import org.atos.commutermap.dao.config.RouteDaoConfig;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.dao.model.Station;
import org.atos.commutermap.network.config.NetworkConfig;
import org.atos.commutermap.network.service.MavinfoServerCaller;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ChunkListenerSupport;
import org.springframework.batch.core.listener.CompositeStepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;

import javax.batch.api.chunk.listener.AbstractItemWriteListener;
import java.util.Arrays;
import java.util.List;

@Configuration
@Import({RouteDaoConfig.class, NetworkConfig.class, SchedulerConfig.class})
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {

    @Autowired
    private Environment env;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private MavinfoServerCaller mavinfoServerCaller;

    @Value("${filter.routes.stale.after.days}")
    private int numberOfDaysAfterDataIsStale;

    @Bean
    public ItemReader<Station> stationsReader() {
        return new RepositoryItemReaderBuilder<Station>()
                .repository(stationRepository)
                .methodName("findAll")
                .sorts(ImmutableMap.of(
                        "id", Sort.Direction.ASC
                ))
                .name("jpaReader")
                .build();
    }

    @Bean
    public FilterStationsProcessor filterStationsProcessor() {
        return new FilterStationsProcessor(stationRepository);
    }

    @Bean
    public CreateMavRequestProcessor createMavRequestProcessor() {
        return new CreateMavRequestProcessor(stationRepository, routeRepository, numberOfDaysAfterDataIsStale);
    }

    @Bean
    public CallMavProcessor callMavProcessor() {
        return new CallMavProcessor(mavinfoServerCaller, failsafeOfferSelectorComposite());
    }

    @Primary
    @Bean
    public OfferSelector failsafeOfferSelectorComposite() {
        return new FailsafeOfferSelectorComposite(mostCommonTimeOfferSelector(), shortestTimeOfferSelector());
    }

    @Bean
    public OfferSelector shortestTimeOfferSelector() {
        return new ShortestTimeOfferSelector();
    }

    @Bean
    public OfferSelector mostCommonTimeOfferSelector() {
        return new MostCommonTimeOfferSelector();
    }

    @Bean
    public MarkFarAwayRoutesProcessor filterFarAwayRoutesProcessor() {
        return new MarkFarAwayRoutesProcessor();
    }

    @Bean
    public FilterFarAwayStationsProcessor filterFarAwayStationsProcessor() {
        return new FilterFarAwayStationsProcessor(stationRepository);
    }

    @Bean
    public ItemWriter<Route> itemWriter() {
        RepositoryItemWriter<Route> routeRepositoryItemWriter = new RepositoryItemWriter<>();
        routeRepositoryItemWriter.setRepository(routeRepository);
        routeRepositoryItemWriter.setMethodName("save");
        return routeRepositoryItemWriter;
    }

    @Bean
    public Job callTheMavServerJob() {
        return jobBuilderFactory.get("mavJob")
                .incrementer(new RunIdIncrementer())
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        LoggerFactory.getLogger("batch").info("Starting job {}", jobExecution.toString());
                        JobStatistics.reset();
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        LoggerFactory.getLogger("batch").info("Completed job {}", jobExecution.toString());
                        JobStatistics.printStatistics();
                    }
                })
                .start(callTheMavServerStep())
                .build();
    }

    @Bean
    public Step callTheMavServerStep() {
        return stepBuilderFactory.get("call")
                .<Station, Route>chunk(10)
                .reader(stationsReader())
                .processor(travelBetweenStationsProcessor())
                .writer(itemWriter())
                .listener(stepExecutionListeners())
                .listener(new ChunkListenerSupport() {
                    @Override
                    public void afterChunk(ChunkContext context) {
                        LoggerFactory.getLogger(getClass()).info("Processed {} stations so far.", context.getStepContext().getStepExecution().getReadCount());
                    }
                })
                .listener(new AbstractItemWriteListener() {
                    @Override
                    public void afterWrite(List<Object> items) {
                        LoggerFactory.getLogger(getClass()).info("Wrote {} routes.", items.size());
                    }
                })
                .build();
    }

    private CompositeStepExecutionListener stepExecutionListeners() {
        CompositeStepExecutionListener compositeStepExecutionListener = new CompositeStepExecutionListener();
        compositeStepExecutionListener.setListeners(
                new StepExecutionListener[]{
                        filterStationsProcessor(),
                        filterFarAwayStationsProcessor(),
                        createMavRequestProcessor(),
                        callMavProcessor(),
                        filterFarAwayRoutesProcessor()
                });
        return compositeStepExecutionListener;
    }

    @Bean
    public ItemProcessor<Station, Route> travelBetweenStationsProcessor() {
        CompositeItemProcessor<Station, Route> compositeProcessor = new CompositeItemProcessor<>();
        compositeProcessor.setDelegates(
                Arrays.asList(
                        filterStationsProcessor(),
                        filterFarAwayStationsProcessor(),
                        createMavRequestProcessor(),
                        callMavProcessor(),
                        filterFarAwayRoutesProcessor()
                ));
        return compositeProcessor;
    }
}
