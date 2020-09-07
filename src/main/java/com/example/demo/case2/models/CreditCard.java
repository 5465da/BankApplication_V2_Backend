package com.example.demo.case2.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

//Credit Card Model
@Document
public class CreditCard {

	private String card_status;
	private String card_type;
	private double card_limit;
	private double balance;
	
	private ObjectId id = new ObjectId();
	
//	@DBRef(db="user")
	private ObjectId user = new ObjectId();

	public String getCard_status() {
		return card_status;
	}


	public void setCard_status(String card_status) {
		this.card_status = card_status;
	}


	public String getCard_type() {
		return card_type;
	}


	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}


	public double getCard_limit() {
		return card_limit;
	}


	public void setCard_limit(double card_limit) {
		this.card_limit = card_limit;
	}


	public double getBalance() {
		return balance;
	}


	public void setBalance(double balance) {
		this.balance = balance;
	}

	public ObjectId getId() {
		return id;
	}


	public void setId(ObjectId id) {
		this.id = id;
	}
	
	public void setUser(ObjectId user) {
		this.user = user;
	}
	
	public ObjectId getUser() {
		return user;
	}
}
