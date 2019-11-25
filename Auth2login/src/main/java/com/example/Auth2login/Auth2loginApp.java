package com.example.Auth2login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@SpringBootApplication
@RestController
public class Auth2loginApp{

	public static void main(String[] args) {
		SpringApplication.run(Auth2loginApp.class, args);
	}

	@GetMapping(value="/a")
	public String A(Model  model) 
	{
		/*System.out.println("a");
		ModelAndView mv=new ModelAndView();
		mv.setViewName("Home");*/
		return "a";
	}
	

	
	   
}
