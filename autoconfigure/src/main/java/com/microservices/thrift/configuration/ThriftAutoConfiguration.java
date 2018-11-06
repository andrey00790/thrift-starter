package com.microservices.thrift.configuration;

import com.microservices.thrift.InjectThriftBeanFactoryPostProcessor;
import com.microservices.thrift.client.ThriftClientInjectionSupplier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration("thriftAutoConfiguration")
@AutoConfigureAfter(ThriftPoolConfiguration.class)
@ConditionalOnWebApplication
public class ThriftAutoConfiguration {

    @Configuration
    @Import({ThriftClientInjectionSupplier.class})
    static class ThriftClientConfiguration {
    }

    @Configuration
    @Import({InjectThriftBeanFactoryPostProcessor.class})
    static class InjectThriftPostProcessorConfiguration {
    }
}