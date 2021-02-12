package ru.geekbrains;

import java.lang.reflect.*;
import java.util.Map;
import java.util.TreeMap;

public class RunTest {

    public static void start(Class testClass){

        // не придумал что то хорошее с сортировкой методов
        Map<String, Method> testMethodsMap = new TreeMap<String, Method>();

        int BeforeSuiteCount = 0;
        int AfterSuiteCount = 0;
        Method beforeSuiteCountMethod = null;
        Method afterSuiteCountMethod = null;

       Method[] testMethods = testClass.getDeclaredMethods();

        for (Method testMethod : testMethods) {
            if(testMethod.getAnnotation(TestAnnotation.class) != null) {
                testMethodsMap.put(String.valueOf(testMethod.getAnnotation(TestAnnotation.class).priority()) +testMethod.getName(), testMethod);
            }else if(testMethod.getAnnotation(BeforeSuite.class) != null) {
                BeforeSuiteCount ++;
                if(BeforeSuiteCount > testMethod.getAnnotation(BeforeSuite.class).maxCount()) {
                    throw new RuntimeException("Annotation 'BeforeSuite' more than "+ testMethod.getAnnotation(BeforeSuite.class).maxCount());
                }
                beforeSuiteCountMethod = testMethod;
            }else if(testMethod.getAnnotation(AfterSuite.class) != null) {
                AfterSuiteCount++;
                if(AfterSuiteCount > testMethod.getAnnotation(AfterSuite.class).maxCount()) {
                    throw new RuntimeException("Annotation 'AfterSuite' more than " + testMethod.getAnnotation(AfterSuite.class).maxCount());
                }
                afterSuiteCountMethod = testMethod;
            }
        }

        if (beforeSuiteCountMethod != null){
            invokeMethod(beforeSuiteCountMethod, testClass);
        }

        for (Method testMethodMap : testMethodsMap.values()) {
            invokeMethod(testMethodMap, testClass);
        }

        if (afterSuiteCountMethod != null){
            invokeMethod(afterSuiteCountMethod, testClass);
        }




    }

    private static void invokeMethod(Method method, Class testClass){
        try {
            method.setAccessible(true);
            method.invoke(testClass);
            method.setAccessible(false);
        }catch (ReflectiveOperationException e){
            e.printStackTrace();
        }

    }
}
