package ru.geekbrains;

import java.util.concurrent.atomic.AtomicInteger;

public class MyLogic {

    private final Object monitor = new Object();
    private static int numberCh = 0;
    private final int COUNT_REPEAT = 10;
    private static String strPrint;

    MyLogic(String str){
        this.strPrint = str;
    }

    public void printString(int numberCh){
        synchronized (monitor) {
            int i = COUNT_REPEAT;
            while(0 < i){
                if(this.numberCh == numberCh) {

                   System.err.print(strPrint.charAt(numberCh));
                    this.numberCh ++;
                    if (this.numberCh >=strPrint.length()){this.numberCh = 0;}
                    i--;
                    monitor.notifyAll();
                    continue;
                }

                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    System.err.print("Error");
                }
            }

        }
    }
}
