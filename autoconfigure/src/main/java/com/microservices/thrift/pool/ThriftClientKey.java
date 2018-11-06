package com.microservices.thrift.pool;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

@Data
public class ThriftClientKey {
    @Getter(AccessLevel.NONE)
    private final String serviceId;
    private final String path;
    private final Class<?> targetClass;

    public String getServiceId() {

        if (StringUtils.isBlank(serviceId)) {
            return WordUtils.uncapitalize(targetClass.getEnclosingClass().getSimpleName());
        } else {
            return serviceId;
        }
    }
}
