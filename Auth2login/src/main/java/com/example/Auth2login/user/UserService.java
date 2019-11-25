package com.example.Auth2login.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Auth2login.user.UserRepository;
import com.example.Auth2login.user.User;
import com.example.Auth2login.user.UserPrincipal;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public User getUserDetails(UserPrincipal principal) {
		return getUserDetails(principal.getUsername());
	}

	public User getUserDetails(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}
}
