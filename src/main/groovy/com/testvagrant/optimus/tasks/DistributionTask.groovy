package com.testvagrant.optimus.tasks

import com.testvagrant.optimus.extensions.OptimusExtension
import com.testvagrant.optimus.extensions.OptimusServiceExtension
import com.testvagrant.optimus.utils.FeatureFilter
import com.testvagrant.optimus.utils.OptimusHelper
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
        OptimusServiceExtension serviceExtension = project.getExtensions().findByType(OptimusServiceExtension.class)
        ReportingExtension reportingExtension = project.getExtensions().findByType(ReportingExtension.class)
        OptimusHelper.setupServiceEnvoirment(serviceExtension)
        OptimusHelper.setup(project,optimusExtension,serviceExtension)
        OptimusSetup optimusSetup = new OptimusSetup();
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
        runFunctionalDistribution(optimusExtension, serviceExtension,reportingExtension, udidList, featureFiles)
    }

    def runFunctionalDistribution(OptimusExtension optimusExtension, OptimusServiceExtension serviceExtension,ReportingExtension reportingExtension, List<String> udidList, List<File> allFiles) {
        OptimusServiceExtension optimusServiceExtension = project.getExtensions().findByType(OptimusServiceExtension.class)
        def size = udidList.size()
        println "pool size -- " + size
        def cucumberArgs;
        GParsPool.withPool(size) {
            allFiles.eachParallel { File file ->
                    project.javaexec {
                        main = "cucumber.api.cli.Main"
                        classpath = optimusExtension.classpath
                        args = getArgs(file,optimusExtension,reportingExtension)
                        ignoreExitValue = true
                        systemProperties = [
                                "testFeed"      : optimusExtension.testFeed,
                                "runMode"       : "Distribution",
                                "setupCompleted": "true",
                                "devMode"       : optimusExtension.devMode,
                                "regression"    : optimusExtension.regression,
                                "env"           : optimusExtension.env,
                                "database"      : serviceExtension.database,
                                "uri"           : serviceExtension.uri,
                                "serviceUrl"    : OptimusHelper.getServiceUrl(optimusServiceExtension)
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
