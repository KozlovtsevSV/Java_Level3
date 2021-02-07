package ru.geekbrains;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void metod_1_TEST() {

        int [] res = Main.metod_1(new int[]{1,2,4,6,7});
        Assertions.assertArrayEquals(new int[]{4,6,7}, res);
    }

    @Test
    void metod_1_TEST_RuntimeException() {

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> Main.metod_1(new int[]{1,2,6,7}));

        Assertions.assertEquals("RuntimeException", runtimeException.getMessage());

    }

    @CsvSource(value = {"'1;2;4;6;7', '4;6;7'", "'1;2;4;4;7', '4;7'", "'1;2;5;6;7', ''"})
    @ParameterizedTest
    void metod_1ParameterizedTest(String inArrayStr, String expectedArrayStr) {

        int[] inArray = Arrays.stream(inArrayStr.split(";")).mapToInt(Integer::parseInt).toArray();
        // если длинна строки ожидаемого результата = 0 считаем что нужно ожидать RuntimeException
        if (expectedArrayStr.length() == 0){
            RuntimeException runtimeException = assertThrows(RuntimeException.class,
                    () -> Main.metod_1(inArray));

            Assertions.assertEquals("RuntimeException", runtimeException.getMessage());
            return;
        }
        int[] expectedArray = Arrays.stream(expectedArrayStr.split(";")).mapToInt(Integer::parseInt).toArray();
        int [] res = Main.metod_1(inArray);
        Assertions.assertArrayEquals(expectedArray, res);
    }

    @ParameterizedTest
    @MethodSource("dataMetod_2_TEST")
    void metod_2_TEST(int[]inArray, Boolean expectedResult) {

        Boolean res = Main.metod_2(inArray);
        Assertions.assertEquals(expectedResult, res);

    }

    public static Stream<Arguments> dataMetod_2_TEST(){
        List<Arguments> data  = new ArrayList<>();
        Random rnd = new Random();
        // создадим 10 наборов тестов
        for (int i = 0; i < 100; i++) {
           int[] dataArray = new int[rnd.nextInt(10)];
           Boolean result = false;
            for (int j = 0; j < dataArray.length; j++) {
                dataArray[j] = rnd.nextInt(10);
                if (dataArray[j]==1 || dataArray[j] ==4){
                    result = true;
                }
            }
            data.add(Arguments.arguments(dataArray, result));
        }
        return data.stream();
    }



}