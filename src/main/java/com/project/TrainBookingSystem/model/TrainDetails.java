package com.project.TrainBookingSystem.model;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="Trains")
public class TrainDetails {
	private String trainName;
	private Timestamp arrivalTime;
	private Timestamp departureTime;
	private String src;
	private String dest;
	private Timestamp timing;
	@Id
	private String trainId;
	public String getTrainName() {
		return trainName;
	}
	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	
	public Timestamp getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(Timestamp arivalTime) {
		this.arrivalTime = arivalTime;
	}
	public Timestamp getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(Timestamp departureTime) {
		this.departureTime = departureTime;
	}
	public String getTrainId() {
		return trainId;
	}
	public void setTrainId(String trainId) {
		this.trainId = trainId;
	}
	public String getDest() {
		return dest;
	}
	public void setDest(String dest) {
		this.dest = dest;
	}
	public Timestamp getTiming() {
		return timing;
	}
	public void setTiming(Timestamp timing) {
		this.timing = timing;
	}
	public String getTraiinId() {
		return trainId;
	}
	public void setTraiinId(String traiinId) {
		this.trainId = traiinId;
	}
	@Override
	public String toString() {
		return "TrainDetails [trainName=" + trainName + ", arivalTime=" + arrivalTime + ", departureTime="
				+ departureTime + ", src=" + src + ", dest=" + dest + ", timing=" + timing + ", trainId=" + trainId
				+ "]";
	}


	
	
}
