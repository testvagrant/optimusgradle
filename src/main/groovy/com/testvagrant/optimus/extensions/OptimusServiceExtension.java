package com.testvagrant.optimus.extensions;

public class OptimusServiceExtension {

    private String database="optimus";
    private String port="8090";
    private String uri="mongodb://localhost:27017";

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
