package com.microservices.thrift.configuration;


import com.microservices.thrift.pool.ThriftClientKey;
import com.microservices.thrift.pool.ThriftClientPool;
import com.microservices.thrift.pool.ThriftClientPooledObjectFactory;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.apache.thrift.TServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertyResolver;

@Configuration
@AutoConfigureAfter(RibbonAutoConfiguration.class)
public class ThriftPoolConfiguration {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private PropertyResolver propertyResolver;

    @Value("${thrift.com.microservices.thrift.client.max.threads:8}")
    private int maxThreads = 8;

    @Value("${thrift.com.microservices.thrift.client.max.idle.threads:8}")
    private int maxIdleThreads = 8;

    @Value("${thrift.com.microservices.thrift.client.max.total.threads:8}")
    private int maxTotalThreads = 8;


    @Bean
    @ConditionalOnMissingBean(name = "thriftClientsPool")
    public KeyedObjectPool<ThriftClientKey, TServiceClient> thriftClientsPool(){
        GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();
        poolConfig.setMaxTotal(maxThreads);
        poolConfig.setMaxIdlePerKey(maxIdleThreads);
        poolConfig.setMaxTotalPerKey(maxTotalThreads);
        poolConfig.setJmxEnabled(false);
         return new ThriftClientPool(thriftClientPoolFactory(), poolConfig);
    }

    @Bean
    public KeyedPooledObjectFactory<ThriftClientKey, TServiceClient> thriftClientPoolFactory() {
        return new ThriftClientPooledObjectFactory(propertyResolver,loadBalancerClient);
    }

}
