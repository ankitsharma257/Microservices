package com.example.Auth2login.visitor.service;

import com.example.Auth2login.visitor.model.Visitor;

public interface VisitorService 
{
	public Visitor createVisitor(Visitor visitor);

	public void FindAndUpdate(String email1,String email2,String status);

}
