package ru.geekbrains;

public class RunnableLesson4 implements Runnable {
    private int numberCh;
    MyLogic myLogic;

    public RunnableLesson4(MyLogic myLogic, int numberCh) {

        this.myLogic = myLogic;
        this.numberCh = numberCh;

    }

    @Override
    public void run() {

        myLogic.printString(numberCh);

    }
}
