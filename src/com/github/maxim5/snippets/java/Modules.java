package com.github.maxim5.snippets.java;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Modules {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<Map> mapClass = Map.class;
        Module module = mapClass.getModule();
        // System.out.println(module.getDescriptor().exports());
        // System.out.println(module.isNamed());
        // System.out.println(module.getName());

        Module module2 = Modules.class.getModule();
        System.out.println(module2);
        // System.out.println(module2.getDescriptor());
        // System.out.println(module2.isNamed());
        // System.out.println(module2.getName());

        Method[] declaredMethods = Module.class.getDeclaredMethods();
        List<Method> list = Arrays.stream(declaredMethods).filter(m -> m.getName().equals("implAddExportsToAllUnnamed")).toList();
        System.out.println(list);
        // module.implAddExportsToAllUnnamed("java.lang.reflect");
        list.get(0).invoke(module, "java.lang.reflect");

        Module module21 = module.addExports("java.lang.reflect", module2);
        module.addOpens("java.lang.reflect", module2);
        // System.out.println(module21);
        // System.out.println(module21.getDescriptor());

        Method privateLookupIn = MethodHandles.class.getDeclaredMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
        MethodHandles.Lookup lookup = (MethodHandles.Lookup) privateLookupIn.invoke(null, AccessibleObject.class, MethodHandles.lookup());
    }
}
