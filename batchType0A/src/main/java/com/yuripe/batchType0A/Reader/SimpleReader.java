package com.yuripe.batchType0A.Reader;

import java.util.Objects;
import java.util.Set;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import com.yuripe.batchType0A.batchprocessing.Model.InsurancePolicyCustom;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class SimpleReader extends FlatFileItemReader<InsurancePolicyCustom> {
    private Validator factory = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public InsurancePolicyCustom doRead() throws Exception {
    	InsurancePolicyCustom InsurancePolicy = super.doRead();
        int len = InsurancePolicy.getPolicyNumber().length();
        if (Objects.isNull(InsurancePolicy)) return null;

        Set<ConstraintViolation<InsurancePolicyCustom>> violations = this.factory.validate(InsurancePolicy);
        if (!violations.isEmpty()) {
            System.out.println(violations);
            String errorMsg = String.format("The input has validation failed. Data is '%s'", InsurancePolicy);
            throw new FlatFileParseException(errorMsg, Objects.toString(InsurancePolicy));
        }
        else {
            return InsurancePolicy;
        }
    }
}