package com.example.Auth2login.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import com.nimbusds.oauth2.sdk.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthorizationRequestRepository
		implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
	private static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
	private static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		log.debug("loadAuthorization");
		return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
				.map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class)).orElse(null);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("1"+authorizationRequest);
		log.debug("saveAuthorization");
		System.out.println("2"+response);
		if (authorizationRequest == null) {
			removeAuthorizationRequestCookies(request, response);
			return;
		}
		log.debug("create new cookie");
		CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
				CookieUtils.serialize(authorizationRequest));
		String redirect_uri = "http://localhost:8080/api/web";// request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
		System.out.println("3"+redirect_uri);
		if (StringUtils.isNotBlank(redirect_uri))
			CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, CookieUtils.serialize(redirect_uri));
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		log.debug("Remove auth request");
		return this.loadAuthorizationRequest(request);
	}

	public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
		log.debug("cookie deleted");
		CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
		CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
	}
}
