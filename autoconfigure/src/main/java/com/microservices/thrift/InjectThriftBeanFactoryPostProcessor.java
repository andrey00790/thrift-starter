package com.microservices.thrift;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;

@Component
public class InjectThriftBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConverterRegistry converterRegistry;

        ConversionService conversionService = beanFactory.getConversionService();
        if (conversionService == null) {
            ConfigurableConversionService newConversionService = new GenericConversionService();
            converterRegistry = newConversionService;
            beanFactory.setConversionService(newConversionService);
        } else if (conversionService instanceof ConverterRegistry) {
            converterRegistry = (ConverterRegistry) conversionService;
        } else {
            throw new IllegalStateException("Cannot configure convertion registry");
        }

        converterRegistry.addConverter(new InjectThriftBeanGenericConverter(beanFactory));
    }
}
