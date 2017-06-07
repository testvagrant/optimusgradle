package com.testvagrant.optimus.tasks

import com.testvagrant.optimus.extensions.OptimusExtension
import com.testvagrant.optimus.utils.OptimusReport
import com.testvagrant.optimus.utils.OptimusSetup
import com.testvagrant.optimus.utils.OptimusTearDown
import groovyx.gpars.GParsPool
import net.masterthought.cucumber.Configuration
import net.masterthought.cucumber.ReportBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.reporting.Reporting
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.TaskAction

import java.text.DateFormat

class FragmentationTask extends DefaultTask {

    FragmentationTask() {
        outputs.upToDateWhen { false };
    }

    @TaskAction
    def runFragmentation() {
        OptimusExtension optimusExtension = project.getExtensions().findByType(OptimusExtension.class);
        ReportingExtension reportingExtension = project.getExtensions().findByType(ReportingExtension.class);
        OptimusSetup optimusSetup = new OptimusSetup();
        optimusSetup.setup(optimusExtension.testFeed)
        def run = optimusSetup.getDevicesForThisRun(project, optimusExtension.testFeed)
        runDeviceFragmentation(run, optimusExtension,reportingExtension);
    }

    def runDeviceFragmentation(List<String> udidList, OptimusExtension extension, ReportingExtension reportingExtension) {
        def size = udidList.size()
        println "Total devices -- " + size
        GParsPool.withPool(size) {
                udidList.eachParallel { String udid ->
                    project.javaexec {
                        main = "cucumber.api.cli.Main"
                        classpath = extension.classpath
                        args = getArgs(udid,extension,reportingExtension)

                        systemProperties = [
                                "testFeed"      : extension.testFeed,
                                "udid"          : udid,
                                "runMode"       : "Fragmentation",
                                "setupCompleted": "true"
                        ]
                    }
                }
        }

    }


    static def String updateReportFileName(String name) {
        String[] deviceIdString = name.split(":");
        return deviceIdString.length > 1 ? "emulator_" + deviceIdString[0].substring(deviceIdString[0].lastIndexOf(".") + 1) : name;
    }

    def getArgs(String udid, OptimusExtension extension, ReportingExtension reportingExtension) {
        if(extension.tags!=null) {
                return ["-p", "pretty", "-p", ("json:${reportingExtension.baseDir}/cucumber/" + updateReportFileName(udid) + ".json"), "--glue", "steps", "-t", extension.tags,
                                "${project.projectDir}/src/test/resources/features"];
        } else {
            return ["-p", "pretty", "-p", ("json:${reportingExtension.baseDir}/cucumber/" + updateReportFileName(udid) + ".json"), "--glue", "steps",
                                "${project.projectDir}/src/test/resources/features"];
        }
    }
}
