package com.yuripe.batchType0A.batchprocessing.Configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.yuripe.batchType0A.batchprocessing.Model.InsurancePolicy;
import com.yuripe.batchType0A.batchprocessing.Processor.PolicyItemProcessor;

@Configuration
public class BatchConfiguration {

	@Bean
	public FlatFileItemReader<InsurancePolicy> reader() {
	  return new FlatFileItemReaderBuilder<InsurancePolicy>()
	    .name("personItemReader")
	    .resource(new ClassPathResource("sample-data.csv"))
	    .delimited()
	    .names(new String[]{"firstName", "lastName"})
	    .fieldSetMapper(new BeanWrapperFieldSetMapper<InsurancePolicy>() {{
	      setTargetType(InsurancePolicy.class);
	    }})
	    .build();
	}

	@Bean
	public PolicyItemProcessor processor() {
	  return new PolicyItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<InsurancePolicy> writer(DataSource dataSource) {
	  return new JdbcBatchItemWriterBuilder<InsurancePolicy>()
	    .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
	    .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
	    .dataSource(dataSource)
	    .build();
	}
	
	@Bean
	public Job importUserJob(JobRepository jobRepository,
	    JobCompletionNotificationListener listener, Step step1) {
	  return new JobBuilder("importUserJob", jobRepository)
	    .incrementer(new RunIdIncrementer())
	    .listener(listener)
	    .flow(step1)
	    .end()
	    .build();
	}

	@Bean
	public Step step1(JobRepository jobRepository,
	    PlatformTransactionManager transactionManager, JdbcBatchItemWriter<InsurancePolicy> writer) {
	  return new StepBuilder("step1", jobRepository)
	    .<InsurancePolicy, InsurancePolicy> chunk(10, transactionManager)
	    .reader(reader())
	    .processor(processor())
	    .writer(writer)
	    .build();
	}
}
