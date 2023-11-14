package com.example.security.configure;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.wannago.jwt.JwtAuthToken;
import com.example.wannago.jwt.JwtAuthTokenProvider;
import com.ssafy.wannago.errorcode.JwtErrorCode;
import com.ssafy.wannago.exception.JwtException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter{
	public static final String AUTHORIZATION_HEADER="access_token";
	private JwtAuthTokenProvider tokenProvider;
	
	public JwtFilter(JwtAuthTokenProvider tokenProvider) {
		this.tokenProvider=tokenProvider;
	}
	
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.debug("class:==================doFilterInternal=====================");
		log.debug(request.getRequestURL().toString());
		log.debug("doFilterInternal");
		Optional<String> token=resolveToken(request);
		log.debug("token:"+token+ " "+token.isPresent());
		if(token.isPresent()) {
			JwtAuthToken jwtAuthToken=tokenProvider.convertAuthToken(token.get());
			try {
				Authentication authentication=tokenProvider.getAuthentication(jwtAuthToken);
				log.debug(authentication.toString());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}catch(Exception e) {
				request.setAttribute("exception", e);
			}
		}else {
			request.setAttribute("exception",new JwtException(JwtErrorCode.JwtReqired.getCode(),JwtErrorCode.JwtReqired.getDescription()));
		}
		log.debug("doFilter");
		filterChain.doFilter(request, response);
		log.debug("endFilter");
	}
	
	private Optional<String> resolveToken(HttpServletRequest request){
		String authToken=request.getHeader(AUTHORIZATION_HEADER);
		if(StringUtils.hasText(authToken)) {
			return Optional.of(authToken);
		}else {
			return Optional.empty();
		}
	}

}
