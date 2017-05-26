package com.testvagrant.optimus.core

import com.testvagrant.optimus.extensions.OptimusExtension
import com.testvagrant.optimus.tasks.DistributionReportTask
import com.testvagrant.optimus.tasks.DistributionTask
import com.testvagrant.optimus.tasks.FragmentationReportTask
import com.testvagrant.optimus.tasks.FragmentationTask
import com.testvagrant.optimus.tasks.OptimusSetupTask
import com.testvagrant.optimus.utils.OptimusSetup
import org.gradle.api.Plugin
import org.gradle.api.Project

class OptimusPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.getExtensions().create("optimus",OptimusExtension.class);
        project.getTasks().create("runFragmentation",FragmentationTask.class).setGroup("optimus");
        project.getTasks().create("fragmentationReport",FragmentationReportTask.class).setGroup("optimus");
        project.getTasks().create("runDistribution",DistributionTask.class).setGroup("optimus");
        project.getTasks().create("distributionReport",DistributionReportTask.class).setGroup("optimus");
        project.getTasks().create("optimusSetup",OptimusSetupTask.class).setGroup("optimus");
    }
}
