package org.atos.commutermap.batch;

import com.google.common.collect.ImmutableMap;
import org.atos.commutermap.dao.RouteRepository;
import org.atos.commutermap.dao.StationRepository;
import org.atos.commutermap.dao.config.DatabaseConfig;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.dao.model.Station;
import org.atos.commutermap.network.config.NetworkConfig;
import org.atos.commutermap.network.service.MavinfoServerCaller;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ChunkListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@Import({DatabaseConfig.class, NetworkConfig.class})
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {

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

    @Override
    public void setDataSource(DataSource dataSource) {
        //noop
    }

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
        return new FilterStationsProcessor();
    }

    @Bean
    public CreateMavRequestProcessor createMavRequestProcessor() {
        return new CreateMavRequestProcessor(baseStation());
    }

    @Bean
    public Station baseStation() {
        return stationRepository.findByName("BUDAPEST*").get();
    }

    @Bean
    public CallMavProcessor callMavProcessor() {
        return new CallMavProcessor(mavinfoServerCaller);
    }

    @Bean
    public FilterFarAwayRoutesProcessor filterFarAwayRoutesProcessor() {
        return new FilterFarAwayRoutesProcessor();
    }

    @Bean
    public FilterFarAwayStationsProcessor filterFarAwayStationsProcessor() {
        return new FilterFarAwayStationsProcessor(baseStation());
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
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        LoggerFactory.getLogger("batch").info("Completed job {}", jobExecution.toString());
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
                .listener(new ChunkListenerSupport() {
                    @Override
                    public void afterChunk(ChunkContext context) {
                        LoggerFactory.getLogger(getClass()).info("Processed {} stations.", context.getStepContext().getStepExecution().getReadCount());
                    }
                })
                .build();
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
