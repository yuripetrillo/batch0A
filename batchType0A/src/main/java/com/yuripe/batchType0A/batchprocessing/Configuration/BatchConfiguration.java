package com.yuripe.batchType0A.batchprocessing.Configuration;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import com.yuripe.batchType0A.Listener.JobCompletionNotificationListener;
import com.yuripe.batchType0A.Listener.JobFailureNotificationListener;
import com.yuripe.batchType0A.Listener.JobListener;
import com.yuripe.batchType0A.Listener.StepListener;
import com.yuripe.batchType0A.Reader.SimpleReader;
import com.yuripe.batchType0A.batchprocessing.Processor.PolicyItemProcessor;
import com.yuripe.batchType0A.batchprocessing.Model.InsurancePolicyCustom;
import jakarta.persistence.EntityManagerFactory;

@Configuration
public class BatchConfiguration {
	@Autowired
    private EntityManagerFactory entityManager;
    
    @Autowired
    StepListener stepExecution;
    
    @Autowired
    JobListener jobExecution;
    
	private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);
	private static final String targetPath = new File(BatchConfiguration.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getParentFile().getAbsolutePath();
	
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

	@Bean
	public Job importUserJob(JobRepository jobRepository,
	    JobCompletionNotificationListener listener, JobFailureNotificationListener failureListener, Step step1) {
	  return new JobBuilder("importUserJob", jobRepository)
	    .incrementer(new RunIdIncrementer())
	    .listener(jobExecution)
	    .listener(failureListener)
	    .listener(listener)
	    .flow(step1)
	    .end()
	    .build();
	}

	@Bean
	public Step step1(JobRepository jobRepository, StepListener stepListener, 
	    PlatformTransactionManager transactionManager, JdbcBatchItemWriter<InsurancePolicyCustom> writer, FlatFileItemReader<InsurancePolicyCustom> reader  /*ItemWriter<InsurancePolicyX> writer*/) {
		return new StepBuilder("step1", jobRepository)
	    .<InsurancePolicyCustom, InsurancePolicyCustom> chunk(10, transactionManager)
	    .reader(simpleReader())
	    .processor(processor())
	    .writer(writer)
	    //.faultTolerant() //allowing spring batch to skip line 
        //.skipLimit(1000) //skip line limit
        //.skip(FormattingException.class) //skip lines when this exception is thrown
	    .listener(stepListener)
	    .build();
	}
	
	/*@Bean
	@StepScope
	public FlatFileItemReader<InsurancePolicyX> reader() {
	  return new FlatFileItemReaderBuilder<InsurancePolicyX>()
	    .name("policyItemReader")
	    .resource(new ClassPathResource("/inputFiles/".concat(stepExecution.getPatternFromStepExecutionContext()))) //sistema path prelevare fuori dal jar non in resources
	    .targetType(InsurancePolicyX.class)
	    .delimited()
	    .delimiter(";")
	    .names(new String[]{"contractor_customer_code", "effective_date", "expire_date", "insured_customer_code", "policy_number", "state"})
	    .customEditors(Collections.singletonMap(Date.class, new DatePropertyEditor()))     
	    //.fieldSetMapper(new PolicyMapper())
	    //.lineMapper(new CustomLineMapper())
	    .build();
	    
	}*/
	
	
	
    @Bean
    @StepScope
    public FlatFileItemReader<InsurancePolicyCustom> simpleReader() {
    	String fileName = stepExecution.getPatternFromStepExecutionContext();
        FlatFileItemReader<InsurancePolicyCustom> itemReader = new SimpleReader();
        itemReader.setLineMapper(lineMapper());
        //itemReader.setLinesToSkip(1);
        itemReader.setResource(new ClassPathResource(Objects.isNull(fileName) ? "/inputFiles/" : "/inputFiles/".concat(fileName))); //create ItemStream interface custom class to manage load of file name 
        return itemReader;
    }

    @Bean
    public LineMapper<InsurancePolicyCustom> lineMapper() {
        DefaultLineMapper<InsurancePolicyCustom> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[]{"contractor_customer_code", "effective_date", "expire_date", "insured_customer_code", "policy_number", "state"});
        lineTokenizer.setDelimiter(";");
        //lineTokenizer.setIncludedFields(new int[] { 0, 1, 2 });
        BeanWrapperFieldSetMapper<InsurancePolicyCustom> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(InsurancePolicyCustom.class);
        fieldSetMapper.setCustomEditors(Collections.singletonMap(Date.class, new DatePropertyEditor()));
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

	@Bean
	public PolicyItemProcessor processor() {
	  return new PolicyItemProcessor();
	}

	@Bean
	//@StepScope
	public JdbcBatchItemWriter<InsurancePolicyCustom> writer(DataSource dataSource) {
	  return new JdbcBatchItemWriterBuilder<InsurancePolicyCustom>()
	    .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
	    .sql("INSERT INTO BATCH.policy (contractor_customer_code, effective_date, expire_date, insured_customer_code, policy_number, state) VALUES (:contractorCustomerCode, :effectiveDate, :expireDate, :insuredCustomerCode, :policyNumber, :state)")
	    .dataSource(dataSource)
	    .build();
	}
	/*WRITER TO JSON FILE
	 * @Bean
	@StepScope
	public JsonFileItemWriter<InsurancePolicyX> writer() throws IOException {
	    JsonFileItemWriterBuilder<InsurancePolicyX> builder = new JsonFileItemWriterBuilder<>();
	    JacksonJsonObjectMarshaller<InsurancePolicyX> marshaller = new JacksonJsonObjectMarshaller<>();
	    return builder
	      .name("writer")
	      .jsonObjectMarshaller(marshaller)
	      .resource(new FileSystemResource("src/main/resources/outputJ.json"))
	      .build();
	}*/
	


}
