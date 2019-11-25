package com.example.Auth2login.visitor.service;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Auth2login.visitor.model.Visitor;
import com.example.Auth2login.visitor.model.VisitorStatus;
import com.example.Auth2login.visitor.model.VisitorSummary;
import com.example.Auth2login.visitor.repository.VisitorRepository;
import com.example.Auth2login.visitor.model.Visitor;
import com.example.Auth2login.visitor.model.VisitorSummary;

@Service
public class VisitorServiceImpl implements VisitorService {

	@Autowired
	VisitorRepository userRepository;
	@Autowired
	MailService mailservice;
	
	
	
	@Override
	public Visitor createVisitor(Visitor visitor)
	{
		System.out.println("testtttttttt");
//		System.out.println(user);
//		System.out.println(user.getName());
//		System.out.println(user.getUsersummary().get(0).getEmailId2());
//		System.out.println(user.getUsersummary().size());
		List<VisitorSummary> visitorSummaryList = visitor.getVisitorsummary();
		boolean isScheduled = populateVisitorSummaryDetails(visitorSummaryList);
		Visitor userResult = userRepository.save(visitor);
		if (visitor != null) {
			new Thread(() -> {
				mailservice.sendApprovalMail();
			}).start();
		}
		
		return null;
	}

	private boolean populateVisitorSummaryDetails(List<VisitorSummary> userSummaryList) 
	{

		boolean isScheduled = false;
		if (userSummaryList != null && userSummaryList.size() == 1) 
		{
			VisitorSummary VisitorSummary = userSummaryList.get(0);
			isScheduled = ((VisitorSummary.getVisitorStatus() != null)
					&& (VisitorSummary.getVisitorStatus().equals(VisitorStatus.SCHEDULED)));
			System.out.println(isScheduled);
			if (!isScheduled) 
			{
				VisitorSummary.setVisitorStatus(VisitorStatus.NEW);
				System.out.println("new");
			} 
			if (VisitorSummary.getVisitNumber() == 0) 
			{
				VisitorSummary.setVisitNumber(1);
			}
		}
		return isScheduled;
	}

	@Override
	public void FindAndUpdate(String email1,String email2, String status) 
	{
		userRepository.FindandUpdateByEmail(email1,email2 ,status );
	}

}
