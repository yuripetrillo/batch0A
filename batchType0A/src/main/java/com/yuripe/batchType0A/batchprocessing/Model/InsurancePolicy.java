package com.yuripe.batchType0A.batchprocessing.Model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "policy", schema = "BATCH",
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "policyNumber")})
public class InsurancePolicy {

	@Id
    @Column(name = "policyId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long policyId;
	
	private String policyNumber;
	
	private String contractorCustomerCode;
	
	private String insuredCustomerCode;
	
	private Date effectiveDate;
	
	private Date expireDate;
	
	private String state;

	
	
	public InsurancePolicy() {
		
	}

	public InsurancePolicy(Long policyId, String policyNumber, String contractorCustomerCode,
			String insuredCustomerCode, Date effectiveDate, Date expireDate, String state) {
		super();
		this.policyId = policyId;
		this.policyNumber = policyNumber;
		this.contractorCustomerCode = contractorCustomerCode;
		this.insuredCustomerCode = insuredCustomerCode;
		this.effectiveDate = effectiveDate;
		this.expireDate = expireDate;
		this.state = state;
	}

	public Long getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Long policyId) {
		this.policyId = policyId;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getContractorCustomerCode() {
		return contractorCustomerCode;
	}

	public void setContractorCustomerCode(String contractorCustomerCode) {
		this.contractorCustomerCode = contractorCustomerCode;
	}

	public String getInsuredCustomerCode() {
		return insuredCustomerCode;
	}

	public void setInsuredCustomerCode(String insuredCustomerCode) {
		this.insuredCustomerCode = insuredCustomerCode;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return "{\n"
				+ "	  \"data\": [{\n"
				+ "	    \"expireDate\": " + "\"" + expireDate + "\"" + "," + "\"effectiveDate\": " + "\"" + effectiveDate + "\"" + "\n"
						+ "	  }]\n"
						+ "}";
	}
	
}
