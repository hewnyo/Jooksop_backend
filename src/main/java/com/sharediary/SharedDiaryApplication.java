package com.sharediary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sharediary")
public class SharedDiaryApplication {
    public static void main(String[] args) {
        SpringApplication.run(SharedDiaryApplication.class, args);
    }
}
