package com.example.demo.case2.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.case2.models.Payment_History;

public interface PaymentHistoryRepository extends MongoRepository<Payment_History, ObjectId>{

	@Query(value="{ card : ?0 }")
	List<Payment_History> findByCard(ObjectId id);
	
	@Query(value="{ card : ?0 }")
	Optional<Payment_History> findByCard2(ObjectId id);

	@Query(value="{ card : ?0 }")
	List<Payment_History> findByCard3(ObjectId id);

}
