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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

	private void signup(String userId){
		String email=userId+"@test.com";
		String password="secure123";

		SignupRequestDto signupDto= SignupRequestDto.builder()
				.userId(userId)
				.password(password)
				.confirmPassword(password)
				.email(email).build();

		ResponseEntity<String> response=restTemplate.postForEntity(
				getBaseUrl("/api/users/register"),
				signupDto,
				String.class
		);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	private String loginAndGetToken(String userId){
		String password="secure123";

		LoginRequestDto loginDto=LoginRequestDto.builder()
				.userId(userId)
				.password(password)
				.build();

		ResponseEntity<AuthResponseDto> response = restTemplate.postForEntity(
				getBaseUrl("/api/auth/login"),
				loginDto,
				AuthResponseDto.class
		);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().isSuccess()).isTrue();

		String token = response.getBody().getData();
		assertThat(token).isNotBlank();
		return token;

	}

	@Test
	void 로그아웃_테스트() throws InterruptedException{

		// 1. 회원가입 및 로그인
		String userId = "logoutTestUser_" + System.currentTimeMillis();
		signup(userId);
		String token = loginAndGetToken(userId);

// 2. 로그아웃 요청
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<String> logoutRes = restTemplate.exchange(
				getBaseUrl("/api/auth/logout"),
				HttpMethod.POST,
				entity,
				String.class
		);

// 🔍 여기까진 200 OK 나와야 정상
		assertThat(logoutRes.getStatusCode()).isEqualTo(HttpStatus.OK);

// 3. 로그아웃 후 다시 요청 (무효 토큰 확인용)
		ResponseEntity<String> afterLogoutRes = restTemplate.exchange(
				getBaseUrl("/api/diaries/some-id"),
				HttpMethod.GET,
				entity,
				String.class
		);

// 🔥 여기서 401 또는 403 나와야 정상 (이미 로그아웃된 토큰)
		assertThat(afterLogoutRes.getStatusCode()).isNotEqualTo(HttpStatus.OK);

	}

}

