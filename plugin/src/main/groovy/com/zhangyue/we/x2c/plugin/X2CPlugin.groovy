package com.zhangyue.we.x2c.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.FeaturePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState;

/**
 * @author ： c h e n g w e i 2018/9/5
 * @description
 */
public class X2CPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        Log.init(project)
        project.plugins.all {
            Log.w(it.toString())
        }

        project.gradle.taskGraph.addTaskExecutionListener(new TaskExecutionListener() {
            @Override
            void beforeExecute(Task task) {
                Log.w(task.name)
                task.inputs.files.each {
//                    Log.w("in  ${it.absolutePath}")
                }

                task.outputs.files.each {
//                    Log.w("out ${it.absolutePath}")
                }
            }

            @Override
            void afterExecute(Task task, TaskState taskState) {

            }
        })
    }
}
