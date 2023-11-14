package com.example.wannago.user.model.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.test.jwt.JwtAuthToken;
import com.example.test.jwt.JwtAuthTokenProvider;
import com.example.wannago.user.model.UserDto;
import com.example.wannago.user.model.mapper.UserMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.wannago.errorcode.CredentialErrorCode;
import com.ssafy.wannago.exception.CredentialExeption;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	private final UserMapper userMapper;
	private final JwtAuthTokenProvider tokenProvider;
	private final RestTemplate restTemplate = new RestTemplate();
	private final Environment env;
	private final PasswordEncoder passwordEncoder;

	@Override
	public String userLogin(UserDto user) throws Exception{
		log.info("class:=================userLogin====================");
		
		UserDto loginUser=userMapper.selectByUserId(user.getUserId());
		if(loginUser==null) {
			throw new CredentialExeption(CredentialErrorCode.NotFoundId.getCode(),CredentialErrorCode.NotFoundId.getDescription());
		}
		log.info("userById: "+loginUser.toString());
		boolean check =passwordEncoder.matches(user.getUserPassword(),loginUser.getUserPassword());
		if(!check) {
			throw new CredentialExeption(CredentialErrorCode.NotMatchIdPassword.getCode(),CredentialErrorCode.NotMatchIdPassword.getDescription());
		}
		return createToken(loginUser);
	}
	
	@Override
	public void createUser(UserDto user) throws Exception{
		log.info("class:=================createUser====================");
		UserDto userById=userMapper.selectByUserId(user.getUserId());
		if(userById!=null) {
			throw new CredentialExeption(CredentialErrorCode.AlreadyExistId.getCode(),CredentialErrorCode.AlreadyExistId.getDescription());
		}
		
		UserDto userByEmailId=userMapper.selectByUserEmailId(user.getUserEmailId());
		if(userByEmailId!=null) {
			throw new CredentialExeption(CredentialErrorCode.AlreadyEmailId.getCode(),CredentialErrorCode.AlreadyEmailId.getDescription());
		}
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        log.info("insertUser: "+user.toString());
        userMapper.insertUser(user);
	}
	
	@Override
	public String createToken(UserDto user) throws Exception{
		log.info("class:=================createToken====================");
		Date expireDate=Date.from(LocalDateTime.now().plusMinutes(180).atZone(ZoneId.systemDefault()).toInstant());
		Map<String,Object> claims=new HashMap<>();
		claims.put("id",user.getUserId());
		JwtAuthToken jwtAuthToken=tokenProvider.createAuthToken(
				user.getUserRole(),
				claims, 
				expireDate);
		return jwtAuthToken.getToken();
	}
	
	@Override
	public String socialLogin(String code, String registrationId) throws Exception {
        log.info("class:=================socialLogin====================");
        String accessToken = getAccessToken(code, registrationId);
        log.debug(accessToken);
        JsonNode userNode = getSocialUserResource(accessToken, registrationId);

        UserDto socialUser = new UserDto();
        switch (registrationId) {
            case "google": {
            	socialUser.setUserId(userNode.get("id").asText());
                String[] emailInfo=userNode.get("email").asText().split("@");
                socialUser.setUserEmailId(emailInfo[0]);
                socialUser.setUserEmailDomain(emailInfo[1]);
                socialUser.setUserName(userNode.get("name").asText());
                break;
            } case "kakao": {
            	socialUser.setUserId(userNode.get("id").asText());
                String[] emailInfo=userNode.get("kakao_account").get("email").asText().split("@");
                socialUser.setUserEmailId(emailInfo[0]);
                socialUser.setUserEmailDomain(emailInfo[1]);
                socialUser.setUserName(userNode.get("kakao_account").get("profile").get("nickname").asText());
                break;
            } case "naver": {
            	socialUser.setUserId(userNode.get("response").get("id").asText());
                String[] emailInfo=userNode.get("response").get("email").asText().split("@");
                socialUser.setUserEmailId(emailInfo[0]);
                socialUser.setUserEmailDomain(emailInfo[1]);
                socialUser.setUserName(userNode.get("response").get("nickname").asText());
                break;
            } default: {
                throw new RuntimeException("UNSUPPORTED SOCIAL TYPE");
            }
        }
        socialUser.setUserPassword("social"+socialUser.getUserId());
        log.info("social User:"+socialUser.toString());
        UserDto userById=userMapper.selectByUserId(socialUser.getUserId());
        if(userById==null) {//이미 존재하는 유저라면 바로 로그인 처리
        	createUser(socialUser);
        }
        return createToken(socialUser);
    }
	
	
	@Override
	public String getAccessToken(String authorizationCode, String registrationId) throws Exception{
		log.info("class:=================getAccessToken====================");
        String clientId = env.getProperty("oauth2." + registrationId + ".client-id");
        String clientSecret = env.getProperty("oauth2." + registrationId + ".client-secret");
        String redirectUri = env.getProperty("oauth2." + registrationId + ".redirect-uri");
        String tokenUri = env.getProperty("oauth2." + registrationId + ".token-uri");
        log.debug("tokenUri: "+tokenUri);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);
        log.debug(entity.toString());
        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }
	
	@Override
	public JsonNode getSocialUserResource(String accessToken, String registrationId) throws Exception {
		log.info("class:=================getSocialUserResource====================");
        String resourceUri = env.getProperty("oauth2."+registrationId+".resource-uri");
        log.debug("resourceUri: "+resourceUri);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        log.debug(entity.toString());
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }
	
	
}
