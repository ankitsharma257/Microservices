package com.example.Auth2login.visitor.controller;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.Auth2login.visitor.model.Visitor;

import com.example.Auth2login.visitor.service.MailService;
import com.example.Auth2login.visitor.service.VisitorService;

@RestController
@RequestMapping(value= "/api")
public class VisitorController 
{
	@Autowired
	MailService ms;
		
	@Autowired
	VisitorService us;

	
	@GetMapping(value="/send")
	public String sendMail()
	{
//		System.out.println("Inside mail api");
		ms.sendMail();
		ms.sendApprovalMail();
		return "Mail sent sucessfully";
		
//		System.out.println("COntroller Working");
	}
	
	@GetMapping(value="/fetch")
	@Scheduled(fixedRate = 5000)
	public void fetchmail() 
	{
		ms.check("imap.gmail.com","imaps","ankit.sharma@accoliteindia.com","aks@25795"); 	
	}
	@PostMapping(value = "/create")
	public ResponseEntity<Visitor> createUser(@RequestBody Visitor visitor)
	{
		System.out.println("inside controller");
		Visitor visitor1 = us.createVisitor(visitor);
		return new ResponseEntity<Visitor>(visitor1, HttpStatus.OK);
			
	}

	@PostMapping(value = "/aaa")
	public String c()
	{
		return "a";
		
			
	}
	/*@PostMapping(value = "/findandupdate/{email1}/{email2}")
	public String Findandupdate(@PathVariable(value= "email") String email1,)
	{
		System.out.println("inside controller");
		us.FindAndUpdate(email1);
		return "updated";
		
		
	}*/
}
