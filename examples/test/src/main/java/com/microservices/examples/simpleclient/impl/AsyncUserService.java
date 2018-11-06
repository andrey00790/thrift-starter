package com.microservices.examples.simpleclient.impl;

import example.TUser;
import example.TUserInfo;
import example.TUserService;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

public class AsyncUserService implements TUserService.AsyncIface{

    @Override
    public void getInfo(TUser user, AsyncMethodCallback resultHandler) throws TException {
         resultHandler.onComplete(getUserInfo(user));
    }

    private TUserInfo getUserInfo(TUser user){
        String age = user.isSetAge() ? String.valueOf(user.getAge()) : "";
        return new TUserInfo(user.getName() + age);
    }
}
