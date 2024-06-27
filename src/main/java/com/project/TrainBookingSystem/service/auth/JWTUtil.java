package com.project.TrainBookingSystem.service.auth;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

@Service
public class JWTUtil {
  private static final Logger LOG = LoggerFactory.getLogger(JWTUtil.class);
  private static String SECRET_KEY="abcd";
  private static String ISSUER="admin";
  private static final int TOKEN_EXPIRY_TIME = 300;


  public String createAccessJwtToken(
      String userName, String role, String source) {
	if (userName.isBlank())
      throw new IllegalArgumentException("Cannot create JWT Token without username");
    Claims claims = Jwts.claims();
    claims.setSubject(userName);
    claims.put("userName", userName);
    claims.put("role", role);
    claims.put("source", source);
    return createToken(claims, source);
  }

  private String createToken(Claims claims, String source) {
    DateTime currentTime = new DateTime();
    return Jwts.builder()
        .setClaims(claims)
        .setIssuer(ISSUER)
        .setIssuedAt(currentTime.toDate())
        .setExpiration(currentTime.plusMinutes(TOKEN_EXPIRY_TIME).toDate())
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY + source)
        .compact();
  }

  public Jws<Claims> extractToken(String token, String source) {
    RawAccessJwtToken rawAccessToken = new RawAccessJwtToken(token);
    return rawAccessToken.parseClaims(SECRET_KEY + source);
  }
}
