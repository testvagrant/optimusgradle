package com.testvagrant.optimus.utils;

import com.testvagrant.optimus.helpers.MongoDBHelper

class OptimusTearDown{
    public void updateBuildRecord(){
        new MongoDBHelper().notifyBuildComplete()
    }
}