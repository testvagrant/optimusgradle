package com.testvagrant.optimus.tasks

import com.testvagrant.optimus.extensions.OptimusExtension
import com.testvagrant.optimus.utils.OptimusSetup
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class OptimusSetupTask extends DefaultTask {

    @TaskAction
    def optimusSetup() {
        OptimusExtension optimusExtension = project.getExtensions().findByType(OptimusExtension.class);
        try {
            new OptimusSetup().setup(optimusExtension.testFeed);
        } catch (Exception e) {

        }
    }
}
