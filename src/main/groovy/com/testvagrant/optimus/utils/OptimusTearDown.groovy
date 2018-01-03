package com.testvagrant.optimus.utils

import com.testvagrant.monitor.services.BuildsServiceImpl
import com.testvagrant.optimus.ReportMain

class OptimusTearDown {
    static void updateBuildRecord() {
        println "updating build end time"
        new BuildsServiceImpl().notifyBuildEnd();
        println "updating build unique scenario count"
        new BuildsServiceImpl().updateBuildWithUniqueScenarios();
    }

    def static teardown() {
        try {
            ReportMain.main();
        } finally {
//           new MongoMain().closeMongo();
        }
    }
}
