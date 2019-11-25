package com.example.Auth2login.security;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.example.Auth2login.user.RoleType;
import com.example.Auth2login.user.User;
import com.example.Auth2login.user.UserPrincipal;
import com.example.Auth2login.user.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomOidcUserService extends OidcUserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

		OidcUser oidcUser = super.loadUser(userRequest);
		log.debug("OAuth2 Oidc User: {}", oidcUser);
		System.out.println("aa");
		Optional<User> user = userRepository.findByEmail(oidcUser.getEmail());
		System.out.println(user);
		if (!user.isPresent()) {
			User newUser = new User();
//			newUser.setId(oidcUser.getName());
			newUser.setFirstName(oidcUser.getGivenName());
			newUser.setLastName(oidcUser.getFamilyName());
			newUser.setEmail(oidcUser.getEmail());
			newUser.setRoles(Arrays.asList(RoleType.ROLE_ADMIN));
			newUser.setPhoneNumber(oidcUser.getPhoneNumber());
			newUser.setPhotoUrl(oidcUser.getPicture());
			userRepository.save(newUser);
			user = Optional.of(newUser);
		}
		UserPrincipal userPrincipal = UserPrincipal.create(user.get());
		return userPrincipal;
	}

}