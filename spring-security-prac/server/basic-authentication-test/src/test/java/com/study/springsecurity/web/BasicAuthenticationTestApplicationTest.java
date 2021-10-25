package com.study.springsecurity.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BasicAuthenticationTestApplicationTest {

    @LocalServerPort
    int port;

    RestTemplate client = new RestTemplate();

    private String greetingUrl() {
        return "http://localhost:" + port + "/greeting";
    }

    @DisplayName("1. Authentication fail")
    @Test
    void test_1() {

        // 인증이 안됬을 경우 예외 발생
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            client.getForObject(greetingUrl(), String.class);
        });

        // 401 에러를 내려 보낸다.
        assertEquals(401, exception.getRawStatusCode());
    }

    @DisplayName("2. Authentication success")
    @Test
    void test_2() {

        // 헤더에 Authentication 정보를 담아 인증 시도
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(
                "user1:1111".getBytes()
        ));
        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = client.exchange(greetingUrl(), HttpMethod.GET, entity, String.class);

        // 인증이 되면 hello 리턴
        assertEquals("hello", response.getBody());

        System.out.println("response = " + response.getBody());
    }
}