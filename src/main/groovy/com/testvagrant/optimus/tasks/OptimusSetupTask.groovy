package com.testvagrant.optimus.tasks

import com.testvagrant.monitor.MongoMain
import com.testvagrant.optimus.register.DeviceRegistrar
import com.testvagrant.optimus.utils.DeviceMatrix
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


class OptimusSetupTask extends DefaultTask {

    @TaskAction
    def optimusSetup() {
            MongoMain.main()
            new DeviceRegistrar().setUpDevices(new DeviceMatrix());
    }
}
