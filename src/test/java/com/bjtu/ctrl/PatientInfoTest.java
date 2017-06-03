package com.bjtu.ctrl;

import com.bjtu.config.SpringBoot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by gimling on 17-5-21.
 */
@ContextConfiguration(classes = SpringBoot.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(PatientInfoCtrl.class)
public class PatientInfoTest {

    @Autowired
    MockMvc mockMvc;


    @Before
    public void setUp(){

    }

    @Test
    public void shouldReturnDefaultMessage() throws Exception {

    }
}
