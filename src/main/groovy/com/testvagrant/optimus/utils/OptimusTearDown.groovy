package com.testvagrant.optimus.utils

import com.testvagrant.monitor.MongoMain
import com.testvagrant.monitor.radiator.MongoWriter
import com.testvagrant.optimus.ReportMain

class OptimusTearDown {
    static void updateBuildRecord() {
        println "updating build end time"
        new MongoWriter().notifyBuildEnd();
        println "updating build unique scenario count"
        new MongoWriter().updateBuildWithUniqueScenarios();
    }

    def static teardown() {
        try {
            ReportMain.main();
        } finally {
            MongoMain.closeMongo();
        }
    }
}
