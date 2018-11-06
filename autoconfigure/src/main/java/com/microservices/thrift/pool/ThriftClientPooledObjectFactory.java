package com.microservices.thrift.pool;

import com.linecorp.armeria.common.SessionProtocol;
import com.microservices.thrift.properties.ThriftProtocol;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.thrift.TServiceClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.env.PropertyResolver;

public class ThriftClientPooledObjectFactory extends BaseKeyedPooledObjectFactory<ThriftClientKey, TServiceClient> {

    private final long DEFAULT_WRITE_TIMEOUT = 1000L;
    private final long DEFAULT_RESPONSE_TIMEOUT = 30000L;
    private final long DEFAULT_MAX_RETRIES = 1L;

    private final PropertyResolver propertyResolver;
    private final LoadBalancerClient loadBalancerClient;

    public ThriftClientPooledObjectFactory(PropertyResolver propertyResolver, LoadBalancerClient loadBalancerClient) {
        this.propertyResolver = propertyResolver;
        this.loadBalancerClient = loadBalancerClient;
    }


    @Override
    public TServiceClient create(ThriftClientKey key) throws Exception {
        return null;
    }

    @Override
    public PooledObject<TServiceClient> wrap(TServiceClient value) {
        return new ThriftClientPooledObject(value);
    }

    private String getUri(String serviceId, String path, ThriftProtocol protocol, SessionProtocol sessionProtocol){
        return String.format("%s+%s//group:%s%s",protocol.getType(),sessionProtocol.uriText(),serviceId,path);
    }

    @Override
    public void activateObject(ThriftClientKey key, PooledObject<TServiceClient> p) throws Exception {
        super.activateObject(key, p);
    }

    @Override
    public void passivateObject(ThriftClientKey key, PooledObject<TServiceClient> p) throws Exception {
        resetAndClose(p);
        super.passivateObject(key, p);
    }

    private void resetAndClose(PooledObject<TServiceClient> p) {
        p.getObject().getInputProtocol().reset();
        p.getObject().getOutputProtocol().reset();
        p.getObject().getInputProtocol().getTransport().close();
        p.getObject().getOutputProtocol().getTransport().close();
    }
}
