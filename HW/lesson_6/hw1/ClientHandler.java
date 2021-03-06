package com.geekbrains.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler {
    private Logger logger = Logger.getLogger(getClass().getName());
    private String nickname;
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public String getNickname() {
        return nickname;
    }

    public ClientHandler(Server server, Socket socket) {
        LogSystem.initLog(logger);
        try {
            this.server = server;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            //new Thread(() -> {
                try {
                    while (true) {
                        String msg = in.readUTF();
                        // /auth login1 pass1
                        if (msg.startsWith("/auth ")) {
                            String[] tokens = msg.split("\\s");
                            String nick = server.getAuthService().getNicknameByLoginAndPassword(tokens[1], tokens[2]);
                            if (nick != null && !server.isNickBusy(nick)) {
                                sendMsg("/authok " + nick);
                                nickname = nick;
                                server.subscribe(this);
                                logger.log(Level.INFO,getNickname() + " Клиент авторизовался");
                                //server.restoreMessages(this, tokens[1], tokens[2]);
                                break;
                            }
                        }
                     }

                    while (true) {
                        String msg;
                        try {
                            msg = in.readUTF();
                        }catch (UTFDataFormatException e){
                            logger.log(Level.INFO,"Клиент отключился");
                            //System.out.println("Клиент отключился");
                            break;
                        }
                        if (msg.startsWith("/renameUser ")) {
                            String[] tokens = msg.split("\\s");
                            if (server.getAuthService().setNicknameByLoginAndPassword(tokens[1], tokens[2], tokens[3])){
                                nickname  = tokens[3];
                                // отправляем клиенту что Логин изменен
                                server.sendLoginRename(this, nickname);
                                // оповещаем всех о смене Логина у пользователя
                                server.sendClientList();
                                logger.log(Level.INFO,getNickname() + " Клиент сменил Логин");
                            }
                        }
                        if(msg.startsWith("/")) {
                            if (msg.equals("/end")) {
                                sendMsg("/end");
                                break;
                            }
                            if(msg.startsWith("/w ")) {
                                String[] tokens = msg.split("\\s", 3);
                                server.privateMsg(this, tokens[1], tokens[2],false);
                                logger.log(Level.INFO,getNickname() + " Клиент отправил приватное сообщение");
                            }
                        } else {
                            server.broadcastMsg(this, nickname + ": " + msg);
                            logger.log(Level.INFO,getNickname() + " Клиент отправил сообщение всем");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.log(Level.WARNING,getNickname() + e.getMessage());
                } finally {
                    ClientHandler.this.disconnect();
                }
           // }).start();
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING,getNickname() + e.getMessage());
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING,getNickname() + e.getMessage());
        }
    }

    public void disconnect() {
        server.unsubscribe(this);
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING,getNickname() + e.getMessage());
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING,getNickname() + e.getMessage());
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING,getNickname() + e.getMessage());
        }
    }
}
