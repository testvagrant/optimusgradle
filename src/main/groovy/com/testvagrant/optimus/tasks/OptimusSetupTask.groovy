package com.testvagrant.optimus.tasks

import com.testvagrant.optimus.utils.OptimusSetup
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


class OptimusSetupTask extends DefaultTask {

    public OptimusSetupTask() {
        outputs.upToDateWhen {false}
    }

    @TaskAction
    def optimusSetup() {
        new OptimusSetup().setup();
    }
}
