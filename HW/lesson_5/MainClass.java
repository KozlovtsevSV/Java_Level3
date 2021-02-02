package ru.geekbrains;

import java.util.concurrent.Phaser;

public class MainClass {

    public static final int CARS_COUNT = 4;
    public static Phaser phaser = new Phaser();

    public static void main(String[] args) {

        System.out.println(phaser.getPhase());
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), phaser);
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();

        }

        phaser.awaitAdvance(0); // ждем пока пройдет первый этап подготовка
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        // ждем окончания каждого этапа гонки
        for (int i = 1; i <= race.getCountStage(); i++) {
            phaser.awaitAdvance(i);
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}

//    Все участники должны стартовать одновременно, несмотря на разное время подготовки.
//    В тоннель не может одновременно заехать больше половины участников (условность).
//    Попробуйте все это синхронизировать.
//    Первый участник, пересекший финишную черту, объявляется победителем (в момент пересечения этой самой черты).
//    Победитель должен быть только один (ситуация с 0 или 2+ победителями недопустима).
//    Когда все завершат гонку, нужно выдать объявление об окончании.
//    Можно корректировать классы (в том числе конструктор машин) и добавлять объекты классов из пакета java.util.concurrent.
