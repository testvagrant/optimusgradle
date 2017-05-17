package com.testvagrant.optimus.tasks

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

class FragmentationTaskTest {

    @Test
    public void fragmentationTaskTest() {
        Project project = ProjectBuilder.builder().build();
        project.apply plugin: 'com.testvagrant.optimus.plugin'
        def actionsList = project.tasks.runFragmentation.getActions().execute()
        actionsList.get(0).each {action -> action.execute()};
    }
}
