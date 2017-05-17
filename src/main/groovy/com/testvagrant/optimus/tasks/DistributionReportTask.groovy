package com.testvagrant.optimus.tasks

import com.testvagrant.optimus.utils.OptimusReport
import org.gradle.api.DefaultTask
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.TaskAction


class DistributionReportTask extends DefaultTask {

    public DistributionReportTask() {
        outputs.upToDateWhen {false}
    }

    @TaskAction
    def reportDistribution() {
        ReportingExtension reportingExtension = project.getExtensions().findByType(ReportingExtension.class);
        new OptimusReport(project,reportingExtension).generateReport(false);
    }
}
