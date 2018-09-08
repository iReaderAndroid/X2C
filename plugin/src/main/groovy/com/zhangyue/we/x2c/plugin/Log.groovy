package com.zhangyue.we.x2c.plugin;

import org.gradle.api.Project;
import org.gradle.api.logging.LogLevel;

/**
 * @author：chengwei 2018/9/5
 * @description
 */
public class Log {

    private static Project sProject;

    public static void init(Project project) {
        sProject = project
    }

    public static void w(String msg) {
        if (sProject != null) {
            sProject.getLogger().log(LogLevel.ERROR, msg)
        }
    }
}
