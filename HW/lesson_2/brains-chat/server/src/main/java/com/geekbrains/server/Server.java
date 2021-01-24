package com.geekbrains.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Server {
    private Vector<ClientHandler> clients;
    private AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    public Server() {
        clients = new Vector<>();
        authService = new SimpleAuthService();
        try (ServerSocket serverSocket = new ServerSocket(8189)) {

            SqlClient.connect();
            //Connection connection = DriverManager.getConnection("jdbc:sqlite:BD.db");
            System.out.println("Сервер запущен на порту 8189");
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(this, socket);
                System.out.println("Подключился новый клиент");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlClient.disconnect();
        System.out.println("Сервер завершил свою работу");
    }

    public void broadcastMsg(ClientHandler sender, String msg) {
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
        SqlClient.insertMsg(sender.getNickname(),"", msg);
    }

    public void privateMsg(ClientHandler sender, String receiverNick, String msg, Boolean restore) {
        if (restore){
            sender.sendMsg(msg);
            return;
        }

        if (sender.getNickname().equals(receiverNick)) {
            sender.sendMsg("заметка для себя: " + msg);
            SqlClient.insertMsg(sender.getNickname(), receiverNick, "заметка для себя: " + msg);
            return;
        }
        for (ClientHandler o : clients) {
            if (o.getNickname().equals(receiverNick) || restore) {
                o.sendMsg("от " + sender.getNickname() + ": " + msg);
                sender.sendMsg("для " + receiverNick + ": " + msg);
                SqlClient.insertMsg(sender.getNickname(), receiverNick, "от " + sender.getNickname() + ": " + msg);
                 return;
            }
        }
            sender.sendMsg("Клиент " + receiverNick + " не найден");
    }

    public void sendLoginRename(ClientHandler sender, String msg) {
        sender.sendMsg("/renameUser " + msg);
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientsList();

    }

    public void restoreMessages(ClientHandler clientHandler, String login, String password) {
        ArrayList<HashMap> messages = SqlClient.getMsg(login, password, 10);
        for (int i = 0; i < messages.size(); i++) {
            privateMsg(clientHandler, messages.get(i).get("FromUser").toString(),  messages.get(i).get("Msg").toString(),true);
        }
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientsList();
    }

    public void sendClientList() {
        broadcastClientsList();
    }

    public boolean isNickBusy(String nickname) {
        for (ClientHandler o : clients) {
            if (o.getNickname().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public void broadcastClientsList() {
        StringBuilder sb = new StringBuilder(15 * clients.size());
        sb.append("/clients ");
        // '/clients '
        for (ClientHandler o : clients) {
            sb.append(o.getNickname()).append(" ");
        }
        // '/clients nick1 nick2 nick3 '
        sb.setLength(sb.length() - 1);
        // '/clients nick1 nick2 nick3'
        String out = sb.toString();
        for (ClientHandler o : clients) {
            o.sendMsg(out);
        }
    }
}
