package com.example.demo.case2.models;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Payment_History {

	private ObjectId id = new ObjectId();
	
	private String payment_type;
	
	private Date date = new Date();
	
	private int transfer_number;

	private ObjectId card;

	private double payment_amount;
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getTransfer_number() {
		return transfer_number;
	}

	public void setTransfer_number(int transfer_number) {
		this.transfer_number = transfer_number;
	}

	public ObjectId getCard() {
		return card;
	}

	public void setCard(ObjectId card) {
		this.card = card;
	}
	
	
	public double getPayment_amount() {
		return payment_amount;
	}
	
	public void setPayment_amount(double payment_amount) {
		this.payment_amount = payment_amount;
	}
	
	
	
}
