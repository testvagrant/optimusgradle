package com.testvagrant.optimus.tasks

import com.testvagrant.optimus.extensions.OptimusExtension
import com.testvagrant.optimus.extensions.OptimusServiceExtension
import com.testvagrant.optimus.utils.OptimusHelper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class OptimusSetupTask extends DefaultTask {

    @TaskAction
    def optimusSetup() {
        OptimusExtension optimusExtension = project.getExtensions().findByType(OptimusExtension.class);
        OptimusServiceExtension serviceExtension = project.getExtensions().findByType(OptimusServiceExtension.class)
        OptimusHelper.setup(project,optimusExtension,serviceExtension)
    }
}
