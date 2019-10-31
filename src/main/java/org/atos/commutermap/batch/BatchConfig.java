package org.atos.commutermap.batch;

import com.google.common.collect.ImmutableMap;
import org.atos.commutermap.dao.StationRepository;
import org.atos.commutermap.dao.config.DatabaseConfig;
import org.atos.commutermap.dao.model.Station;
import org.atos.commutermap.network.config.NetworkConfig;
import org.atos.commutermap.network.model.TravelOfferResponse;
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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;

import javax.sql.DataSource;

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
    private MavinfoServerCaller mavinfoServerCaller;

    @Override
    public void setDataSource(DataSource dataSource) {
        //noop
    }

    @Bean
    public ItemReader<Station> itemReader() {
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
    public TravelBetweenStationsProcessor travelBetweenStationsProcessor() {
        return new TravelBetweenStationsProcessor(stationRepository.findByName("BUDAPEST*").get(), mavinfoServerCaller);
    }

    @Bean
    public ItemWriter<TravelOfferResponse> itemWriter() {
        return items -> items.forEach(item -> System.out.println("Something would be done here"));
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
                .flow(callTheMavServerStep())
                .end()
                .build();
    }

    @Bean
    public Step callTheMavServerStep() {
        return stepBuilderFactory.get("call")
                .<Station, TravelOfferResponse>chunk(10)
                .reader(itemReader())
                .processor(travelBetweenStationsProcessor())
                .writer(itemWriter())
                .build();
    }
}
