package com.project.TrainBookingSystem.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;


import com.project.TrainBookingSystem.exception.JwtExpiredTokenException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class RawAccessJwtToken {

  private String token;
  public RawAccessJwtToken(String token) {
    this.token = token;
  }

  public Jws<Claims> parseClaims(String signingKey) throws JwtExpiredTokenException {
    try {
      return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
    } catch (UnsupportedJwtException
        | MalformedJwtException
        | IllegalArgumentException
        | SignatureException ex) {
      throw new JwtExpiredTokenException("Invalid token", ex);
    } catch (ExpiredJwtException expiredEx) {
      throw new JwtExpiredTokenException("Token expired", expiredEx);
    }
  }
}
