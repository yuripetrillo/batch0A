package com.yuripe.batchType0A.Listener;

import java.util.Objects;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.stereotype.Component;

@Component
public class StepListener implements StepExecutionListener {
	
	private static String filePattern = "";
	
	@BeforeStep
	@Override
	public void beforeStep(StepExecution stepExecution) {
	    JobParameters jobParameters = stepExecution.getJobExecution().getJobParameters();
	    filePattern = jobParameters.getString("filePattern");
	}
	
	public String getPatternFromStepExecutionContext() {
		return Objects.isNull(filePattern) ? "no_resources" : filePattern;
	}
	
}
