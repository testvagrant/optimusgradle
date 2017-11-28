package com.testvagrant.optimus.tasks

import com.testvagrant.monitor.MongoMain
import com.testvagrant.optimus.extensions.OptimusExtension
import com.testvagrant.optimus.utils.OptimusSetup
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import static com.testvagrant.monitor.radiator.MongoDriverService.mongoService

class OptimusSetupTask extends DefaultTask {

    @TaskAction
    def optimusSetup() {
        try {
            OptimusExtension optimusExtension = project.getExtensions().findByType(OptimusExtension.class);
            try {
                mongoService().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            new OptimusSetup().setup(optimusExtension.testFeed);
        } finally {
            MongoMain.closeMongo();
        }
    }
}
