package com.yuripe.batchType0A.Listener;

import java.util.Set;

import org.springframework.batch.core.ItemReadListener;
import com.yuripe.batchType0A.batchprocessing.Model.InsurancePolicyCustom;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class SimpleReaderListener implements ItemReadListener<InsurancePolicyCustom> {
    private Validator factory = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public void afterRead(InsurancePolicyCustom insurancePolicy) {
        Set<ConstraintViolation<InsurancePolicyCustom>> violations = this.factory.validate(insurancePolicy);
        violations.stream().forEach(v -> System.out.println(v));
    }

    @Override
    public void beforeRead() {
    	String x = "";
    	x.concat("");
    }

    @Override
    public void onReadError(Exception e) {
    	String x = "";
    	x.concat("");
    }
}
