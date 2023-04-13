package com.yuripe.batchType0A.batchprocessing.Configuration;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

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

import com.yuripe.batchType0A.Mapper.PolicyMapper;
import com.yuripe.batchType0A.batchprocessing.Model.InsurancePolicy;
import com.yuripe.batchType0A.batchprocessing.Processor.PolicyItemProcessor;

@Configuration
public class BatchConfiguration {

	@Bean
	public FlatFileItemReader<InsurancePolicy> reader() {
	  return new FlatFileItemReaderBuilder<InsurancePolicy>()
	    .name("policyItemReader")
	    .resource(new ClassPathResource("sample-data.csv"))
	    .targetType(InsurancePolicy.class)
	    .delimited()
	    .delimiter(",")
	    .names(new String[]{"contractor_customer_code", "effective_date", "expire_date", "insured_customer_code", "policy_number", "state"})
	    .customEditors(Collections.singletonMap(Date.class, new DatePropertyEditor()))
	    /*.fieldSetMapper(new BeanWrapperFieldSetMapper<InsurancePolicy>() {{
	      setTargetType(InsurancePolicy.class);
	      setCustomEditors(Collections.singletonMap(Date.class, new DatePropertyEditor()));
	    }})*/
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
	    .sql("INSERT INTO BATCH.policy (contractor_customer_code, effective_date, expire_date, insured_customer_code, policy_number, state) VALUES (:contractorCustomerCode, :effectiveDate, :expireDate, :insuredCustomerCode, :policyNumber, :state)")
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
