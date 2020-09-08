package com.example.demo.case2.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.case2.models.CreditCard;
import com.example.demo.case2.models.Payees;
import com.example.demo.case2.models.Payment_History;
import com.example.demo.case2.models.User;
import com.example.demo.case2.repository.CreditCardRepository;
import com.example.demo.case2.repository.PayeesRepository;
import com.example.demo.case2.repository.PaymentHistoryRepository;
import com.example.demo.case2.repository.UserRepository;
import com.mongodb.MongoClientException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository refUserRepository;

	@Autowired
	private CreditCardRepository refCardRepository;

	@Autowired
	private PayeesRepository refPayeesRepository;

	@Autowired
	private PaymentHistoryRepository refPaymentHistoryRepo;

	@Autowired
	PasswordEncoder encoder;

	/*
	 * Register Locate if user present in database, if found return false else
	 * encrypt the password and grant a role to user Save details to database
	 */

	@Override
	public Boolean register(User user) {
		Optional<User> users = this.refUserRepository.findByEmail(user.getEmail());
		if (users.isPresent()) {
			return false;
		} 
		else
		user.setPassword(encoder.encode(user.getPassword()));
		user.grantAuthority("USER");
		user.setAccount_status("Pending");
		refUserRepository.save(user);

		return true;
	}

	/*
	 * 
	 * Apply Credit Card Locate if email exists If exists query to save new card
	 * application
	 * 
	 */
	@Override
	public boolean applyCreditCard(String email, CreditCard card) {
		Optional<User> user = this.refUserRepository.findByEmail(email);

		if (user.isPresent()) {
			List<CreditCard> refCredit = this.refCardRepository.getFromId(user.get().getId(), card.getCard_type());
			if (refCredit.isEmpty()) {
				card.setUser(user.get().getId());
				card.setCard_status("Pending");
				card.setCard_type(card.getCard_type());
				refCardRepository.save(card);
				return true;
			} else
				return false;
		}
		return false;

	}

	/**
	 * For Overview page Get Active Credit card details
	 */
	@Override
	public List<JSONObject> findCardDetails(String email) throws Exception {

		Optional<User> user = this.refUserRepository.findByEmail(email);
		List<JSONObject> myJSONObjects = new ArrayList<JSONObject>();

		List<CreditCard> refCredit = this.refCardRepository.getActiveCard(user.get().getId());
		for (CreditCard card : refCredit) {
			if (user.isPresent()) {
				JSONObject obj = new JSONObject();
				obj.put("card_type", card.getCard_type());
				obj.put("card_limit", card.getCard_limit());
				obj.put("card_id", card.getId());
				obj.put("balance", card.getBalance());
				obj.put("id", user.get().getId());
				obj.put("card_status", card.getCard_status());
				obj.put("email", email);
				myJSONObjects.add(obj);
			} else
				continue;
		}

		return myJSONObjects;
	}

	/*
	 * Get Credit Card Application status
	 */
	@Override
	public List<JSONObject> getCardApplicationStatus(String email) throws Exception {

		Optional<User> user = this.refUserRepository.findByEmail(email);
		List<JSONObject> myJSONObjects = new ArrayList<JSONObject>();

		List<CreditCard> refCredit = this.refCardRepository.getCardDetails(user.get().getId());
		for (CreditCard card : refCredit) {
			if (user.isPresent()) {
				JSONObject obj = new JSONObject();
				obj.put("card_type", card.getCard_type());
				obj.put("card_id", card.getId());
				obj.put("id", user.get().getId());
				obj.put("card_status", card.getCard_status());
				obj.put("email", email);
				myJSONObjects.add(obj);
			} else
				continue;
		}

		return myJSONObjects;
	}

	// Add payees
	@Override
	public boolean addPayee(Payees refPayee, String email) {
		Optional<User> user = this.refUserRepository.findByEmail(email);
		if (user.isPresent()) {
			refPayee.setUser(user.get().getId());
			refPayee.setName(refPayee.getName());
			refPayee.setNumber(refPayee.getNumber());
			refPayee.setPayees_type(refPayee.getPayees_type());
			refPayeesRepository.save(refPayee);
			return true;
		}
		return false;
	}

	/*
	 * Get payees for mobile
	 */
	@Override
	public List<JSONObject> getPayeeMobile(String email) {
		Optional<User> user = this.refUserRepository.findByEmail(email);
		List<JSONObject> myJSONObjects = new ArrayList<JSONObject>();
		List<Payees> refPayee = this.refPayeesRepository.getMobilePayees(user.get().getId());

		for (Payees payees : refPayee) {
			if (user.isPresent()) {
				JSONObject obj = new JSONObject();
				obj.put("id", payees.getId());
				obj.put("name", payees.getName());
				obj.put("user", payees.getUser());
				obj.put("number", payees.getNumber());
				obj.put("payee_type", payees.getPayees_type());
				myJSONObjects.add(obj);
			} else
				continue;
		}

		return myJSONObjects;
	}

	/*
	 * 
	 * Get all payess
	 */
	@Override
	public List<JSONObject> getPayees(String email) {
		Optional<User> user = this.refUserRepository.findByEmail(email);
		List<JSONObject> myJSONObjects = new ArrayList<JSONObject>();
		List<Payees> refPayee = this.refPayeesRepository.getPayees(user.get().getId());

		for (Payees payees : refPayee) {
			if (user.isPresent()) {
				JSONObject obj = new JSONObject();
				obj.put("id", payees.getId());
				obj.put("name", payees.getName());
				obj.put("user", payees.getUser());
				obj.put("number", payees.getNumber());
				obj.put("payee_type", payees.getPayees_type());
				myJSONObjects.add(obj);
			} else
				continue;
		}

		return myJSONObjects;
	}

	/*
	 * Make payment
	 */
	@Override
	public boolean pay(Payment_History refPay, String email) {

		Optional<CreditCard> refCard = this.refCardRepository.findById(refPay.getCard());

		if (refCard.isPresent()) {
			double balance = refCard.get().getBalance() - refPay.getPayment_amount();
			CreditCard card = refCard.get();
			card.setBalance(balance);
			card.setCard_limit(balance);

			refPay.setDate(new Date());
			refPay.setCard(refPay.getCard());
			refPay.setPayment_type(refPay.getPayment_type());
			refPay.setTransfer_number(1);
			refPay.setPayment_amount(refPay.getPayment_amount());

			refCardRepository.save(card);
			refPaymentHistoryRepo.save(refPay);
			return true;
		}

		else
			return false;
	}

	/*
	 * return payment history in json form
	 * 
	 */
	@Override
	public List<JSONObject> getPaymentHistory(String email) {
		Optional<User> user = this.refUserRepository.findByEmail(email);
		List<CreditCard> refCard = this.refCardRepository.getCardDetails(user.get().getId());
		Optional<Payment_History> refPayHistory;
		List<JSONObject> myJSONObjects = new ArrayList<JSONObject>();

		for (CreditCard creditCard : refCard) {
			JSONObject obj = new JSONObject();
			refPayHistory = this.refPaymentHistoryRepo.findByCard2(creditCard.getId());
			obj.put("id", refPayHistory.get().getId());
			obj.put("payment_amount", refPayHistory.get().getPayment_amount());
			obj.put("payment_type", refPayHistory.get().getPayment_type());
			obj.put("transfer_number", refPayHistory.get().getTransfer_number());
			obj.put("date_time", refPayHistory.get().getDate());
			obj.put("card", refPayHistory.get().getCard());
			obj.put("card_type", creditCard.getCard_type());
			myJSONObjects.add(obj);

		}

		return myJSONObjects;
	}

	/*
	 * Get payment history by credit details return in a json form
	 */
	@Override
	public List<JSONObject> getCardPaymentHistory(CreditCard card) {

		List<JSONObject> myJSONObjects = new ArrayList<JSONObject>();
		List<Payment_History> list = this.refPaymentHistoryRepo.findByCard3(card.getUser());

		for (Payment_History payment_History : list) {
			JSONObject obj = new JSONObject();
			obj.put("id", payment_History.getId());
			obj.put("payment_amount", payment_History.getPayment_amount());
			obj.put("payment_type", payment_History.getPayment_type());
			obj.put("transfer_number", payment_History.getTransfer_number());
			obj.put("date_time", payment_History.getDate());
			obj.put("card", payment_History.getCard());
			myJSONObjects.add(obj);
		}
		return myJSONObjects;
	}

	/*
	 * Get Name
	 */
	@Override
	public String getName(String email) {
		Optional<User> user = this.refUserRepository.findByEmail(email);
		return user.get().getName();
	}

}
