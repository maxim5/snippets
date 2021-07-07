package com.github.maxim5.snippets.java;

import com.google.common.base.Stopwatch;
import com.google.common.reflect.ClassPath;

import java.beans.JavaBean;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// Consider also
//   https://github.com/ronmamo/reflections
//      new Reflections("my.package").getTypesAnnotatedWith(MyAnnotation.class)
//   https://github.com/rmuller/infomas-asl
@SuppressWarnings("UnstableApiUsage")
public class Reflect {
    private static final Stopwatch stopwatch = Stopwatch.createUnstarted();
    private static ClassPath classPath;

    public static void main(String[] args) throws Throwable {
        getClassPath();
        getAnnotations();
        getTypesAnnotatedWith(JavaBean.class);
        invokeMethodsAtRuntime();
    }

    private static void getClassPath() throws IOException {
        stopwatch.start();
        classPath = ClassPath.from(ClassLoader.getSystemClassLoader());
        stopwatch.stop();

        System.out.println("Collecting the classpath...");
        System.out.println("Elapsed time: " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        System.out.println("Classpath size: " + classPath.getResources().size());
    }

    private static void getAnnotations() {
        System.out.println();
        System.out.println("Iterating annotations for a class...");
        for (Annotation annotation : MyBean.class.getDeclaredAnnotations()) {
            System.out.println(annotation.toString());
        }
    }

    private static void getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        stopwatch.start();
        Set<? extends Class<?>> classes = classPath.getAllClasses()
                .stream()
                .filter(classInfo -> classInfo.getPackageName().startsWith("com.github"))
                .map((ClassPath.ClassInfo classInfo) -> {
                    try {
                        return classInfo.load();
                    } catch (NoClassDefFoundError e) {
                        System.out.println(classInfo.getPackageName() + " " + classInfo.getName());
                        return null;
                    }
                })
                .filter(load -> load != null && load.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
        stopwatch.stop();

        System.out.println();
        System.out.println("Loading and filtering by annotation...");
        System.out.println("Elapsed time: " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        System.out.println("Matched classes: " + classes);
    }

    private static void invokeMethodsAtRuntime() throws Throwable {
        MyBean instance = new MyBean();

        System.out.println();
        System.out.println("Analyzing class methods...");
        System.out.println(Arrays.toString(MyBean.class.getMethods()));

        Method call = MyBean.class.getMethod("call");
        System.out.println(call + " " + Arrays.toString(call.getParameters()));
        call.invoke(instance);

        // This does not work:
        // MyBean.class.getMethod("get");

        System.out.println();
        System.out.println("Invoking methods at runtime...");
        Method get = Arrays.stream(MyBean.class.getMethods())
                .filter(method -> method.canAccess(instance) && method.getName().equals("get"))
                .findFirst().orElseThrow();
        System.out.println(get + " " + Arrays.toString(get.getParameters()));
        get.setAccessible(true);
        get.invoke(instance, 0);
        get.invoke(instance, 10);

        Method post = Arrays.stream(MyBean.class.getMethods())
                .filter(method -> method.canAccess(instance) && method.getName().equals("post"))
                .findFirst().orElseThrow();
        System.out.println(post + " " + Arrays.toString(post.getParameters()));
        post.setAccessible(true);
        post.invoke(instance, 0, 0, null);
        post.invoke(instance, 10, 42L, "value");

        // Thanks https://dzone.com/articles/think-twice-before-using-reflection for the idea
        // LambdaMetafactory?
        MethodHandle handle = MethodHandles.lookup().unreflect(post);
        handle.invoke(instance, 1, 1, "x");

        handle.bindTo(instance);
        handle.invoke(instance, 2, 2, "y");
    }

    @JavaBean
    private static class MyBean {
        public void call() {
            System.out.println("call()");
        }

        public void get(int param) {
            System.out.println("get(): " + param);
        }

        public void post(int x, long y, String z) {
            System.out.println("post(): " + x + " " + y + " " + z);
        }
    }
}
