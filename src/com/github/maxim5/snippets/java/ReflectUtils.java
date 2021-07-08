package com.github.maxim5.snippets.java;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

// https://github.com/etaoinshrdlcumwfgypbvkjxqz/Capable-Cables/blob/e760ff9a75a2f8d3ae62980e6eb3b960118b2942/subprojects/utilities/src/main/java/io/github/etaoinshrdlcumwfgypbvkjxqz/capablecables/utilities/dynamic/InvokeUtilities.java
public class ReflectUtils {
    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> makeConsumer(MethodHandles.Lookup lookup, MethodHandle method,
                                               Class<T> inputType) throws Throwable {
        return (Consumer<T>) LambdaMetafactory.metafactory(lookup,
                "accept",
                MethodType.methodType(Consumer.class),
                MethodType.methodType(void.class, Object.class),
                method,
                MethodType.methodType(void.class, inputType))
                .getTarget()
                .invokeExact();
    }

    @SuppressWarnings("unchecked")
    public static <T, U> BiConsumer<T, U> makeBiConsumer(MethodHandles.Lookup lookup, MethodHandle method,
                                                         Class<T> inputType1, Class<U> inputType2) throws Throwable {
        return (BiConsumer<T, U>) LambdaMetafactory.metafactory(lookup,
                "accept",
                MethodType.methodType(BiConsumer.class),
                MethodType.methodType(void.class, Object.class, Object.class),
                method,
                MethodType.methodType(void.class, inputType1, inputType2))
                .getTarget()
                .invokeExact();
    }

    @SuppressWarnings("unchecked")
    public static <T> ObjIntConsumer<T> makeObjIntConsumer(MethodHandles.Lookup lookup, MethodHandle method,
                                                           Class<T> inputType) throws Throwable {
        return (ObjIntConsumer<T>) LambdaMetafactory.metafactory(lookup,
                "accept",
                MethodType.methodType(ObjIntConsumer.class),
                MethodType.methodType(void.class, Object.class, int.class),
                method,
                MethodType.methodType(void.class, inputType, int.class))
                .getTarget()
                .invokeExact();
    }

    @SuppressWarnings({"rawtypes"})
    public static BiConsumer makeBiConsumer(MethodHandles.Lookup lookup, Object bean, Method method) throws Throwable {
        CallSite site = LambdaMetafactory.metafactory(lookup,
                "accept",
                MethodType.methodType(BiConsumer.class),
                MethodType.methodType(void.class, Object.class, Object.class),
                lookup.findVirtual(bean.getClass(), method.getName(), MethodType.methodType(void.class, method.getParameterTypes()[0])),
                MethodType.methodType(void.class, bean.getClass(), method.getParameterTypes()[0]));
        MethodHandle factory = site.getTarget();
        return (BiConsumer) factory.invoke();
    }

    @SuppressWarnings({"unchecked"})
    public static <T> ObjIntConsumer<T> makeObjIntConsumer(MethodHandles.Lookup lookup, T bean, Method method) throws Throwable {
        CallSite site = LambdaMetafactory.metafactory(lookup,
                "accept",
                MethodType.methodType(ObjIntConsumer.class),
                MethodType.methodType(void.class, Object.class, int.class),
                lookup.findVirtual(bean.getClass(), method.getName(), MethodType.methodType(void.class, method.getParameterTypes()[0])),
                MethodType.methodType(void.class, bean.getClass(), method.getParameterTypes()[0]));
        MethodHandle factory = site.getTarget();
        return (ObjIntConsumer<T>) factory.invoke();
    }
}
