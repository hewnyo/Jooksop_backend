package com.sharediary;

import com.sharediary.user.domain.User;
import com.sharediary.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SharedDiaryApplication implements CommandLineRunner{

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(SharedDiaryApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception{
        User user=User.builder()
                .email("test@example.com")
                .password("1234")
                .nickname("tester")
                .profileImageUrl(null)
                .build();

        userRepository.save(user);
        System.out.println("✅ 테스트 유저 저장 성공!");
    }
}
