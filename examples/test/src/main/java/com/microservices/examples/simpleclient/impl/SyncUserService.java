package com.microservices.examples.simpleclient.impl;

import example.TUser;
import example.TUserInfo;
import example.TUserService;
import org.apache.thrift.TException;

public class SyncUserService implements TUserService.Iface {

    @Override
    public TUserInfo getInfo(TUser user) throws TException {
        String age = user.isSetAge() ? String.valueOf(user.getAge()) : "";
        return new TUserInfo(user.getName() + age);
    }
}
