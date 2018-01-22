package com.testvagrant.optimus.tasks

import com.testvagrant.optimus.extensions.OptimusExtension
import com.testvagrant.optimus.utils.FeatureFilter
import com.testvagrant.optimus.utils.OptimusSetup
import groovyx.gpars.GParsPool
import org.gradle.api.DefaultTask
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.TaskAction

class DistributionTask extends DefaultTask {
    private Collection<File> featureFiles = new ArrayList<>()

    DistributionTask() {
        outputs.upToDateWhen { false }
    }


    @TaskAction
    def runDistribution() {
        OptimusExtension optimusExtension = project.getExtensions().findByType(OptimusExtension.class)
        ReportingExtension reportingExtension = project.getExtensions().findByType(ReportingExtension.class)
        OptimusSetup optimusSetup = new OptimusSetup()
        optimusSetup.setup(optimusExtension.testFeed)
        def udidList = optimusSetup.getDevicesForThisRun(project, optimusExtension.testFeed)
        List<String> tags = optimusSetup.getTags(optimusExtension.tags)
        FeatureFilter featureFilter = new FeatureFilter(tags)
        List<File> featureFilesList = featureFilter.collectAllFeatureFilesInProject(getProject().getProjectDir().listFiles())
        if (tags.size() > 0) {
            featureFiles = featureFilter.getFilteredFeatures(featureFilesList)
        }
        else {
            featureFiles = featureFilesList
        }
        println "Feature files size "+featureFiles.size()
        featureFiles.forEach({ file -> System.out.println(file.getName()) })
        runFunctionalDistribution(optimusExtension, reportingExtension, udidList, featureFiles)
    }


    def runFunctionalDistribution(OptimusExtension optimusExtension, ReportingExtension reportingExtension, List<String> udidList, List<File> allFiles) {
        def size = udidList.size()
        println "pool size -- " + size
        def cucumberArgs;
        GParsPool.withPool(size) {
            allFiles.eachParallel { File file ->
                project.javaexec {
                    main = "cucumber.api.cli.Main"
                    classpath = optimusExtension.classpath
                    args = getArgs(file,optimusExtension,reportingExtension);
                    systemProperties = [
                            "testFeed"      : optimusExtension.testFeed,
                            "runMode"       : "Distribution",
                            "setupCompleted": "true",
                            "devMode"       : optimusExtension.devMode,
                            "regression"    : optimusExtension.regression,
                            "env"           : optimusExtension.env
                    ]
                }
            }
        }

    }

    static def getArgs(File file, OptimusExtension optimusExtension, ReportingExtension reportingExtension) {
        println optimusExtension.tags
        println reportingExtension.baseDir
        if(optimusExtension.tags!=null)
            return ["-p", "pretty", "-p", ("json:${reportingExtension.baseDir}/cucumber/${file.name}.json"), "--glue", "steps", "--tags", optimusExtension.tags,"--tags","~@intent,~@Intent,~@dataIntent,~@DataIntent",
                    file.toPath()]
        else
            return ["-p", "pretty", "-p", ("json:${reportingExtension.baseDir}/cucumber/${file.name}.json"), "--glue", "steps","--tags","~@intent,~@Intent,~@dataIntent,~@DataIntent", file.toPath()]
    }
}
