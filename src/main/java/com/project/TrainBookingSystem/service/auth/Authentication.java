package com.project.TrainBookingSystem.service.auth;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.project.TrainBookingSystem.dao.DatabaseHelper;
import com.project.TrainBookingSystem.model.GenericResponse;
import com.project.TrainBookingSystem.resources.QueryConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

@Service
public class Authentication {
	private static final Logger LOG = LoggerFactory.getLogger(Authentication.class);
	@Autowired
	private GenericResponse response;
	@Autowired
	private DatabaseHelper DbHelper;

	public GenericResponse logonAuthenticate(HttpHeaders headers) {
		try {
			if (headers.containsKey("SSO-User") && headers.containsKey("X-Env") && headers.containsKey("role")
					&& validateLogon(headers.get("SSO-User").get(0))) {
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

	public GenericResponse authenticate(HttpHeaders headers) {
		GenericResponse response = new GenericResponse();
		try {
			if (headers.containsKey("SSO-User") && headers.containsKey("X-Env") && headers.containsKey("Authorization")
					&& headers.containsKey("role") && validateUserAuthentacity(headers)) {
				response.setOutputCode(200);
				response.setOutputDescription("All required headers are present");
			} else {
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

	public boolean validateUserAuthentacity(HttpHeaders headers) {
		JWTUtil util = new JWTUtil();
		try {
			Jws<Claims> token = util.extractToken(headers.get("Authorization").get(0), headers.get("X-Env").get(0));
			String userName = token.getBody().get("userName").toString();
			String role = token.getBody().get("role").toString();
			String source = token.getBody().get("source").toString();
			LOG.info("UserModel in authenticity : " + headers);
			if (userName.equals(headers.get("SSO-User").get(0)) && role.equals(headers.get("role").get(0))
					&& source.equals(headers.get("X-Env").get(0))) {
				return true;
			}else {
				LOG.info("User is not authenticated");
			}
		} catch (Exception e) {
			LOG.error("Exception occured while extraction of token.".concat(e.getMessage()));
			return false;
		}
		return false;

	}

	public boolean validateLogon(String UserName) {
		try {
			registerLogOn(UserName);

		} catch (Exception e) {
			LOG.error("Exception occured while extraction of token.".concat(e.getMessage()));
			return false;
		}
		return true;

	}

	public void registerLogOn(String userName) {
        List<Object> paramList = new ArrayList<>();
        String ipv4Address = "";
        try {
            URL url = new URL("https://whatismyipaddress.com/");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("whatismyipaddress.com/ip/")) {
                    String ipv4Pattern = "<span class=\"address\" id=\"ipv4\">.*?<a .*?>(.*?)</a>";
                    ipv4Address = extractIPAddress(line, ipv4Pattern);
                    break;
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception properly in your application
        }

        paramList.add(userName);
        paramList.add(ipv4Address);
        paramList.add(getLocation(ipv4Address));
        DbHelper.executeFunction(QueryConstants.LOGON_REGISTER, paramList);
    }
	
	public String getLocation(String ip_Add) {
		String location = "";
		try {
			URL url = new URL("https://whatismyipaddress.com/ip/" + ip_Add);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("<title>")) {
					int index = line.indexOf(" in ");

			        if (index != -1) {
			            location = line.substring(index + 4, line.length() - 8); // +4 to skip " in ", -8 to exclude "</title>"
			            System.out.println("Location: " + location);
			        } else {
			            System.out.println("Location not found.");
			        }
				}
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;

	}
    private static String extractIPAddress(String htmlContent, String pattern) {
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(htmlContent);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "Address not found";
    }

}
