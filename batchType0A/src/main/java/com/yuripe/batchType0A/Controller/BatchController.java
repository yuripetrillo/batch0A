package com.yuripe.batchType0A.Controller;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yuripe.core.library.services.FTPService;
import com.yuripe.core.library.utility.FtpClient;

@RestController
public class BatchController {
	  private static final Logger logger = LoggerFactory.getLogger(BatchController.class);
	  private final FTPService ftp = new FTPService();
	
	  @Autowired 
	  JobLauncher jobLauncher;

	  @Autowired
	  private Job importUserJob;

	  @PostMapping(
	      value = "/launch0A/{scheduleName}/{filePattern}",
	      produces = MediaType.APPLICATION_JSON_VALUE)

	  public void importFileAndRun(@PathVariable String scheduleName, @PathVariable String filePattern) throws Exception {
		  FtpClient ftpClient = new FtpClient("127.0.0.1", 21, "yuri", "adminftp");
		  
	      if(!ftp.checkFTPServerStatus(ftpClient)) {
	    	  logger.error(HttpStatus.SERVICE_UNAVAILABLE.toString());
	      }
	      if(!ftp.checkFTPServerTargetFile(ftpClient, filePattern)) {
	    	  logger.error("Requested data ".concat(HttpStatus.NOT_FOUND.toString()));
	      }
	      
	      ftp.downloadFile(filePattern, Paths.get(this.getClass().getResource("/").getPath()).toString());
		  //Get file from FTP server using core library
	
	 
	   this.jobLauncher.run(this.importUserJob, new JobParametersBuilder().toJobParameters());
	  }
}
