package com.microservices.examples.simpleclient;

import com.microservices.thrift.annotation.ThriftClient;
import example.TUserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class TestAuthowireService {

    @ThriftClient(serviceId = "param-user-service", path = "/api")
    private  TUserService.Iface paramUserService;

    private TUserService.Iface methodUserService;


    @ThriftClient(serviceId = "method-user-service", path = "/api")
    public void setMethodUserService(TUserService.Iface methodUserService) {
        this.methodUserService = methodUserService;
    }

    private final TUserService.Iface constructorUserService;


    public TestAuthowireService(@ThriftClient(serviceId = "constructor-user-service", path = "/api")TUserService.Iface constructorUserService) {
        this.constructorUserService = constructorUserService;
    }

}
