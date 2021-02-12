package ru.geekbrains;

public class Class_2 {



    @TestAnnotation(priority = 2)
    private static void method_1(){
        System.out.println("Class_2.method_1");
    }

    @TestAnnotation(priority = 10)
    public static void method_2(){
        System.out.println("Class_2.method_2");
    }

    @TestAnnotation(priority = 1)
    public static void method_3(){
        System.out.println("Class_2.method_3");
    }

    @BeforeSuite
    public static void beforeMethod(){
        System.out.println("Class_2.beforeMethod");
    }

    @AfterSuite
    public static void afterMethod(){
        System.out.println("Class_2.afterMethod");
    }

}
