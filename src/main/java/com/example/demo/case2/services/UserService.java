package com.example.demo.case2.services;

import java.util.List;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.example.demo.case2.models.CreditCard;
import com.example.demo.case2.models.Payees;
import com.example.demo.case2.models.Payment_History;
import com.example.demo.case2.models.User;

@Service
public interface UserService {
	Boolean register(User user);

	List<JSONObject> findCardDetails(String userNameFromJwtToken) throws Exception;

	boolean applyCreditCard(String email, CreditCard card);

	List<JSONObject> getCardApplicationStatus(String email) throws Exception;

	boolean addPayee(Payees refPayee,String email);
	
	boolean pay(Payment_History refPay,String email);

	List<JSONObject> getPaymentHistory(String email);

	List<JSONObject> getCardPaymentHistory(CreditCard refCard);

	List<JSONObject> getPayeeMobile(String email);

	List<JSONObject> getPayees(String email);

	String getName(String email);
}
