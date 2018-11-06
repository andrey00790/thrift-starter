package com.microservices.thrift.endpoint;

import com.linecorp.armeria.client.ClientRequestContext;
import com.linecorp.armeria.client.Endpoint;
import com.linecorp.armeria.client.endpoint.EndpointGroup;
import com.linecorp.armeria.client.endpoint.EndpointSelectionStrategy;
import com.linecorp.armeria.client.endpoint.EndpointSelector;

import java.util.Optional;

public final class ServiceDiscoverySelectionStrategy implements EndpointSelectionStrategy {
    @Override
    public EndpointSelector newSelector(EndpointGroup endpointGroup) {
        return null;
    }

    public static final class ServiceDiscoverySelector implements EndpointSelector {

        private final ServiceDiscoveryEndpointGroup endpointGroup;

        public ServiceDiscoverySelector(EndpointGroup endpointGroup) {
            if (endpointGroup instanceof ServiceDiscoveryEndpointGroup) {
                this.endpointGroup = (ServiceDiscoveryEndpointGroup) endpointGroup;
            } else {
                throw new IllegalStateException("endpoint group need be instance ServiceDiscoveryEndpointGroup class");
            }
        }

        @Override
        public EndpointGroup group() {
            return endpointGroup;
        }

        @Override
        public EndpointSelectionStrategy strategy() {
            return EndpointSelectionStrategy.WEIGHTED_ROUND_ROBIN;
        }

        @Override
        public Endpoint select(ClientRequestContext ctx) {
            return Optional.ofNullable(endpointGroup.getLoadBalancerClient().choose(endpointGroup.getServiceId()))
                    .map(it -> Endpoint.of(it.getHost(), it.getPort()))
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("can not chose endpoint for serviceId=%s", endpointGroup.getServiceId())));
        }
    }
}
