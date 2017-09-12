package com.testvagrant.optimus.utils

import com.testvagrant.devicemanagement.core.MongoMain
import com.testvagrant.optimus.helpers.DeviceHelper
import com.testvagrant.optimus.register.DeviceRegistrar
import org.gradle.api.Project


class OptimusSetup {

    def setup(String testFeed) {
        println "Executing MongoMain"
        MongoMain.main();
        JsonValidator.validateTestFeed(testFeed + ".json")
        new DeviceRegistrar().setUpDevices(new DeviceMatrix(testFeed + ".json"))
    }

    List<String> getDevicesForThisRun(Project project, String testFeedName) {
        new DeviceHelper(getAppJson(project, testFeedName)).getConnectedDevicesMatchingRunCriteria();
    }

    private String getAppJson(Project project, String testfeedName) {
        File file = new File(project.projectDir.toString() + "/src/test/resources/" + testfeedName + ".json");
        println file.getAbsolutePath()
        if (file.exists()) {
            return file.text
        }
        return ''
    }

    def List<String> getTags(String tags) {
        List<String> tagsList = new ArrayList<>();
        if (tags == null)
            return tagsList;
        if (tags.contains("~")) {
            if (tags.contains(",")) {
                throw new Exception("Cannot pass multiple tags with ~");
            }
            tagsList.add(tags);
            return tagsList;
        }
        if (!tags.contains(",")) {
            tagsList.add(tags);
            return tagsList;
        }
        tagsList = Arrays.asList(tags.split(","));
        tagsList.stream().forEach({ item -> item.toString().replaceAll("\\s", "") });
        return tagsList;
    }
}
