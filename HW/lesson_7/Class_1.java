package ru.geekbrains;

public class Class_1 {


    @AfterSuite
    public static void afterMethod(){
        System.out.println("Class_1.afterMethod");
    }

    @TestAnnotation(priority = 2)
    private static void method_1(){
        System.out.println("Class_1.method_1");
    }

    @TestAnnotation(priority = 1)
    public static void method_2(){
        System.out.println("Class_1.method_2");
    }



}
