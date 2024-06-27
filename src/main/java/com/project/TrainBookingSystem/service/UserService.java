/**
 * 
 */
package com.project.TrainBookingSystem.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.TrainBookingSystem.dao.DatabaseHelper;
import com.project.TrainBookingSystem.model.GenericResponse;
import com.project.TrainBookingSystem.model.LogOnResponse;
import com.project.TrainBookingSystem.model.UserModel;
import com.project.TrainBookingSystem.resources.QueryConstants;
import com.project.TrainBookingSystem.service.auth.JWTUtil;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
@Service
public class UserService {
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	DatabaseHelper dbHelper = new DatabaseHelper();
	ObjectMapper objectMappper = new ObjectMapper();

	public LogOnResponse authorizeLogon(UserModel request) {
		JWTUtil util = new JWTUtil();
		LogOnResponse response = new LogOnResponse();
		GenericResponse genericResponse = new GenericResponse();
		try {
			genericResponse = validateUser(request);
			String token = null;
			if(genericResponse.getOutputCode()==200){
				token = util.createAccessJwtToken(request.getUserName(), request.getRole(), request.getSource());
				response.setOutputCode(200);
				response.setUserName(request.getUserName());
				response.setTokenDetails(token);
			}else {
				response.setOutputCode(400);
				response.setUserName(null);
				response.setTokenDetails(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Exception occured while creating jwt token".concat(e.getMessage()));
			response.setOutputCode(400);
			response.setUserName(null);
			response.setTokenDetails(null);
		}
		return response;

	}
	public  GenericResponse validateUser(UserModel request) {
		GenericResponse genericResponse = new GenericResponse();
		try {
			List<Object> paramList = new ArrayList<>();
			paramList.add(request.getUserEmail());
			paramList.add(request.getUserPassword());
			paramList.add(request.getRole());
			Object output = dbHelper.executeFunction(QueryConstants.VALIDATE_USER_QUERY, paramList);
			JsonNode jsonNode = objectMappper.readTree(output.toString());
			LOG.info("JosnNode from DB : "+jsonNode);
			if(jsonNode.get("code").asInt()==200) {
				LOG.info("Validation successfull, user role : "+request.getRole());
				genericResponse.setOutputCode(jsonNode.get("code").asInt());
				genericResponse.setOutputDescription(jsonNode.get("description").asText());
			}else {
				LOG.info("Validation not successfull, user role : "+request.getRole());
				genericResponse.setOutputCode(400);
				genericResponse.setOutputData(null);
				genericResponse.setOutputDescription("User validation not passed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Exception occured while creating jwt token".concat(e.getMessage()));
			genericResponse.setOutputCode(400);
			genericResponse.setOutputData(null);
			genericResponse.setOutputDescription("Exception occured while user validation");
		}
		return genericResponse;

	}
}
