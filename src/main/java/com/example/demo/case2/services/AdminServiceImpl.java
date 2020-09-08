package com.example.demo.case2.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.case2.models.CreditCard;
import com.example.demo.case2.models.User;
import com.example.demo.case2.repository.CreditCardRepository;
import com.example.demo.case2.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private UserRepository refUserRepository;

	@Autowired
	private CreditCardRepository refCardRepository;

	/*
	 * Update User Account
	 * Approve or deny
	 */
	@Override
	public boolean updateUserStatus(ObjectId id, String account_status) {
		Optional<User> user = this.refUserRepository.findById(id);
		if (user.isPresent()) {
			User userUpdate = user.get();
			userUpdate.setAccount_status(account_status);
			refUserRepository.save(userUpdate);
			return true;
		} else
			return false;
	}

	/*
	 * Approve,Deny Credit Card application
	 * Locate credit cards detail using id and based on the card type
	 * Update if found, and set the balance
	 */
	
	@Override
	public boolean updateCardApp(CreditCard card) {
		Optional<CreditCard> refCredit = this.refCardRepository.findById(card.getId());

		if (refCredit.isPresent()) {
			String status = refCredit.get().getCard_status();
			String type = refCredit.get().getCard_type();
			if (type.equals(card.getCard_type()) && status.equals("Pending")) {
				CreditCard ref = refCredit.get();
				ref.setCard_status(card.getCard_status());
				if(card.getCard_status().equals("Active")) {
					ref.setBalance(30000);
					ref.setCard_limit(30000);
				}
				refCardRepository.save(ref);
				return true;
			}
		}

		return false;
	}
	
	/*
	 * Get Customer with pending account status
	 */
	@Override
	public List<CreditCard> getAllApplication() {
		return this.refCardRepository.findByStatus("Pending");
	}

	/*
	 * 
	 * Locate and return json pending account status
	 */
	@Override
	public List<JSONObject> getPendingUser() {
		List<User> pendingUsers = this.refUserRepository.findByStatus("Pending");
		List<JSONObject> myJSONObjects = new ArrayList<JSONObject>();

		for (User user : pendingUsers) {
			JSONObject obj = new JSONObject();
			obj.put("id", user.getId());
			obj.put("Email", user.getEmail());
			myJSONObjects.add(obj);
		}

		return myJSONObjects;
	}

	/*
	 * Locate customer users who are account status active
	 */
	@Override
	public List<User> getActiveUser() {
		return this.refUserRepository.findByActive("Active");
	}

	
	/*
	 * Created a list of pending cards
	 * JsonObject to hold required details
	 * Created a optional to get user email via from the pendingcards list
	 * return the json objects
	 */
	@Override
	public List<JSONObject> getPendingCards() {
		List<CreditCard> pendingCards = this.refCardRepository.findByStatus("Pending");
		List<JSONObject> myJSONObjects = new ArrayList<JSONObject>();

		for (CreditCard card : pendingCards) {
			Optional<User> user = this.refUserRepository.findById(card.getUser());
			if (user.isPresent()) {
				JSONObject obj = new JSONObject();
				obj.put("id", card.getUser());
				obj.put("creditcard_type", card.getCard_type());
				obj.put("email", user.get().getEmail());
				obj.put("card_id",card.getId());
				myJSONObjects.add(obj);
			}

		}
		return myJSONObjects;
	}
	
	/*
	 * List all cards applicants or holders
	 * Create a optional to get user email based on the id located in allCards list
	 * Return json object of all the customer cards details
	 */
	@Override
	public List<JSONObject> getCustomerCardStatus() {
		List<CreditCard> allCards = this.refCardRepository.findAll();
		List<JSONObject> myJSONObjects = new ArrayList<JSONObject>();
		for (CreditCard card : allCards) {
			Optional<User> user = this.refUserRepository.findById(card.getUser());
			if (user.isPresent()) {
				JSONObject obj = new JSONObject();	
				obj.put("id", card.getUser());
				obj.put("account_status",user.get().getAccount_status());
				obj.put("creditcard_type", card.getCard_type());
				obj.put("email", user.get().getEmail());
				obj.put("creditcard_balance",card.getBalance());
				obj.put("creditcard_status",card.getCard_status());
				obj.put("creditcard_type",card.getCard_type());
				obj.put("creditcard_limit", card.getCard_limit());
				obj.put("card_id", card.getId());
				myJSONObjects.add(obj);
			}
		}
		
		return myJSONObjects;
	}

	
	/*
	 * Get username
	 */
	@Override
	public String getName(String email) {
		Optional<User> user = this.refUserRepository.findByEmail(email);
		return user.get().getName();
	}


}
