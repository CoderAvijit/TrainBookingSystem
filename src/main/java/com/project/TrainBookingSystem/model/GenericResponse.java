package com.project.TrainBookingSystem.model;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class GenericResponse {
	private int outputCode;
	private String outputDescription;
	private JsonNode outputData;
	public int getOutputCode() {
		return outputCode;
	}
	public void setOutputCode(int outputCode) {
		this.outputCode = outputCode;
	}
	public String getOutputDescription() {
		return outputDescription;
	}
	public void setOutputDescription(String outputDescription) {
		this.outputDescription = outputDescription;
	}
	public JsonNode getOutputData() {
		return outputData;
	}
	public void setOutputData(JsonNode outputData) {
		this.outputData = outputData;
	}
	@Override
	public String toString() {
		return "GenericResponse [outputCode=" + outputCode + ", outputDescription=" + outputDescription
				+ ", outputData=" + outputData + "]";
	}
	
	
	
	
}
