package ru.geekbrains;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {

    // для каждого этапа тонель есть своя пропускная способность
    protected Semaphore semaphore;

    public Tunnel() {
        this.length = 80;
        this.description = "Тоннель " + length + " метров и шириной " + MainClass.CARS_COUNT / 2+ " машины";
        this.semaphore = new Semaphore(MainClass.CARS_COUNT / 2, true);
    }

    // свой тунель какой хочешь
    public Tunnel(int width) {
        this.length = 80;
        this.description = "Тоннель " + length + " метров и шириной " + width + " машин";
        this.semaphore = new Semaphore(width, true);
    }

    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                semaphore.acquire();
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                synchronized(this){
                System.out.println(c.getName() + " закончил этап: " + description);
                semaphore.release();}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
