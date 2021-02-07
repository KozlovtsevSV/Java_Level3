package ru.geekbrains;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        int[] arr = new int[] {1, 4, 3, 5, 6};
        printArray(metod_1(arr));

    }

    public static int[] metod_1(int[]array){

        for (int i = array.length - 1; i >= 0; i--) {
            if(array[i]==4){
                return Arrays.copyOfRange(array, i, array.length);
            }
        }
        throw new RuntimeException("RuntimeException");
    }

    public static Boolean metod_2(int[]array){

        for (int i = 0; i < array.length; i++) {
            if(array[i]==1 || array[i]==4 ){
                return true;
            }
        }
        return false;
    }

    public static void printArray(int[] array){
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + ", ");

        }
    }
}


//2. Написать метод, которому в качестве аргумента передается не пустой одномерный целочисленный массив.
//        Метод должен вернуть новый массив, который получен путем вытаскивания из исходного массива элементов, идущих после последней четверки.
//        Входной массив должен содержать хотя бы одну четверку, иначе в методе необходимо выбросить RuntimeException.
//        Написать набор тестов для этого метода (по 3-4 варианта входных данных). Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].
//
//3. Написать метод, который проверяет состав массива из чисел 1 и 4. Если в нем нет хоть одной четверки или единицы, то метод вернет false;
//Написать набор тестов для этого метода (по 3-4 варианта входных данных).