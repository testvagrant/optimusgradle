package com.testvagrant.optimus.utils

import com.testvagrant.devicemanagement.core.MongoMain
import com.testvagrant.devicemanagement.io.MongoWriter
import com.testvagrant.optimus.ReportMain

class OptimusTearDown{
    static void updateBuildRecord(){
        new MongoWriter().notifyBuildEnd();
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
