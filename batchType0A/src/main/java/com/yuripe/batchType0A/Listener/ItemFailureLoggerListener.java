package com.yuripe.batchType0A.Listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.stereotype.Component;

import com.yuripe.batchType0A.BatchType0AApplication;

@SuppressWarnings("rawtypes")
@Component
public class ItemFailureLoggerListener extends ItemListenerSupport {

	private static final Logger logger = LoggerFactory.getLogger(BatchType0AApplication.class);

    public void onReadError(Exception ex) {
        logger.error("Encountered error on read YURIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII", ex);
    }

    public void onWriteError(Exception ex, Object item) {
        logger.error("Encountered error on write YURIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII", ex);
    }

}