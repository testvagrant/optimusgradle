package com.testvagrant.optimus.core

import com.testvagrant.optimus.extensions.OptimusExtension
import com.testvagrant.optimus.extensions.OptimusServiceExtension
import com.testvagrant.optimus.tasks.*
import org.gradle.api.Plugin
import org.gradle.api.Project

class OptimusPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.getExtensions().create("optimus",OptimusExtension.class);
        project.getExtensions().create("service",OptimusServiceExtension.class);
        project.getTasks().create("runFragmentation",FragmentationTask.class).setGroup("optimus");
        project.getTasks().create("fragmentationReport",FragmentationReportTask.class).setGroup("optimus");
        project.getTasks().create("runDistribution",DistributionTask.class).setGroup("optimus");
        project.getTasks().create("distributionReport",DistributionReportTask.class).setGroup("optimus");
        project.getTasks().create("optimusSetup",OptimusSetupTask.class).setGroup("optimus");
        project.getTasks().create("spinServices",StartServiceTask.class).setGroup("optimus");
        project.getTasks().create("stopServices",StopServiceTask.class).setGroup("optimus");
    }
}
