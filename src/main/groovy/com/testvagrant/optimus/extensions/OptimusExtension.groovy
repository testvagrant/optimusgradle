package com.testvagrant.optimus.extensions

import org.gradle.api.file.FileCollection


class OptimusExtension {

    private String testFeed;
    private String tags;
    private FileCollection classpath;
    private String devMode;
    private String regression;
    private String env;

    String getTags() {
        return tags
    }

    void setTags(String tags) {
        this.tags = tags
    }

    String getTestFeed() {
        return testFeed
    }

    void setTestFeed(String testFeed) {
        this.testFeed = testFeed
    }

    FileCollection getClasspath() {
        return classpath
    }

    void setClasspath(FileCollection classpath) {
        this.classpath = classpath
    }

    String getDevMode() {
        return devMode
    }

    void setDevMode(String devMode) {
        this.devMode = devMode
    }

    String getRegression() {
        return regression
    }

    void setRegression(String regression) {
        this.regression = regression
    }

    String getEnv() {
        return env
    }

    void setEnv(String env) {
        this.env = env
    }
}
