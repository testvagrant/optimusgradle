package com.testvagrant.optimus.utils

import com.testvagrant.optimus.extensions.OptimusExtension
import com.testvagrant.optimus.extensions.OptimusServiceExtension
import com.testvagrant.optimus.helpers.DeviceHelper
import org.gradle.api.Project


class OptimusHelper {

    static def setup(Project project, OptimusExtension optimusExtension, OptimusServiceExtension serviceExtension) {
        project.javaexec {
            main = "com.testvagrant.optimus.OptimusMain"
            classpath = optimusExtension.classpath
            args = [optimusExtension.testFeed]
            ignoreExitValue = true
            systemProperties = [
                    "serviceUrl"   : getServiceUrl(serviceExtension)
            ]
        }
    }
    static def getServiceUrl(OptimusServiceExtension optimusServiceExtension) {
        return String.format("http://localhost:%s/v1",optimusServiceExtension.port)
    }

    static def setupServiceEnvoirment(OptimusServiceExtension serviceExtension) {
        System.setProperty("serviceUrl",getServiceUrl(serviceExtension));
    }

    static List<String> getDevicesForThisRun(Project project, String testFeedName) {
        new DeviceHelper(getAppJson(project, testFeedName)).getConnectedDevicesMatchingRunCriteria();
    }


}
