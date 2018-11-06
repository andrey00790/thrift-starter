package com.microservices.examples.simpleserver;

import lombok.Builder;
import lombok.Data;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.net.InetSocketAddress;


public class TestTHttpSertverBuilder {

    @Builder
    public static Server build(int port, TProtocolFactory protocolFactory, TProcessor processor, String servletMapping) {
        Server server = new Server(new InetSocketAddress("127.0.0.1", port));
        ServletHandler handler = new ServletHandler();
        ServletHolder holder = new ServletHolder(new TServlet(processor, protocolFactory));
        handler.addServletWithMapping(holder, servletMapping);
        server.setHandler(handler);
        return server;
    }

    @Data
    public static class THttpServer {
        private final int port;
        private final TProtocolFactory protocolFactory;
        private final TProcessor processor;
        private final String servletMapping;
    }
}
