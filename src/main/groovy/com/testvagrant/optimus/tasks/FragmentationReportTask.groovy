package com.testvagrant.optimus.tasks

import com.testvagrant.optimus.utils.OptimusReport
import com.testvagrant.optimus.utils.OptimusTearDown
import org.gradle.api.DefaultTask
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.TaskAction


class FragmentationReportTask extends DefaultTask {

    FragmentationReportTask() {
        outputs.upToDateWhen {false}
    }

    @TaskAction
    def fragmentationReport() {
        ReportingExtension reportingExtension = project.getExtensions().findByType(ReportingExtension.class);
        try {
            OptimusTearDown.updateBuildRecord()
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            OptimusTearDown.teardown()
        }
        new OptimusReport(project,reportingExtension).generateReport(true);
    }
}
