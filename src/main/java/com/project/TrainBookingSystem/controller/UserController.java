/**
 * 
 */
package com.project.TrainBookingSystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.coyote.Response;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.TrainBookingSystem.dao.DatabaseHelper;
import com.project.TrainBookingSystem.model.GenericResponse;
import com.project.TrainBookingSystem.model.LogOnResponse;
import com.project.TrainBookingSystem.model.TrainDetails;
import com.project.TrainBookingSystem.model.UserModel;
import com.project.TrainBookingSystem.resources.QueryConstants;
import com.project.TrainBookingSystem.service.UserService;
import com.project.TrainBookingSystem.service.auth.Authentication;


/**
 * 
 */
@RestController
@RequestMapping(value="/api")
public class UserController {
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	@Autowired private LogOnResponse response;
	@Autowired private GenericResponse genericResponse;
	@Autowired private UserService userService;
	@Autowired private Authentication authentication;
	@Autowired private DatabaseHelper DBHelper;
	ObjectMapper mapper = new ObjectMapper();
	
	@PostMapping(value="/logon")
	public LogOnResponse logonUser(@RequestHeader HttpHeaders headers, @RequestBody UserModel logonRequest) {
		
		try {
			genericResponse = authentication.logonAuthenticate(headers);
			if(genericResponse.getOutputCode()==200) {
				logonRequest.setUserName(headers.get("SSO-User").get(0));
				logonRequest.setSource(headers.get("X-Env").get(0));
				logonRequest.setRole(headers.get("role").get(0));
				LOG.info("User Model : "+logonRequest);
				response = userService.authorizeLogon(logonRequest);
			}else {
				LOG.error("Exception occured while validating headers");
			}
			
			
		}catch(Exception e) {
			LOG.error("Exception occured while validating headers".concat(e.getMessage()));
		}
		return response;
	}
	
	@GetMapping(value="/traindetails")
	public GenericResponse getTrainDetails(@RequestHeader HttpHeaders headers, @RequestBody UserModel userModel) {
		LOG.info("Execution started for getTrainDetails");
		GenericResponse genericResponse= new GenericResponse();
		try {
			genericResponse = authentication.authenticate(headers,userModel);
			List<Object> paramList = new ArrayList<>();
			if(genericResponse.getOutputCode()==200) {
				Object trainDetails = DBHelper.executeFunction(QueryConstants.GET_TRAIN_DETAILS_QUERY,paramList);
				JsonNode nodeResult = mapper.readTree(trainDetails.toString());
				LOG.info("Fetched TrainDetails : "+nodeResult);
				genericResponse.setOutputData(nodeResult);
				genericResponse.setOutputCode(200);
				genericResponse.setOutputDescription("Train details fetched successfully");
				
			}else {
				genericResponse.setOutputCode(400);
				genericResponse.setOutputDescription("Bad Request");
				genericResponse.setOutputData(null);
			}
			
		}catch(Exception e) {
			LOG.error("Exception occured while fetching the train details : ".concat(e.getMessage()));
			genericResponse.setOutputDescription("Exception occured while fetching the train details : "
					.concat(e.getMessage()));
		}
		return genericResponse;
	}
	
	@PostMapping(value="/register")
	public GenericResponse registerUser(@RequestHeader HttpHeaders headers, @RequestBody UserModel userModel) {
		LOG.info("Inside Register controller");
		GenericResponse genericResponse = new GenericResponse();
		try  {
			List<Object> paramList = new ArrayList<>();
			if(!userModel.equals(null)) {
				paramList.add(userModel.getUserEmail());
				paramList.add(userModel.getRole());
				paramList.add(userModel.getUserAge());
				paramList.add(userModel.getUserLocation());
				paramList.add(userModel.getUserName());
				paramList.add(userModel.getUserPassword());
				paramList.add(userModel.getUserPhNo());
				Object output = DBHelper.executeFunction(QueryConstants.REGISTER_LOG_ON_QUERY, paramList);
				LOG.info("Output from query : "+output);
				JsonNode nodeResult = mapper.readTree(output.toString());
				if(nodeResult.get("code").asInt()!=200) {
					genericResponse.setOutputCode(400);
					genericResponse.setOutputData(null);
					genericResponse.setOutputDescription("Failure");
				}else {
					genericResponse.setOutputCode(200);
					genericResponse.setOutputDescription("Succcess");
					genericResponse.setOutputData(nodeResult.get("data"));
				}
				
			}else {
				genericResponse.setOutputCode(400);
				genericResponse.setOutputData(null);
				genericResponse.setOutputDescription("Pay Load can't be empty");
			}
			
		}catch(Exception e) {
			LOG.error("Exception occured in register controller".concat(e.getMessage()));
			genericResponse.setOutputCode(400);
			genericResponse.setOutputDescription("Error occured");
			genericResponse.setOutputData(null);
			return genericResponse;
		}
		return genericResponse;
	}
}
