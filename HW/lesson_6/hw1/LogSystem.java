package com.geekbrains.server;

import com.geekbrains.client.Main;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.*;

public class LogSystem {

   // private Logger logger;//
   private static Handler handlerInfo;

    static {
        try {
            handlerInfo = new FileHandler("log_%g.txt",1024 * 100,10, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Handler handlerError;

    static {
        try {
            handlerError = new FileHandler("error_log_%g.txt",1024 * 100,10, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public LogSystem(String className) {
//
//        try {
//            this.logger = Logger.getLogger(className);
//        }catch (NullPointerException e){
//            e.fillInStackTrace();
//        }
//
//        initLog(this.logger);
//
//    }


    public static void initLog(Logger logger){
        handlerError.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);
        logger.addHandler(handlerInfo);
        logger.addHandler(handlerError);
        // для лога информации создадим свой формат
        handlerInfo.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return LocalDateTime.now() + ":" + record.getMessage() + "\n";
            }
        });
        handlerInfo.setFilter(new Filter() {
            @Override
            public boolean isLoggable(LogRecord record) {
                return record.getLevel() == Level.INFO;
            }
        });

    }

}
