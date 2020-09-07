package com.example.demo.case2.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.case2.models.CreditCard;

public interface CreditCardRepository extends MongoRepository<CreditCard, ObjectId> {

	//Locate Credit card status
	@Query(value="{ card_status : ?0}",count = true)
	List<CreditCard> findByStatus(String Status);
	
	@Query(value="{ user : ?0}",count = true)
	List<CreditCard> getCardDetails(ObjectId id);
	
	@Query(value="{ user : ?0 card_status: Active}",count = true)
	List<CreditCard> getActiveCard(ObjectId id);
	
	@Query(value="{ user : ?0 card_type : ?1}",count = true)
	List<CreditCard> getFromId(ObjectId id,String type);
	
}
