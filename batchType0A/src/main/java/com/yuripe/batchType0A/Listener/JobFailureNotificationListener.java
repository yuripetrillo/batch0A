package com.yuripe.batchType0A.Listener;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.yuripe.core.library.utility.FileManagement;

@Component
public class JobFailureNotificationListener implements JobExecutionListener {

  private static final Logger log = LoggerFactory.getLogger(JobFailureNotificationListener.class);
  private static final String targetPath = new File(JobCompletionNotificationListener.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getParentFile().getAbsolutePath();
  private static final String sourceDir = targetPath.concat("/src/main/resources/inputFiles/"); //remove resources mod path to external
  private static final String errorDir = targetPath.concat("/inputFiles_error/");

  private final JdbcTemplate jdbcTemplate;
  @Autowired
  StepListener stepExecution;
  @Autowired
  public JobFailureNotificationListener(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.FAILED) {
       log.info("!!! JOB FAILED! Time to verify the results");
      FileManagement fileManager = new FileManagement();
      fileManager.moveFile(sourceDir.concat(stepExecution.getPatternFromStepExecutionContext()), errorDir.concat(stepExecution.getPatternFromStepExecutionContext()));      
  }
  }
}