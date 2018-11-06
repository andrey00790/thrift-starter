package com.microservices.thrift.annotation;



import com.microservices.thrift.client.ThriftClientInjectionSupplier;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ThriftAutowire(ThriftClientInjectionSupplier.class)
@Documented
@Inherited
public @interface ThriftClient {

    String serviceId() default "";

    String path() default "";
}
