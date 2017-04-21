package com.bjtu.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by gimling on 17-4-21.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void registerTest(){
        Map json = new HashMap<String,String>();
        json.put("username","Gimling");
        json.put("password","123456");
        json.put("repeat-password","123456");
        HttpEntity<Map> httpEntity = new HttpEntity<Map>(json);
        ResponseEntity<String> entity = this.restTemplate.postForEntity("/register",httpEntity,String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    //测试url:/toGetPerInfo get
    @Test
    public void userInfoTest(){
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/toGetPerInfo",String.class);
    }

    //测试url：/toChangePerInfo post
    @Test
    public void changePerInfoTest(){
        Map json = new HashMap<String,String>();
        json.put("userHosipital","北郊～～～");
        ResponseEntity<String> entity = this.restTemplate.postForEntity("/toChangePerInfo",json,String.class);
    }
}
