package com.testvagrant.optimus.utils

import net.masterthought.cucumber.Configuration
import net.masterthought.cucumber.ReportBuilder
import org.gradle.api.Project
import org.gradle.api.reporting.Reporting
import org.gradle.api.reporting.ReportingExtension

import java.text.DateFormat


class OptimusReport {

    private Project project;
    private ReportingExtension reportingExtension;

    OptimusReport(Project project, ReportingExtension reportingExtension) {
        this.project = project;
        this.reportingExtension = reportingExtension;
    }
    def generateReport(Boolean isFragmentation) {
        def jsonReports = project.fileTree(dir: "${reportingExtension.baseDir}/cucumber/").include '**/*.json'.toString()
        File reportOutputDirectory = new File("${reportingExtension.baseDir}/cucumber");

        List<String> jsonReportFiles = new ArrayList<String>();
        jsonReports.each { File file ->
            jsonReportFiles.add("${reportingExtension.baseDir}/cucumber/${file.name}".toString());
        }

        String buildNumber = DateFormat.instance.format(new Date())
        String projectName = "cucumberProject";
        boolean runWithJenkins = false;
        boolean parallelTesting = isFragmentation;

        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
//optional configuration
        configuration.setParallelTesting(parallelTesting);
        configuration.setRunWithJenkins(runWithJenkins);
        configuration.setBuildNumber(buildNumber);

        ReportBuilder reportBuilder = new ReportBuilder(jsonReportFiles, configuration);
        reportBuilder.generateReports();

        println("\nReport available on: ${reportingExtension.baseDir}/cucumber/cucumber-html-reports/overview-features.html")

    }
}
