package com.sharediary;

import com.sharediary.auth.dto.AuthResponseDto;
import com.sharediary.auth.dto.LoginRequestDto;
import com.sharediary.auth.dto.SignupRequestDto;
import com.sharediary.user.domain.User;
import com.sharediary.user.dto.UserResponseDto;
import com.sharediary.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/*@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = SharedDiaryApplication.class)

public class JooksopApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private String getBaseUrl(String path){
		return "http://localhost:"+port+path;
	}


	@Test
	void 회원가입_로그인_정상_작동_테스트() {

	}



}*/

