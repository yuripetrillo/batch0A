package com.yuripe.batchType0A.batchprocessing.Configuration;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import javax.sql.DataSource;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import com.yuripe.batchType0A.Listener.ItemFailureLoggerListener;
import com.yuripe.batchType0A.Listener.JobCompletionNotificationListener;
import com.yuripe.batchType0A.Listener.JobFailureNotificationListener;
import com.yuripe.batchType0A.Listener.StepListener;
import com.yuripe.batchType0A.Mapper.PolicyMapper;
import com.yuripe.batchType0A.batchprocessing.Model.InsurancePolicy;
import com.yuripe.batchType0A.batchprocessing.Processor.PolicyItemProcessor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;

@Configuration
public class BatchConfiguration {
	
	/** 
     *Load the properties 
     */ 
    @Value("${database.driver}") 
    private String databaseDriver; 
    @Value("${database.url}") 
    private String databaseUrl; 
    @Value("${database.username}") 
    private String databaseUsername; 
    @Value("${database.password}") 
    private String databasePassword; 
    
    @Autowired
    private EntityManagerFactory entityManager;
    
    @Autowired
    StepListener stepExecution;

	@Bean
	@StepScope
	@BeforeStep
	public FlatFileItemReader<InsurancePolicy> reader() {
	  return new FlatFileItemReaderBuilder<InsurancePolicy>()
	    .name("policyItemReader")
	    .resource(new ClassPathResource(stepExecution.getPatternFromStepExecutionContext()))
	    .targetType(InsurancePolicy.class)
	    .delimited()
	    .delimiter(";")
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
	//@StepScope
	public JdbcBatchItemWriter<InsurancePolicy> writer(DataSource dataSource) {
	  return new JdbcBatchItemWriterBuilder<InsurancePolicy>()
	    .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
	    .sql("INSERT INTO BATCH.policy (contractor_customer_code, effective_date, expire_date, insured_customer_code, policy_number, state) VALUES (:contractorCustomerCode, :effectiveDate, :expireDate, :insuredCustomerCode, :policyNumber, :state)")
	    .dataSource(dataSource)
	    .build();
	}
	/*WRITER TO JSON FILE
	 * @Bean
	@StepScope
	public JsonFileItemWriter<InsurancePolicy> writer() throws IOException {
	    JsonFileItemWriterBuilder<InsurancePolicy> builder = new JsonFileItemWriterBuilder<>();
	    JacksonJsonObjectMarshaller<InsurancePolicy> marshaller = new JacksonJsonObjectMarshaller<>();
	    return builder
	      .name("writer")
	      .jsonObjectMarshaller(marshaller)
	      .resource(new FileSystemResource("src/main/resources/outputJ.json"))
	      .build();
	}*/
	
	@Bean
	public Job importUserJob(JobRepository jobRepository,
	    JobCompletionNotificationListener listener, JobFailureNotificationListener failureListener, Step step1) {
	  return new JobBuilder("importUserJob", jobRepository)
	    .incrementer(new RunIdIncrementer())
	    .listener(failureListener)
	    .listener(listener)
	    .flow(step1)
	    .end()
	    .build();
	}

	@Bean
	public Step step1(JobRepository jobRepository, StepListener stepListener, 
	    PlatformTransactionManager transactionManager, JdbcBatchItemWriter<InsurancePolicy> writer, FlatFileItemReader<InsurancePolicy> reader  /*ItemWriter<InsurancePolicy> writer*/) {
	  return new StepBuilder("step1", jobRepository)
	    .<InsurancePolicy, InsurancePolicy> chunk(10, transactionManager)
	    .reader(reader)
	    .processor(processor())
	    .writer(writer)
	    .listener(stepListener)
	    .build();
	}

}
