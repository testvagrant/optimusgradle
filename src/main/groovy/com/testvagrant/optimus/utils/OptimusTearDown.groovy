package com.testvagrant.optimus.utils

import com.testvagrant.monitor.radiator.MongoWriter

class OptimusTearDown{
    static void updateBuildRecord(){
        new MongoWriter().notifyBuildEnd();
        new MongoWriter().updateBuildWithUniqueScenarios();
    }
}