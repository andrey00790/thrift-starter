package com.microservices.thrift.client;

import com.linecorp.armeria.client.endpoint.EndpointGroupRegistry;
import com.microservices.thrift.CustomObjectSupplier;
import com.microservices.thrift.annotation.ThriftClient;
import com.microservices.thrift.endpoint.ServiceDiscoveryEndpointGroup;
import com.microservices.thrift.endpoint.ServiceDiscoverySelectionStrategy;
import com.microservices.thrift.pool.ThriftClientKey;
import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.thrift.TServiceClient;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
@ConditionalOnBean(LoadBalancerClient.class)
public class ThriftClientInjectionSupplier implements CustomObjectSupplier<Object, ThriftClient> {

    private final LoadBalancerClient loadBalancerClient;
    private final KeyedObjectPool<ThriftClientKey, TServiceClient> thriftClientsPool;

    @Autowired
    public ThriftClientInjectionSupplier(LoadBalancerClient loadBalancerClient, KeyedObjectPool<ThriftClientKey, TServiceClient> thriftClientsPool) {
        this.loadBalancerClient = loadBalancerClient;
        this.thriftClientsPool = thriftClientsPool;
    }


    @Override
    public Object get(ThriftClient annotation, TypeDescriptor targetType) {
        createEndpointGroup(annotation.serviceId());
        return ProxyFactory.getProxy(targetType.getObjectType(), (MethodInterceptor) it -> {

                    ThriftClientKey thriftClientKey = new ThriftClientKey(
                            annotation.serviceId(),
                            annotation.path(),
                            targetType.getObjectType()
                    );
                    TServiceClient serviceClient = null;
                    try {
                        serviceClient = thriftClientsPool.borrowObject(thriftClientKey);
                        return ReflectionUtils.invokeMethod(it.getMethod(), serviceClient, it.getArguments());
                    } finally {
                        if (serviceClient != null) {
                            thriftClientsPool.returnObject(thriftClientKey, serviceClient);
                        }
                    }
                }
        );
    }

    @Override
    public Class<ThriftClient> getSelfAnnotationClass() {
        return ThriftClient.class;
    }

    private void createEndpointGroup(String serviceId) {
        ServiceDiscoveryEndpointGroup group = new ServiceDiscoveryEndpointGroup(serviceId, loadBalancerClient);
        EndpointGroupRegistry.register(serviceId, group, new ServiceDiscoverySelectionStrategy());
    }
}