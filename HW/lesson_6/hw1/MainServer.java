package com.geekbrains.server;

import java.io.IOException;

public class MainServer {
    public static void main(String[] args) {

        try {
            //Class.forName("org.sqlite.JDBC");
            new Server();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
