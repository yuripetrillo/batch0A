package com.yuripe.batchType0A.Exception;

import org.springframework.context.annotation.Configuration;

@Configuration
public class FormattingException extends Exception {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String requestId;

	    // Custom error message
	    private String message;

	    // Custom error code representing an error in system
	    private String errorCode;

	    public FormattingException () {
        }
	    
	    public FormattingException (String message) {
	            super(message);
	            this.message = message;
	        }

	        public FormattingException (String requestId, String message, String errorCode) {
	            super(message);
	            this.requestId = requestId;
	            this.message = message;
	            this.errorCode = errorCode;
	        }

	        public String getRequestId() {
	            return this.requestId;
	        }

	        public void setRequestId(String requestId) {
	            this.requestId = requestId;
	        }

	        @Override
	        public String getMessage() {
	            return this.message;
	        }

	        public void setMessage(String message) {
	            this.message = message;
	        }

	        public String getErrorCode() {
	            return this.errorCode;
	        }

	        public void setErrorCode(String errorCode) {
	            this.errorCode = errorCode;
	        }
	    }
