package com.sharediary;

import com.sharediary.auth.dto.AuthResponseDto;
import com.sharediary.auth.dto.LoginRequestDto;
import com.sharediary.auth.dto.SignupRequestDto;
import com.sharediary.auth.jwt.JwtProvider;
import com.sharediary.user.domain.User;
import com.sharediary.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes=SharedDiaryApplication.class)
public class JooksopApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private String getBaseUrl(String path){
		return "http://localhost:"+port+path;
	}

	@Test
	void 회원가입_및_로그인_테스트() throws InterruptedException {
		//회원가입
		String userId="apiUser_"+System.currentTimeMillis();
		String email=userId+"@example.com";
		String rawPassword="secure123";
		String password=passwordEncoder.encode(rawPassword);

		SignupRequestDto signupDto=SignupRequestDto.builder()
				.userId(userId)
				.password(rawPassword)
				.confirmPassword(rawPassword)
				.nickname("테스트유저")
				.email(email).build();

		ResponseEntity<String> signupRes=restTemplate.postForEntity(
				getBaseUrl("/api/users/register"), signupDto,String.class
		);
		assertThat(signupRes.getStatusCode()).isEqualTo(HttpStatus.OK);

		User saved=userRepository.findByUserId(userId).orElse(null);
		System.out.println("Saved user = " + saved);
		System.out.println("Saved password = " + saved.getPassword());

		assertThat(saved).isNotNull();

		//로그인
		Thread.sleep(200);
		LoginRequestDto loginDto=LoginRequestDto.builder()
				.userId(userId)
				.password(rawPassword)
				.build();

		ResponseEntity<AuthResponseDto> loginRes=restTemplate.postForEntity(
				getBaseUrl("/api/auth/login"), loginDto, AuthResponseDto.class
		);

		assertThat(loginRes.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(loginRes.getBody()).isNotNull();
		assertThat(loginRes.getBody().isSuccess()).isTrue();

		String token=loginRes.getBody().getData();
		assertThat(token).isNotBlank();
		System.out.println("로그인 token = " + token);

		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer "+token);
		HttpEntity<?> entity=new HttpEntity<>(headers);

		System.out.println("✅ 요청 헤더: " + headers);

		ResponseEntity<String> protectedRes=restTemplate.exchange(
				getBaseUrl("/api/diaries/"+userId+"?date=2024-06-01"),
				HttpMethod.GET,
				entity,
				String.class
		);
		assertThat(protectedRes.getStatusCode()).isEqualTo(HttpStatus.OK);
		System.out.println("✅ 보호된 엔드포인트 응답: " + protectedRes.getBody());
	}

}
