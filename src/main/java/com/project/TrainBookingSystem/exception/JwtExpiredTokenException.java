package com.project.TrainBookingSystem.exception;

public class JwtExpiredTokenException extends RuntimeException {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public JwtExpiredTokenException() {}

  public JwtExpiredTokenException(String msg) {
    super(msg);
  }

  public JwtExpiredTokenException(String msg, Throwable t) {
    super(msg, t);
  }
}
