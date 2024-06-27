/**
 * 
 */
package com.project.TrainBookingSystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * This is a userModel class 
 * 
 */
@Entity
@Table(name="User")
public class UserModel {
	private String userName;
	private String userAge;
	@Id
	private String userEmail;
	private String userPhNo;
	private String userPassword;
	private String userLocation;
	private String role;
	private String source;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserAge() {
		return userAge;
	}
	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserPhNo() {
		return userPhNo;
	}
	public void setUserPhNo(String userPhNo) {
		this.userPhNo = userPhNo;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserLocation() {
		return userLocation;
	}
	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}
	
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	@Override
	public String toString() {
		return "UserModel [userName=" + userName + ", userAge=" + userAge + ", userEmail=" + userEmail + ", userPhNo="
				+ userPhNo + ", userPassword=" + userPassword + ", userLocation=" + userLocation + ", role=" + role
				+ ", source=" + source + "]";
	}
	
	
	
}
