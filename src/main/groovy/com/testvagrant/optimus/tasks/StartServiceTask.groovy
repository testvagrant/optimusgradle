package com.testvagrant.optimus.tasks

import com.testvagrant.monitor.clients.ServiceClient
import com.testvagrant.optimus.extensions.OptimusServiceExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class StartServiceTask extends DefaultTask {

    StartServiceTask() {
        outputs.upToDateWhen {false}
    }

    @TaskAction
    def startServices() {
        OptimusServiceExtension optimusServiceExtension = project.extensions.findByType(OptimusServiceExtension.class)
        ServiceClient serviceClient = new ServiceClient();
        println "Mongo uri "+optimusServiceExtension.uri
        println serviceClient.serviceDown
        if(serviceClient.serviceDown) {
            String port = "--port="+optimusServiceExtension.port
            String database = "--database="+optimusServiceExtension.database
            String uri = "--uri="+optimusServiceExtension.uri
            serviceClient.startService(port,database,uri)
        }
    }
}
