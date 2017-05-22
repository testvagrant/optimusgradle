package com.testvagrant.optimus.utils

import com.testvagrant.monitor.radiator.MongoWriter
import com.testvagrant.optimus.ReportMain

class OptimusTearDown{
    static void updateBuildRecord(){
        new MongoWriter().notifyBuildEnd();
        new MongoWriter().updateBuildWithUniqueScenarios();
    }

    def static teardown() {
        ReportMain.main();
    }
}
