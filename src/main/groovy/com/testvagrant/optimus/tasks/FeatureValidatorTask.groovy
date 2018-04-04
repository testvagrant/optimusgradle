package com.testvagrant.optimus.tasks

import com.testvagrant.optimus.extensions.OptimusExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


class FeatureValidatorTask extends DefaultTask{

    FeatureValidatorTask() {
        outputs.upToDateWhen { false }
    }

    @TaskAction
    def validateFeatures() {
        OptimusExtension optimusExtension = project.getExtensions().getByType(OptimusExtension.class);
        optimusExtension.tags;
    }

}
