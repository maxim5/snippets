package com.github.maxim5.snippets.java;

/*import jdk.incubator.vector.FloatVector;*/

public class VectorApi {
    public static void main(String[] args) {
        vector();
    }

    private static void vector() {
        float[] a = new float[] {1, 2, 3, 4};
        float[] b = new float[] {5, 8, 10, 12};
/*
        FloatVector first = FloatVector.fromArray(FloatVector.SPECIES_128, a, 0);
        FloatVector second = FloatVector.fromArray(FloatVector.SPECIES_128, b, 0);
        FloatVector result = first
                .add(second)
                .pow(2)
                .neg();
        System.out.println(result);
*/
    }
}
