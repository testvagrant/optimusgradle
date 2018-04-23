package com.testvagrant.optimus.tasks

import com.testvagrant.optimus.extensions.OptimusExtension
import com.testvagrant.optimus.extensions.OptimusServiceExtension
import com.testvagrant.optimus.utils.OptimusHelper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class StartServiceTask extends DefaultTask {

    StartServiceTask() {
        outputs.upToDateWhen {false}
    }

    @TaskAction
    def startServices() {
        OptimusServiceExtension optimusServiceExtension = project.extensions.findByType(OptimusServiceExtension.class)
        OptimusExtension optimusExtension = project.extensions.findByType(OptimusExtension.class)
        println "Mongo uri "+optimusServiceExtension.uri
        println "Services URI "+OptimusHelper.getServiceUrl(optimusServiceExtension)
        String port = "--port="+optimusServiceExtension.port
        String database = "--database="+optimusServiceExtension.database
        String uri = "--uri="+optimusServiceExtension.uri
        project.javaexec {
            main = "com.testvagrant.monitor.clients.ServiceClient"
            classpath = optimusExtension.classpath
            args = [port,database,uri]
            ignoreExitValue = true
            systemProperties = [
                    "serviceUrl": OptimusHelper.getServiceUrl(optimusServiceExtension)
            ]
        }
    }
}
