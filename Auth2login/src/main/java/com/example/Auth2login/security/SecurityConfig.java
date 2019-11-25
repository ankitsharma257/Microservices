package com.example.Auth2login.security;
//docker run ankitsharma257/websocket:0.0.1-SNAPSHOT
//docker run -p 8080:8080 --name ankitsharma257/websocket:0.0.1-SNAPSHOT --link=mongo  ankitsharma257/websocket:0.0.1-SNAPSHOT
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)

public class SecurityConfig extends WebSecurityConfigurerAdapter 
{
	@Autowired
	private AuthorizationRequestRepository<OAuth2AuthorizationRequest> requestRepository;
	
	@Autowired
	private OidcUserService oidcUserService;
	
	@Autowired
	private AuthenticationSuccessHandler successHandler;
	
	@Autowired
	private AuthenticationFailureHandler failureHandler;
	
	@Autowired
	private Filter tokenAuthenticationFilter;
	
	@Autowired
	private TokenProperties tokenProperties;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception 
    {
    	http
       .cors().and().csrf().disable().formLogin().disable().httpBasic().disable()
       //.sessionManagement()
       //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	   //.and()
       .exceptionHandling().
		authenticationEntryPoint(new CustomAuthenticationEntryPoint())
		.and()
    	.authorizeRequests()
    	.antMatchers("/login/**","/api/sockjs-0.3.4.min.js").permitAll()
    	
		.anyRequest().authenticated()
		.and()
         .oauth2Login()
         
         .authorizationEndpoint()
 		.authorizationRequestRepository(requestRepository)
 		.and()
 		.redirectionEndpoint()
        .and()
		.userInfoEndpoint()
		.oidcUserService(oidcUserService)
		.and()
		.successHandler(successHandler)
		.failureHandler(failureHandler)
		.and()
		.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
    
    @Bean
	public RSAKey rsaKey()
			throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, JOSEException {
		KeyStore keyStore = KeyStore.getInstance(tokenProperties.getType());
		keyStore.load(new ClassPathResource(tokenProperties.getPath()).getInputStream(),
				tokenProperties.getPassword().toCharArray());
		return RSAKey.load(keyStore, tokenProperties.getAlias(), tokenProperties.getPassword().toCharArray());
	}

	@Bean
	@DependsOn(value = "rsaKey")
	public RSASSASigner signer(@Autowired RSAKey rsaKey) throws KeyStoreException, JOSEException {
		return new RSASSASigner(rsaKey);
	}

	@Bean
	@DependsOn(value = "rsaKey")
	RSASSAVerifier verifier(@Autowired RSAKey rsaKey) throws KeyStoreException, JOSEException {
		return new RSASSAVerifier(rsaKey);
	}
	
}