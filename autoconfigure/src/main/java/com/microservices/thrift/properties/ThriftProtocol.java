package com.microservices.thrift.properties;

public enum ThriftProtocol {
    TCOMPACT("tcompact"),
    TBINARY("tbinary"),
    TJSON("tjson"),
    TTEXT("ttext");

    private final String type;

    ThriftProtocol(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
