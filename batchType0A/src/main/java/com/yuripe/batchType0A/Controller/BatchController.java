package com.yuripe.batchType0A.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yuripe.core.library.services.FTPService;
import com.yuripe.core.library.utility.SFTP;

@RestController
@RequestMapping("/api/batch0A")
public class BatchController {
	  private static final Logger logger = LoggerFactory.getLogger(BatchController.class);
	  private final FTPService ftp = new FTPService();
	
	  @Autowired 
	  JobLauncher jobLauncher;

	  @Autowired
	  private Job importUserJob;

	  @PostMapping(
	      value = "/launchJob/{targetPath}/{filePattern}",
	      produces = MediaType.APPLICATION_JSON_VALUE)
	  public void importFileAndRun(@PathVariable String targetPath, @PathVariable String filePattern) throws Exception {
		  /*FtpClient ftpClient = new FtpClient("127.0.0.1", 21, "yuri", "adminftp");
		  
	      if(!ftp.checkFTPServerStatus(ftpClient)) {
	    	  logger.error(HttpStatus.SERVICE_UNAVAILABLE.toString());
	      }
	      if(!ftp.checkFTPServerTargetFile(ftpClient, filePattern)) {
	    	  logger.error("Requested data ".concat(HttpStatus.NOT_FOUND.toString()));
	      }
	      
	      ftp.downloadFile(filePattern, Paths.get(this.getClass().getResource("/").getPath()).toString());
		  //Get file from FTP server using core library*/
		  
		  if(targetPath.equals("")) //add control, if target path is empty or target path is not target batch folder.
			  return;
	
		  //log if batch start or not and who launched the batch (normalyp)
	   this.jobLauncher.run(this.importUserJob, new JobParametersBuilder().addString("filePattern", filePattern).toJobParameters());
	  }
}
