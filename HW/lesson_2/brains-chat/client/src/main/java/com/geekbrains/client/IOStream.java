package com.geekbrains.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class IOStream {

    private static String nameFileHistory = "history.txt";
    private static String nameFolder = "client";
    private static int countReturnMsg = 100;

    public static Boolean renameUser(String oldLogin, String newLogin){

        Path oldPath = Paths.get(nameFolder,oldLogin);
        Path newPath = Paths.get(nameFolder,newLogin);
        try {
            Files.move(oldPath, newPath);
        }
        catch (IOException e){
            return false;
        }
        return true;

    }

    public static void saveMsg(String login, String msg){

        Path path = Paths.get(nameFolder,login, nameFileHistory);

        if(!checkPath(path.getParent())){
            if (createDirectory(path.getParent())) {
                writeToFile(path, msg);
            }
        }
        else{
            writeToFile(path, msg);
        }
    }


    public static List<String> loadMsg(String login){

        Path path = Paths.get(nameFolder,login, nameFileHistory);
        List<String> listMsg = readFromFile(path);

        if(countReturnMsg >= listMsg.size()){
            return listMsg;
        }
        else{
            return listMsg.subList(listMsg.size()-countReturnMsg-1, listMsg.size()-1);
        }

    }

    public static Boolean checkPath(Path path){

        return Files.exists(path);
    }

    public static Boolean createDirectory(Path path){

        try {
            Files.createDirectories(path);
        } catch (IOException e){
            return false;
        }
      return true;
    }

    public static void writeToFile(Path path, String msg) {

        try {
            if(Files.exists(path)){
                Files.write(path, List.of(msg), StandardOpenOption.APPEND);
            }
            else{
                Files.write(path, List.of(msg), StandardOpenOption.CREATE);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static List<String> readFromFile(Path path) {

        List<String> result = List.of();
        try {
            result = Files.readAllLines(path);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }


//    1. Добавить в сетевой чат запись локальной истории в текстовый файл на клиенте.
//    2. После загрузки клиента показывать ему последние 100 строк чата.

}
