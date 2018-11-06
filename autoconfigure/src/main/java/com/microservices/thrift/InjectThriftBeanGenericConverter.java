package com.microservices.thrift;


import com.microservices.thrift.annotation.ThriftAutowire;
import com.microservices.thrift.configuration.ThriftAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.function.Function;

public class InjectThriftBeanGenericConverter implements ConditionalGenericConverter {


    private static final Logger logger = LoggerFactory.getLogger(InjectThriftBeanGenericConverter.class);

    private final BeanFactory beanFactory;

    public InjectThriftBeanGenericConverter(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return null;
    }


    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return ThriftAutoConfiguration.class.isAssignableFrom(sourceType.getObjectType())
                && resolveAnnotation(ThriftAutowire.class, targetType);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        ThriftAutowire annotation = getAnnotation(targetType);

       if(annotation == null){
           throw new IllegalStateException("annotation must not be null");
       }

        CustomObjectSupplier customObjectSupplier = createCustomObjectSupplierBean(annotation, beanFactory);
        return customObjectSupplier.get(getCustomAnnotationSource(targetType).apply(customObjectSupplier.getSelfAnnotationClass()), targetType);

    }

    private CustomObjectSupplier createCustomObjectSupplierBean(ThriftAutowire annotation, BeanFactory beanFactory) {
        Class<? extends CustomObjectSupplier<?, ?>> CustomObjectSupplierBeanClass = annotation.value();
        return beanFactory.getBean(CustomObjectSupplierBeanClass);
    }

    private boolean resolveAnnotation(Class<? extends Annotation> injectClass, TypeDescriptor targetType) {
        Object sourceMetaData = getSourceMetaData(targetType);
        if (sourceMetaData instanceof Field) {
            return targetType.hasAnnotation(injectClass);
        } else if (sourceMetaData instanceof MethodParameter) {
            MethodParameter targetMethodParameter = (MethodParameter) sourceMetaData;
            return AnnotatedElementUtils.isAnnotated(targetMethodParameter.getAnnotatedElement(), injectClass);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private <T extends Annotation> T getAnnotation(TypeDescriptor targetType) {
        Object sourceMetaData = getSourceMetaData(targetType);
        if (sourceMetaData instanceof MethodParameter) {
            MethodParameter targetMethodParameter = (MethodParameter) sourceMetaData;
            return (T) AnnotatedElementUtils.getMergedAnnotation(targetMethodParameter.getAnnotatedElement(),
                    ThriftAutowire.class);
        }

        return (T) targetType.getAnnotation(ThriftAutowire.class);
    }

    private Function<Class<? extends Annotation>, Annotation> getCustomAnnotationSource(TypeDescriptor targetType) {
        Object sourceMetaData = getSourceMetaData(targetType);
        if (sourceMetaData instanceof MethodParameter) {
            MethodParameter targetMethodParameter = (MethodParameter) sourceMetaData;
            return cls -> AnnotatedElementUtils.getMergedAnnotation(targetMethodParameter.getAnnotatedElement(), cls);
        } else {
            return targetType::getAnnotation;
        }

    }

    private Object getSourceMetaData(TypeDescriptor targetType) {
        return targetType.getResolvableType().getSource();
    }
}
