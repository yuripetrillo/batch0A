package com.yuripe.batchType0A.Listener;

import java.util.Objects;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.stereotype.Component;

@Component
public class JobListener implements JobExecutionListener {
	
	private static String filePattern = "";
	
	@BeforeJob
	@Override
	public void beforeJob(JobExecution jobExecution) {
	    JobParameters jobParameters = jobExecution.getJobParameters();
	    filePattern = jobParameters.getString("filePattern");
	}
	
	@AfterJob
	@Override
	public void afterJob(JobExecution jonExecution) {
		//FileManagement fileManager = new FileManagement();
	    //fileManager.moveFile(sourceDir.concat(stepExecution.getPatternFromStepExecutionContext()), errorDir.concat(stepExecution.getPatternFromStepExecutionContext()));
	}
	
	public String getPatternFromJobExecutionContext() {
		return Objects.isNull(filePattern) ? "no_resources" : filePattern;
	}
	
}