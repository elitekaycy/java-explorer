package com.elitekaycy.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.elitekaycy.annotations.Entity;
import com.elitekaycy.interfaces.EntityScanner;

public class HibernateEntityScanner implements EntityScanner {
    private final String packageName;

    public HibernateEntityScanner(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public Set<Class<?>> scanEntities() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace(".", "/");

            Enumeration<URL> resources = classLoader.getResources(path);
            List<File> directories = new ArrayList<>();

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                directories.add(new File(resource.getFile()));
            }

            Set<Class<?>> entityClasses = new HashSet<>();
            for (File directory : directories) {
                entityClasses.addAll(findEntityClasses(directory, packageName));
            }
            return entityClasses;
        }

        catch (IOException ignored) {
            return Collections.emptySet();
        }

    }

    private Set<Class<?>> findEntityClasses(File directory, String packageName) {
        HashSet<Class<?>> entityClasses = new HashSet<>();

        if (!directory.exists())
            return entityClasses;

        File[] files = directory.listFiles();
        if (files == null) {
            return entityClasses;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                entityClasses.addAll(findEntityClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(Entity.class) && !Modifier.isAbstract(clazz.getModifiers())) {
                        entityClasses.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Failed to load class: " + className, e);
                }
            }
        }

        return entityClasses;
    }

}
