package com.geekbrains.server;

import com.geekbrains.client.Main;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.*;

public class Server {
    private Logger logger = Logger.getLogger(getClass().getName());
    private Vector<ClientHandler> clients;
    private AuthService authService;
    private Boolean serverStop;



    public AuthService getAuthService() {
        return authService;
    }

    public Server() throws IOException {
        LogSystem.initLog(logger);
        clients = new Vector<>();
        authService = new SimpleAuthService();
        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
//        ExecutorService service = Executors.newCachedThreadPool();8189
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            SqlClient.connect();
            // ставим таймаут для того чтобы сервер можнобыло остановить
            serverSocket.setSoTimeout(1000);
            serverStop = false;
           // System.out.println("Сервер запущен на порту 8189");
            logger.log(Level.INFO,"Сервер запущен на порту 8189");

            while (!serverStop) {
                serverStop = stopServer();
                try {
                    Socket socket = serverSocket.accept();
                    poolExecutor.submit(()->{
                        new ClientHandler(this, socket);
                    });
                     //System.out.println("Подключился новый клиент");
                    logger.log(Level.INFO,"Подключился новый клиент\n");
                }catch (SocketTimeoutException e){
                    //System.out.println("Server works count "+ poolExecutor.getPoolSize());
                    // это конечно эже не ошибка которую стоит писать в лог, но сделаю так посмотрим как это работает
                    logger.log(Level.WARNING,"ERROR:Server works count, active clients: " + poolExecutor.getPoolSize()+ "\n");
                }

            }
            SqlClient.disconnect();
        } catch (IOException e) {
            //e.printStackTrace();
            logger.log(Level.WARNING,"Не удалось запустить сервер: " + e.getLocalizedMessage());
        }
        poolExecutor.shutdownNow();
        //service.shutdownNow();

        // еще бы при штатной остановке сервера оповестить клиентов.....
        //System.out.println("Сервер завершил свою работу");
        logger.log(Level.INFO,"Сервер завершил свою работу\n");
    }

    public static Boolean stopServer() {
        // не поняьно как тут по нормальному выключить сервер добавил флаг
        Path path = Paths.get("flagStopServer.txt");
        return Files.exists(path);
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
