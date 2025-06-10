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

<<<<<<< HEAD
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
=======
/*@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
>>>>>>> hyewon
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
<<<<<<< HEAD
		// 유니크한 사용자 ID 생성
		String userId = "testUser_" + System.currentTimeMillis();
		String password = "secure123";
		String email = userId + "@test.com";

		// 1. 회원가입 요청
		SignupRequestDto signupDto = SignupRequestDto.builder()
				.userId(userId)
				.password(password)
				.confirmPassword(password)
				.email(email)
				.nickname("테스트유저") // nickname 필드가 필수라면 반드시 추가
				.build();

		ResponseEntity<AuthResponseDto> signupResponse = restTemplate.postForEntity(
				getBaseUrl("/api/auth/signup"),
				signupDto,
				AuthResponseDto.class
		);

		assertThat(signupResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(signupResponse.getBody()).isNotNull();
		assertThat(signupResponse.getBody().isSuccess()).isTrue();

		// 2. 로그인 요청
		LoginRequestDto loginDto = LoginRequestDto.builder()
				.userId(userId)
				.password(password)
				.build();

		ResponseEntity<AuthResponseDto> loginResponse = restTemplate.postForEntity(
				getBaseUrl("/api/auth/login"),
				loginDto,
				AuthResponseDto.class
		);

		assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(loginResponse.getBody()).isNotNull();
		assertThat(loginResponse.getBody().isSuccess()).isTrue();
		assertThat(loginResponse.getBody().getData()).isNotBlank(); // 토큰 확인
	}


}
=======

	}



}*/
>>>>>>> hyewon

