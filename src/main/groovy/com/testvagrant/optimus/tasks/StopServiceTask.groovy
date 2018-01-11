package com.testvagrant.optimus.tasks

import com.testvagrant.monitor.clients.ServiceClient
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


class StopServiceTask extends DefaultTask {

    StopServiceTask() {
        outputs.upToDateWhen {false}
    }

    @TaskAction
    def stopServices() {
        ServiceClient serviceClient = new ServiceClient();
        if(serviceClient.serviceRunning) {
            serviceClient.stopService()
        }
    }
}
