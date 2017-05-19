package com.testvagrant.optimus.utils

import com.testvagrant.monitor.radiator.MongoWriter;
import com.testvagrant.optimus.helpers.MongoDBHelper

class OptimusTearDown{
    static void updateBuildRecord(){
        new MongoWriter().notifyBuildEnd();
        new MongoWriter().updateBuildWithUniqueScenarios();
    }
}