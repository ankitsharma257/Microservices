package com.example.Auth2login.visitor.model;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class VisitorSummary implements Serializable{
	
	private int visitNumber;
	private String contactPersonEmailId;
	private VisitorStatus visitorStatus;
//	private Date inTime;
//	private Date outTime;
//	private String contactPersonEmailId;
	

}
