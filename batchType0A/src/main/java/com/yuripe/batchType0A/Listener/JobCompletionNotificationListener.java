package com.yuripe.batchType0A.Listener;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.yuripe.core.library.utility.FileManagement;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

  private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
  private static final String targetPath = new File(JobCompletionNotificationListener.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getParentFile().getAbsolutePath();
  private static final String sourceDir = targetPath.concat("/src/main/resources/inputFiles/"); //remove resources mod path to external
  private static final String successDir = targetPath.concat("/inputFiles_done/");

  private final JdbcTemplate jdbcTemplate;
  @Autowired
  StepListener stepExecution;
  @Autowired
  public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  
  @Override
  public void beforeJob(JobExecution jobExecution) {

  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! JOB FINISHED! Time to verify the results");
      FileManagement fileManager = new FileManagement();
      fileManager.moveFile(sourceDir.concat(stepExecution.getPatternFromStepExecutionContext()), successDir.concat(stepExecution.getPatternFromStepExecutionContext()));      

      
  }
  }
}