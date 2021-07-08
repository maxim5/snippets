package com.github.maxim5.snippets.java;

import org.openjdk.jmh.annotations.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.ObjIntConsumer;

@Fork(1)
@BenchmarkMode({Mode.Throughput /*, Mode.AverageTime*/})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 8, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class ReflectBenchmark {
    @State(Scope.Benchmark)
    public static class ExecutionPlan {
        @Param({ "10", "100" })
        private int iterations = 0;

        private Action instance;
        private final Object object = new int[0];
        private final String s = "";
        private final int value = 10;

        private Method intArg;
        private MethodHandle intArgHandle;
        private BiConsumer<Action, Integer> intArgBiConsumer;
        private ObjIntConsumer<Action> intArgObjIntConsumer;

        private Method withArgs;
        private MethodHandle withArgsHandle;

        @Setup(Level.Invocation)
        public void setUp() throws Throwable {
            MethodHandles.Lookup lookup = MethodHandles.lookup();

            instance = new Action();

            intArg = getMethod("intArg");
            intArgHandle = lookup.unreflect(intArg).bindTo(instance);
            intArgBiConsumer = ReflectUtils.makeBiConsumer(lookup, lookup.unreflect(intArg), Action.class, Integer.class);
            intArgObjIntConsumer = ReflectUtils.makeObjIntConsumer(lookup, lookup.unreflect(intArg), Action.class);

            withArgs = getMethod("withArgs");
            withArgsHandle = lookup.unreflect(withArgs).bindTo(instance);
        }

        private Method getMethod(String name) {
            return Arrays.stream(Action.class.getMethods())
                    .filter(method -> method.canAccess(instance) && method.getName().equals(name))
                    .findFirst()
                    .orElseThrow();
        }
    }

    @Benchmark
    public void int_arg_native_call(ExecutionPlan plan) throws Throwable {
        for (int i = 0; i < plan.iterations; i++) {
            plan.instance.intArg(plan.value);
        }
    }

    @Benchmark
    public void int_arg_reflect_method_invoke(ExecutionPlan plan) throws Throwable {
        for (int i = 0; i < plan.iterations; i++) {
            plan.intArg.invoke(plan.instance, plan.value);
        }
    }

    @Benchmark
    public void int_arg_reflect_method_handle_invoke_exact(ExecutionPlan plan) throws Throwable {
        for (int i = 0; i < plan.iterations; i++) {
            plan.intArgHandle.invokeExact(plan.value);
        }
    }

    @Benchmark
    public void int_arg_reflect_bi_consumer_call(ExecutionPlan plan) throws Throwable {
        for (int i = 0; i < plan.iterations; i++) {
            plan.intArgBiConsumer.accept(plan.instance, plan.value);
        }
    }

    @Benchmark
    public void int_arg_reflect_obj_int_consumer_call(ExecutionPlan plan) throws Throwable {
        for (int i = 0; i < plan.iterations; i++) {
            plan.intArgObjIntConsumer.accept(plan.instance, plan.value);
        }
    }

    @Benchmark
    public void with_args_native_call(ExecutionPlan plan) throws Throwable {
        for (int i = 0; i < plan.iterations; i++) {
            plan.instance.withArgs(plan.object, plan.s, plan.value);
        }
    }

    @Benchmark
    public void with_args_reflect_method_invoke(ExecutionPlan plan) throws Throwable {
        for (int i = 0; i < plan.iterations; i++) {
            plan.withArgs.invoke(plan.instance, plan.object, plan.s, plan.value);
        }
    }

    @Benchmark
    public void with_args_reflect_method_handle_invoke_exact(ExecutionPlan plan) throws Throwable {
        for (int i = 0; i < plan.iterations; i++) {
            plan.withArgsHandle.invokeExact(plan.object, plan.s, plan.value);
        }
    }

    private static class Action {
        private int state;

        public void noArgs() {
            state++;
        }

        public void intArg(int value) {
            state += value;
        }

        public void withArgs(Object obj, String s, int value) {
            state += obj.hashCode() + s.hashCode() + value;
        }
    }
}
