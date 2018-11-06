package com.microservices.thrift.pool;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.apache.thrift.TServiceClient;

public class ThriftClientPool extends GenericKeyedObjectPool {
    public ThriftClientPool(KeyedPooledObjectFactory factory, GenericKeyedObjectPoolConfig config) {
        super(factory, config);
    }
}
