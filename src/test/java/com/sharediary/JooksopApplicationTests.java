package com.sharediary;

import com.sharediary.auth.dto.SignupRequestDto;
import com.sharediary.user.domain.User;
import com.sharediary.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

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

	@Test
	void 아이디중복확인_테스트() throws InterruptedException{
		String userId="duplicateTest_"+System.currentTimeMillis();
		String email=userId+"@test.com";
		String rawPassword="secure123";
		String password=passwordEncoder.encode(rawPassword);

		SignupRequestDto signupDto=SignupRequestDto.builder()
				.userId(userId)
				.password(rawPassword)
				.confirmPassword(rawPassword)
				.nickname("중복테스트유저")
				.email(email).build();

		ResponseEntity<String> signupRes=restTemplate.postForEntity(
				getBaseUrl("/api/users/register"),
				signupDto,
				String.class
		);

		assertThat(signupRes.getStatusCode()).isEqualTo(HttpStatus.OK);

		User savedUser=userRepository.findByUserId(userId).orElse(null);
		assertThat(savedUser).isNotNull();
		System.out.println("✅ DB 저장된 사용자: " + savedUser.getUserId());

		Thread.sleep(200);
		ResponseEntity<Boolean> duplicateCheckRes=restTemplate.exchange(
				getBaseUrl("/api/auth/check-id?userId="+userId),
				HttpMethod.GET,
				null,
				Boolean.class
		);

		assertThat(duplicateCheckRes.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(duplicateCheckRes.getBody()).isTrue();
		System.out.println("✅ 중복 확인 결과: " + duplicateCheckRes.getBody());

		//존재하지 않는 ID로 확인
		String newUserId=userId+"new";
		ResponseEntity<Boolean> notDuplicateRes=restTemplate.exchange(
				getBaseUrl("/api/auth/check-id?userId="+newUserId),
				HttpMethod.GET,
				null,
				Boolean.class
		);
		assertThat(notDuplicateRes.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(notDuplicateRes.getBody()).isFalse();
		System.out.println("✅ 중복 아님 확인 결과: " + notDuplicateRes.getBody());
	}

}

