package com.example.projekatop2;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        try{
            ServerSocket ss = new ServerSocket(9000);
            ServerSocket ss1 = new ServerSocket(12345);
            System.out.println("Server pokrenut...");
            while(true){
                Socket sock = ss.accept();
                Socket sockPom = ss1.accept();
                ServerThread s1 = new ServerThread(sock, sockPom);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }
}
