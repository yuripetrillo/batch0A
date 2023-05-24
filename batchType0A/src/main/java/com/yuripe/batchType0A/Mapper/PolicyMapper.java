package com.yuripe.batchType0A.Mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.yuripe.normalizator.models.InsurancePolicy;

public class PolicyMapper implements FieldSetMapper<InsurancePolicy> {

@Override
public InsurancePolicy mapFieldSet(FieldSet fieldSet) throws BindException {
	
	//InsurancePolicy person = new InsurancePolicy();
    //person.setEffectiveDate(fieldSet.readDate("effective_date", "yyyyMMdd"));
    //person.setExpireDate(fieldSet.readDate("expire_date", "yyyyMMdd"));
    return null;
}

}