package com.microservices.thrift.endpoint;

import com.linecorp.armeria.client.endpoint.DynamicEndpointGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;


@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceDiscoveryEndpointGroup extends DynamicEndpointGroup {

    private final String serviceId;
    private final LoadBalancerClient loadBalancerClient;
}
