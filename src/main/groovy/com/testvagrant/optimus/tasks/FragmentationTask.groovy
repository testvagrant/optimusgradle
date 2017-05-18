package com.testvagrant.optimus.tasks

import com.testvagrant.optimus.extensions.OptimusExtension
import com.testvagrant.optimus.utils.OptimusReport
import com.testvagrant.optimus.utils.OptimusSetup
import cucumber.api.cli.Main
import groovyx.gpars.GParsPool
import net.masterthought.cucumber.Configuration
import net.masterthought.cucumber.ReportBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.internal.tasks.SourceSetCompileClasspath
import org.gradle.api.reporting.Reporting
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction

import java.text.DateFormat


class FragmentationTask extends DefaultTask {

    FragmentationTask() {
        outputs.upToDateWhen {false};
    }

    @TaskAction
    def runFragmentation() {
        OptimusExtension optimusExtension = project.getExtensions().findByType(OptimusExtension.class);
        ReportingExtension reportingExtension = project.getExtensions().findByType(ReportingExtension.class);
        OptimusSetup optimusSetup = new OptimusSetup();
        optimusSetup.setup()
        def run = optimusSetup.getDevicesForThisRun(project,optimusExtension.testFeed)
        runDeviceFragmentation(run,optimusExtension,reportingExtension);
        new OptimusReport(project,reportingExtension).generateReport(true);
    }

    def runDeviceFragmentation(List<String> udidList,OptimusExtension extension,ReportingExtension reportingExtension) {
        def size = udidList.size()
        println "Total devices -- " + size
        GParsPool.withPool(size) {
            try {
                udidList.eachParallel { String udid ->
                    project.javaexec {
                        main = "cucumber.api.cli.Main"
                        classpath = extension.classpath
                        args = ["-p", "pretty", "-p", ("json:${reportingExtension.baseDir}/cucumber/" + updateReportFileName(udid) + ".json"), "--glue", "steps", "-t", extension.tags,
                                "${project.projectDir}/src/test/resources/features"]
                        systemProperties = [
                                "testFeed"      : extension.testFeed,
                                "udid"          : udid,
                                "runMode"       : "Fragmentation",
                                "setupCompleted": "true"
                        ]
                    }
                }
            } catch (Exception e) {
                e.printStackTrace()
            }
        }

    }

    def generateReport(Boolean isFragmentation) {
        def jsonReports = project.fileTree(dir: "${Reporting.baseDir}/cucumber/").include '**/*.json'.toString()
        File reportOutputDirectory = new File("${Reporting.baseDir}/cucumber");

        List<String> jsonReportFiles = new ArrayList<String>();
        jsonReports.each { File file ->
            jsonReportFiles.add("${Reporting.baseDir}/cucumber/${file.name}".toString());
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

        println("\nReport available on: ${Reporting.baseDir}/cucumber/cucumber-html-reports/overview-features.html")

    }


    def String updateReportFileName(String name) {
        String[] deviceIdString = name.split(":");
        return deviceIdString.length > 1 ? "emulator_" + deviceIdString[0].substring(deviceIdString[0].lastIndexOf(".") + 1) : name;
    }
}
