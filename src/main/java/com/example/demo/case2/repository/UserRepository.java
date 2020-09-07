package com.example.demo.case2.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.case2.models.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
	
	//Locate User details via email
	Optional<User> findByEmail(String email);
	
	//Locate reset token from a specific user
	Optional<User> findByresetToken(String resetToken);
	
	//Find User with a specify status
	@Query(value="{ account_status : ?0}",count = true)
	List<User> findByStatus(String status);
	
	//Find User with Active status
	@Query(value="{ account_status : ?0, roles: USER}",count = true)
	List<User> findByActive(String status);

}
