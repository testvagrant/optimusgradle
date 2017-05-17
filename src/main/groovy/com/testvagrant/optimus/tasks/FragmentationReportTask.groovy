package com.testvagrant.optimus.tasks

import com.testvagrant.optimus.utils.OptimusReport
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
        new OptimusReport(project,reportingExtension).generateReport(true);
    }
}
