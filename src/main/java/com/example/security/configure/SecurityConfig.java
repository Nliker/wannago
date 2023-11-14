package com.example.security.configure;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final AuthenticationEntryPoint entryPoint;
	private final JwtFilter jwtFilter;
	
	@Value("${security.allowed-uris}")
    private String[] allowedUris;
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
		log.debug(Arrays.deepToString(allowedUris));
		return httpSecurity
				.csrf().disable()
				.cors().and()
				.headers().frameOptions().disable().and()
				.authorizeRequests()
				.antMatchers(allowedUris).permitAll()
				.antMatchers("/*").authenticated()
				.anyRequest().authenticated().and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint))
                .build();
	}
}
