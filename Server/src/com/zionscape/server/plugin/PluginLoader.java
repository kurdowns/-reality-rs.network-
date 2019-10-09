package com.zionscape.server.plugin;

import com.google.common.reflect.ClassPath;

public class PluginLoader {

    public static void load() throws Exception {
        int loaded = 0;

        ClassPath classpath = ClassPath.from(ClassLoader.getSystemClassLoader());
        for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClassesRecursive("com.zionscape.server.plugin.impl")) {
            Class clazz = classInfo.load();

            Object o = clazz.newInstance();
            if (o instanceof Plugin) {
                ((Plugin)o).onEnable();
                loaded++;
            }
        }

        System.out.println(loaded + " plugins loaded...");
    }

}
