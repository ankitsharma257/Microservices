package com.example.Auth2login.visitor.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators.Max;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.example.Auth2login.visitor.model.Visitor;
import com.example.Auth2login.visitor.model.VisitorStatus;
import com.example.Auth2login.visitor.model.VisitorSummary;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.operation.GroupOperation;
public class VisitorDalImpl implements VisitorDal 
{
/*	ApplicationContext ctx = 
			new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		MongoOperations mongoOperation = 
			(MongoOperations) ctx.getBean("mongoTemplate");*/
	@Autowired
	MongoOperations mongoOperation;
	@Override
	public Optional<Visitor> FindandUpdateByEmail(String VisitorId , String ContactPId ,String Status) 
	{	
		
		Query query = new Query();
		Criteria criteria = new Criteria();  	
		query.addCriteria(Criteria.where("emailId1").is(VisitorId) );
		Visitor userTest3 = mongoOperation.findOne(query, Visitor.class);
		//System.out.println(userTest3);
		List<VisitorSummary> element =userTest3.getVisitorsummary();
		VisitorSummary max = Collections.max(element, Comparator.comparingInt(VisitorSummary::getVisitNumber));
		Query query1 = new Query();
		Criteria criteria2 = new Criteria();
		query1.addCriteria(Criteria.where("usersummary.contactPersonEmailId").is(ContactPId).andOperator(Criteria.where("usersummary.visitNumber").is(max.getVisitNumber()) ) );		
		Update update = new Update();
		update.set("age",38);
		update.set("usersummary.$.userstatus",Status);
		mongoOperation.updateFirst(query1, update, Visitor.class);
		return null;
	}

	
}
