package com.yuripe.batchType0A;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class BatchType0AApplication {

	private static final Logger logger = LoggerFactory.getLogger(BatchType0AApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(BatchType0AApplication.class, args);
	}

}
