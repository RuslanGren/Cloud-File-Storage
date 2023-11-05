package com.example.cloudfilestorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableRedisHttpSession
public class CloudFileStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudFileStorageApplication.class, args);
    }

}
