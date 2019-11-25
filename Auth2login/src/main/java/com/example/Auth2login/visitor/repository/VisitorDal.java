package com.example.Auth2login.visitor.repository;

import java.util.Optional;

import com.example.Auth2login.visitor.model.Visitor;

public interface VisitorDal 
{
	public Optional<Visitor> FindandUpdateByEmail(String email1, String email2, String Status);
}
