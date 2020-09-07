package com.example.demo.case2.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.case2.models.Payees;

public interface PayeesRepository extends MongoRepository<Payees, ObjectId>{

	@Query(value="{ user : ?0 payees_type: MobileBill}",count = true)
	List<Payees> getMobilePayees(ObjectId id);
	
	@Query(value="{ user : ?0 payees_type: Transfer}",count = true)
	List<Payees> getPayees(ObjectId id);
}
