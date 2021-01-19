package ru.geekbrains;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        // #1
        System.out.println("============ ЗАДАНИЕ №1 ============");
        Integer[] test_ArrayInt = new Integer[]{1,5,6,8,10};

        test_ArrayInt = getArray(test_ArrayInt,0,4);

        printArray(test_ArrayInt);

        String[] test_ArrayStr = new String[]{"Каждый","Охотник","Хочет","Знать","Где","Сидит","Фазан"};

        test_ArrayStr = getArray(test_ArrayStr,0,4);

        printArray(test_ArrayStr);

        //#2
        System.out.println("============ ЗАДАНИЕ №2 ============");
        System.out.println(getArrayList(test_ArrayInt));
        System.out.println(getArrayList(test_ArrayStr));

        //#3

        System.out.println("============ ЗАДАНИЕ №3 ============");

        class Box<T> {

            private ArrayList<T> items = new ArrayList<>();
            private float maxWeigh = 12f;

            Box(float maxWeigh){
                this.maxWeigh = maxWeigh;

            }

            public Boolean addItemBox(T item){
               if(this.getWeightBox()+ getWeightItem((Fruit) item) <= maxWeigh){
                    items.add(item);
                    return true;
                }
                return false;
            }

            public void moveFruitBox(Box<T> boxReceiver){
                do {
                    if(boxReceiver.addItemBox(items.get(items.size()-1))){
                        deleteItemBox();
                    }
                    else return;
                }
                while (items.size() > 0);
            }

            public Boolean deleteItemBox(){

                if(items.size() > 0){
                    items.remove(items.size() - 1);
                    return true;
                }
                return false;
            }

            public Boolean compare(Box box){

                return Math.abs(this.getWeightBox() - box.getWeightBox())< 0.001f;
            }

            public float getWeightBox(){

                float result = 0;

                for (int i = 0; i < items.size(); i++) {
                    result += getWeightItem((Fruit) items.get(i));
                }
                return result;
            }

            public float getWeightItem(Fruit item){

                float result = item.weightFruit();

                return result;
            }

        }

        Box<Orange> box1 = new Box<>(12f);

        for (int i = 0; i < 10; i++) {
            if(! box1.addItemBox(new Orange())){
                System.out.println("Коробка полна не возможно добавить фрукт");
            }
        }

        Box<Apple> box2 = new Box<>(12f);

        for (int i = 0; i < 12; i++) {
            if(! box2.addItemBox(new Apple())){
                System.out.println("Коробка полна не возможно добавить фрукт");
            }
        }

        System.out.println("Вес в коробках равен? " + box1.compare(box2));

        Box<Orange> box3 = new Box<>(12f);

        for (int i = 0; i < 2; i++) {
            if(! box3.addItemBox(new Orange())){
                System.out.println("Коробка полна не возможно добавить фрукт");
            }
        }

        System.out.println("Вес в коробке №1 до премещения " + box1.getWeightBox());
        System.out.println("Вес в коробке №2 до премещения " + box3.getWeightBox());

        box1.moveFruitBox(box1);

        System.out.println("Вес в коробке №1 после премещения " + box1.getWeightBox());
        System.out.println("Вес в коробке №2 после премещения " + box3.getWeightBox());

    }

    public static <T> T[] getArray(T[] inputArray, int indexFrom, int indexTo){
        if(inputArray.length == 0 || indexFrom < 0 ||indexTo < 0 || indexFrom > inputArray.length - 1 ||indexTo > inputArray.length - 1){
            return inputArray;
        }

        T temp = inputArray[indexTo];
        T[] result = inputArray;
        result[indexTo] = result[indexFrom];
        result[indexFrom] = temp;
        return result;
    }

    public static ArrayList getArrayList(Object[] inputArray){

        ArrayList result = new ArrayList();

        for (int i = 0; i < inputArray.length; i++) {
            result.add(inputArray[i]);
        }

        return result;
    }

    public static void printArray(Object[] inputArray){

        for (int i = 0; i < inputArray.length; i++) {
            System.out.println(inputArray[i]);
        }

    }

}


//        1. Написать метод, который меняет два элемента массива местами (массив может быть любого ссылочного типа);
//        2. Написать метод, который преобразует массив в ArrayList;
//        3. Большая задача:
//        Есть классы Fruit -> Apple, Orange (больше фруктов не надо);
//        Класс Box, в который можно складывать фрукты. Коробки условно сортируются по типу фрукта, поэтому в одну коробку нельзя сложить и яблоки, и апельсины;
//        Для хранения фруктов внутри коробки можно использовать ArrayList;
//        Сделать метод getWeight(), который высчитывает вес коробки, зная количество фруктов и вес одного фрукта (вес яблока – 1.0f, апельсина – 1.5f. Не важно, в каких это единицах);
//        Внутри класса Коробка сделать метод compare, который позволяет сравнить текущую коробку с той, которую подадут в compare в качестве параметра, true – если она равны по весу, false – в противном случае (коробки с яблоками мы можем сравнивать с коробками с апельсинами);
//        Написать метод, который позволяет пересыпать фрукты из текущей коробки в другую (помним про сортировку фруктов: нельзя яблоки высыпать в коробку с апельсинами). Соответственно, в текущей коробке фруктов не остается, а в другую перекидываются объекты, которые были в этой коробке;
//        Не забываем про метод добавления фрукта в коробку.