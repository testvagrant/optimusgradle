package com.testvagrant.optimus.utils

import com.testvagrant.optimus.helpers.DeviceHelper
import com.testvagrant.optimus.register.DeviceRegistrar
import org.gradle.api.Project
import redis.embedded.RedisServer

class OptimusSetup {

    def setup() {
        killRedisServer()
        startRedisServer()
        new DeviceRegistrar().setUpDevices(new DeviceMatrix());
    }

    def List<String> getDevicesForThisRun(Project project,String testFeedName) {
        new DeviceHelper(getAppJson(project,testFeedName)).getConnectedDevicesMatchingRunCriteria();
    }

    private String getAppJson(Project project,String testfeedName) {
        File file = new File(project.projectDir.toString()+"/src/test/resources/"+testfeedName+".json");
        println file.getAbsolutePath()
        if(file.exists()) {
            return file.text
        }
        return ''
    }

    def List<String> getTags(String tags) {
        if(!tags.contains(",")) {
            List<String> tagsList = new ArrayList<>();
            tagsList.add(tags);
            return tagsList;
        }
        List<String> tagsList = Arrays.asList(tags.split(","));
        tagsList.stream().forEach({item -> item.toString().replaceAll("\\s","")});
        return tagsList;
    }


    static def killRedisServer() {
        "redis-cli shutdown".execute()
    }

    static def startRedisServer() {
        RedisServer redisServer = new RedisServer();
        redisServer.start()
    }
}
