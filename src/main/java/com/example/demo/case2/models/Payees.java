package com.example.demo.case2.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Payees {

	private ObjectId id = new ObjectId();
	
	private int number;
	
	private String name;
	
	private String payees_type;
	
	private ObjectId user;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPayees_type() {
		return payees_type;
	}
	
	public void setPayees_type(String payees_type) {
		this.payees_type = payees_type;
	}

	public ObjectId getUser() {
		return user;
	}
	
	public void setUser(ObjectId user) {
		this.user = user;
	}
	
	
}
