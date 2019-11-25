package com.example.Auth2login.visitor.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.Auth2login.visitor.model.Visitor;

@Repository
public interface VisitorRepository extends MongoRepository<Visitor, Integer> , VisitorDal
{
	
}
