package com.yuripe.batchType0A.batchprocessing.Processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.yuripe.batchType0A.batchprocessing.Model.InsurancePolicyCustom;
import com.yuripe.normalizator.models.InsurancePolicy;

public class PolicyItemProcessor implements ItemProcessor<InsurancePolicyCustom, InsurancePolicyCustom>{

	private static final Logger log = LoggerFactory.getLogger(PolicyItemProcessor.class);

	  @Override
	  public InsurancePolicyCustom process(final InsurancePolicyCustom policy) throws Exception {
	    /*final String firstName = policy.getContractorCustomerCode().toUpperCase();
	    final String lastName = policy.getInsuredCustomerCode().toUpperCase();

	    final InsurancePolicy transformedPolicy = new InsurancePolicy(null, firstName, lastName, lastName, null, null, lastName);
	     */
		  
	    log.info("Converting (" + policy + ")");

	    return policy;
	  }

}
