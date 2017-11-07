package com.testvagrant.optimus.extensions

import org.gradle.api.file.FileCollection


class OptimusExtension {

    private String testFeed;
    private String tags;
    private FileCollection classpath;
    private String devMode;

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
        println "Optimus Dev Mode"+devMode
        this.devMode = devMode
    }
}
