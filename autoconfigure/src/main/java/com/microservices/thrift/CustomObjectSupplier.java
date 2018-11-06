package com.microservices.thrift;

import org.springframework.core.convert.TypeDescriptor;

import java.lang.annotation.Annotation;

public interface CustomObjectSupplier<T, I extends Annotation> {

    T get(I annotation, TypeDescriptor targetType);

    Class<I> getSelfAnnotationClass();
}
