package com.microservices.thrift.pool;

import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.TServiceClient;

public class ThriftClientPooledObject<T extends TServiceClient> extends DefaultPooledObject<T> {

    public ThriftClientPooledObject(T object) {
        super(object);
    }
}
