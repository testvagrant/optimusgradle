package com.testvagrant.optimus.tasks

import com.testvagrant.optimus.extensions.OptimusExtension
import com.testvagrant.optimus.utils.FeatureFilter
import com.testvagrant.optimus.utils.OptimusReport
import com.testvagrant.optimus.utils.OptimusSetup
import com.testvagrant.optimus.utils.OptimusTearDown
import groovyx.gpars.GParsPool
import org.gradle.api.DefaultTask
import org.gradle.api.reporting.Reporting
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.TaskAction


class DistributionTask extends DefaultTask {
    private Collection<File> featureFiles = new ArrayList<>();

    public DistributionTask() {
        outputs.upToDateWhen {false}
    }


    @TaskAction
    def runDistribution() {
        OptimusExtension optimusExtension = project.getExtensions().findByType(OptimusExtension.class);
        ReportingExtension reportingExtension = project.getExtensions().findByType(ReportingExtension.class);
        OptimusSetup optimusSetup = new OptimusSetup();
        optimusSetup.setup()
        def udidList = optimusSetup.getDevicesForThisRun(project,optimusExtension.testFeed)
        List<String> tags = optimusSetup.getTags(optimusExtension.tags);
        FeatureFilter featureFilter = new FeatureFilter(tags);
        List<File> featureFilesList = featureFilter.collectAllFeatureFilesInProject(getProject().getProjectDir().listFiles());
        featureFiles = featureFilter.getFilteredFeatures(featureFilesList);
        featureFiles.forEach({file -> System.out.println(file.getName())});
        runFunctionalDistribution(optimusExtension,reportingExtension,udidList,featureFiles);
        new OptimusReport(project,reportingExtension).generateReport(false);
        new OptimusTearDown().updateBuildRecord();

    }




    def runFunctionalDistribution(OptimusExtension optimusExtension, ReportingExtension reportingExtension,List<String> udidList, List<File> allFiles) {
        def size = udidList.size()
        println "pool size -- " + size
        GParsPool.withPool(size) {
            try {
                allFiles.eachParallel { File file ->
                    project.javaexec {
                        main = "cucumber.api.cli.Main"
                        classpath = optimusExtension.classpath
                        args = ["-p", "pretty", "-p", ("json:${reportingExtension.baseDir}/cucumber/${file.name}.json"), "--glue", "steps", "--tags", optimusExtension.tags,
                                file.toPath()]
                        systemProperties = [
                                "testFeed"      : optimusExtension.testFeed,
                                "runMode"       : "Distribution",
                                "setupCompleted": "true"
                        ]
                    }
                }
            } catch (Exception e) {
                e.printStackTrace()
            }
        }
    }
}
