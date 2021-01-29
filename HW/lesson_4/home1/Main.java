package ru.geekbrains;

public class Main {

    private static String strPrint = "ABCDF";

    public static void main(String[] args)  {

        MyLogic myLogic = new MyLogic(strPrint);

        Thread[] arrayThread = new Thread[strPrint.length()];

        for (int i = 0; i < strPrint.length(); i++) {
            arrayThread[i] = new Thread(new RunnableLesson4(myLogic, i));
            arrayThread[i].start();

        }
        // ждем завершение только последнего потока т.к. он полюбому последним отработает
        try {
                arrayThread[arrayThread.length-1].join();
        }
            catch (InterruptedException e) {
        }
    }

}

//        1. Создать три потока, каждый из которых выводит определенную букву (A, B и C) 5 раз (порядок – ABСABСABС). Используйте wait/notify/notifyAll.
//        2. На серверной стороне сетевого чата реализовать управление потоками через ExecutorService.
