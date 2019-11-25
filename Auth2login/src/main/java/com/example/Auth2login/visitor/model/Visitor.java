package com.example.Auth2login.visitor.model;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="Visitor")
@Data
public class Visitor implements Serializable
{
	@Id
	private int id;
	private String name;
	private String city;
	private int age;
	private int salary;
	private String emailId1;
	
	List<VisitorSummary> visitorsummary;
	

}

