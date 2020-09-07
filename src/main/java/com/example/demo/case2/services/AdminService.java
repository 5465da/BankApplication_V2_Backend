package com.example.demo.case2.services;

import java.util.List;

import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.demo.case2.models.CreditCard;
import com.example.demo.case2.models.User;

@Service
public interface AdminService {

	boolean updateUserStatus(ObjectId objectId, String account_status);

	boolean updateCardApp(CreditCard card);
	
	String getName(String email);
	
	List<CreditCard> getAllApplication();
	
	List<JSONObject> getPendingUser();
	
	List<User> getActiveUser();

	List<JSONObject> getPendingCards();

	List<JSONObject> getCustomerCardStatus();
	
}
