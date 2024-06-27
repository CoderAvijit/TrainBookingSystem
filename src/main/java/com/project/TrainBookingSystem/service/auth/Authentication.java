package com.project.TrainBookingSystem.service.auth;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.project.TrainBookingSystem.model.GenericResponse;
import com.project.TrainBookingSystem.model.LogOnResponse;
import com.project.TrainBookingSystem.model.UserModel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import org.slf4j.Logger;

@Service
public class Authentication {
	private static final Logger LOG = LoggerFactory.getLogger(Authentication.class);
	@Autowired
	private GenericResponse response;

	public GenericResponse logonAuthenticate(HttpHeaders headers) {
		try {
			if (headers.containsKey("SSO-User") 
					&& headers.containsKey("X-Env") && 
					headers.containsKey("role")) {
				response.setOutputCode(200);
				;
				response.setOutputDescription("Required headers are present");
				return response;
			}

		} catch (Exception e) {
			LOG.error("Exception Occured while getting the headers");
			response.setOutputCode(400);
			response.setOutputDescription("Bad Request");
			return response;
		}
		return response;
	}

	public GenericResponse authenticate(HttpHeaders headers, UserModel userModel) {
		GenericResponse response = new GenericResponse();
		try {
			if (headers.containsKey("SSO-User") 
					&& headers.containsKey("X-Env") 
					&& headers.containsKey("Authorization")
					&& headers.containsKey("role")
					&& validateUserAuthentacity(headers,userModel)) {
					response.setOutputCode(200);
					response.setOutputDescription("All required headers are present");
				}else {
				response.setOutputCode(400);
				response.setOutputDescription("All required headers not present");
			}
		} catch (Exception e) {
			response.setOutputCode(400);
			response.setOutputDescription("Exception occured ".concat(e.getMessage()));
			return response;
		}
		return response;
	}

	public boolean validateUserAuthentacity(HttpHeaders headers,UserModel userModel) {
		JWTUtil util = new JWTUtil();
		try {
		Jws<Claims> token = util.extractToken(headers.get("Authorization").get(0),
				headers.get("X-Env").get(0));
		String userName = token.getBody().get("userName").toString();
		String role = token.getBody().get("role").toString();
		String source = token.getBody().get("source").toString();
		LOG.info("UserModel in authenticity : "+headers);
		if(userName.equals(headers.get("SSO-User").get(0)) 
				&& role.equals(headers.get("role").get(0))
				&& source.equals(headers.get("X-Env").get(0))) {
			return true;
		}
		}catch(Exception e) {
			LOG.error("Exception occured while extraction of token.".concat(e.getMessage()));
			return false;
		}
		return false;

	}
}
