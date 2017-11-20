package com.testvagrant.optimus.tasks

import com.testvagrant.optimus.utils.OptimusReport
import com.testvagrant.optimus.utils.OptimusTearDown
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
       try {
           OptimusTearDown.updateBuildRecord()
           OptimusTearDown.teardown()
       } catch (Exception e) {
       }
        new OptimusReport(project,reportingExtension).generateReport(false);
    }
}
