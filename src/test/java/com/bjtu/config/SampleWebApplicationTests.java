package com.bjtu.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by Gimling on 2017/4/7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleWebApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    JdbcTemplate template;

    @Test
    public void testJspWithEl() throws Exception {
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/index", String.class);
        assertThat(entity.getBody()).isEqualTo("This is /index page.");
    }

    @Test
    public void testPostMethod() throws Exception{

        String name = "Gimling";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> json= new LinkedMultiValueMap<String, String>();

        json.add("name",name);

        HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity(json,headers);

        ResponseEntity<String> entity = this.restTemplate.postForEntity("/post",httpEntity,String.class);

        assertThat(entity.getBody()).isEqualTo("success");
    }


    @Test
    public void jsonTest(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //MultiValueMap<String, String> json= new LinkedMultiValueMap<String, String>();
        Map json = new HashMap<String,String>();
        json.put("username","Gimling");
        json.put("password","password");
        json.put("code","123");
        HttpEntity<Map<String,String>> httpEntity = new HttpEntity(json,headers);

        ResponseEntity<String> entity = this.restTemplate.postForEntity("/login",httpEntity,String.class);
        assertThat(entity.getStatusCode()).isEqualTo(200);
    }



}
