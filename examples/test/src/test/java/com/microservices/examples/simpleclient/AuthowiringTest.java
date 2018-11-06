package com.microservices.examples.simpleclient;


import com.microservices.thrift.annotation.ThriftClient;
import example.TUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthowiringTest {

//    @Autowired
//    private ObjectProvider<TestAuthowireService> authowireServiceObjectProvider;

    //@Test
//    public void testAutowiring(){
//        assertNotNull(authowireServiceObjectProvider);
//        authowireServiceObjectProvider.getObject();
//    }
}
