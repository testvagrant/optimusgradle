package com.testvagrant.optimus.tasks

import com.testvagrant.optimus.utils.OptimusReport
import com.testvagrant.optimus.utils.OptimusTearDown
import org.gradle.api.DefaultTask
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.TaskAction


class FragmentationReportTask extends DefaultTask {

    public FragmentationReportTask() {
        outputs.upToDateWhen {false}
    }

    @TaskAction
    def fragmentationReport() {
        ReportingExtension reportingExtension = project.getExtensions().findByType(ReportingExtension.class);
        OptimusTearDown.updateBuildRecord()
        OptimusTearDown.teardown()
        new OptimusReport(project,reportingExtension).generateReport(true);
    }
}
