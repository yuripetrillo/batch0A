package com.yuripe.batchType0A.Listener;

import java.util.Objects;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.yuripe.core.library.utility.FileManagement;

@Component
public class StepListener implements StepExecutionListener {
	
	private static String filePattern = "";
	
	@BeforeStep
	@Override
	public void beforeStep(StepExecution stepExecution) {
	    JobParameters jobParameters = stepExecution.getJobExecution().getJobParameters();
	    filePattern = jobParameters.getString("filePattern");
	}
	
	@AfterStep
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		//FileManagement fileManager = new FileManagement();
	    //fileManager.moveFile(sourceDir.concat(stepExecution.getPatternFromStepExecutionContext()), errorDir.concat(stepExecution.getPatternFromStepExecutionContext()));
		return stepExecution.getExitStatus();
	}
	
	public String getPatternFromStepExecutionContext() {
		return Objects.isNull(filePattern) ? "no_resources" : filePattern;
	}
	
}
