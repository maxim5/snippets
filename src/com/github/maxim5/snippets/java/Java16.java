package com.github.maxim5.snippets.java;

import org.jetbrains.annotations.Contract;

public class Java16 {
    public static void main(String[] args) {
        records();
    }

    private static void records() {
        var foo = new Foo("Name", 25);
        System.out.println(foo);
        System.out.println(foo.describe());
    }
}

record Foo(String name, int age) {
    @Contract(pure = true)
    public String describe() {
        return name + " (" + age + " years)";
    }

    /*
    public void update() {
        age += 1;
    }
    */
}
