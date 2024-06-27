package com.project.TrainBookingSystem.model;

import org.springframework.stereotype.Component;

@Component
public class LogOnResponse {
	private String userName;
	private int outputCode;
	private String tokenDetails;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getOutputCode() {
		return outputCode;
	}
	public void setOutputCode(int outputCode) {
		this.outputCode = outputCode;
	}
	public String getTokenDetails() {
		return tokenDetails;
	}
	public void setTokenDetails(String tokenDetails) {
		this.tokenDetails = tokenDetails;
	}
	@Override
	public String toString() {
		return "LogOnResponse [userName=" + userName + ", outputCode=" + outputCode + ", tokenDetails=" + tokenDetails
				+ "]";
	}
	
	
	
}
