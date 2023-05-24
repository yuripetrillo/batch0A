package com.yuripe.batchType0A.Mapper;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.context.annotation.Configuration;

import com.yuripe.batchType0A.Exception.FormattingException;
import com.yuripe.normalizator.models.InsurancePolicy;

@Configuration
public class CustomLineMapper implements LineMapper<InsurancePolicy> {

	private static int INSURANCE_POLICY_MAX_LENGTH = 48;
@Override
public InsurancePolicy mapLine(String line, int lineNumber) throws Exception {
	if(line.length() > INSURANCE_POLICY_MAX_LENGTH)	
		throw new FormattingException("ERROR FORMATTING, MAX LENGTH EXCEEDED");
	return null;
}
}