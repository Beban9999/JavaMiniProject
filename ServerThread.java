package com.example.projekatop2;

import java.io.*;
import java.net.Socket;
import java.util.Locale;

public class ServerThread extends Thread{
    Socket sock;
    BufferedReader in;
    PrintWriter out;
    Socket sockPom;
    public ServerThread(Socket sock, Socket sockPom){
        this.sock = sock;
        this.sockPom = sockPom;
        try{
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())),true);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        start();
    }

    public void run() {
        try{
            String req = in.readLine();
            System.out.println("Server primio zahtev: " + req);

            String[] zahtev = req.split("#");
            String odg = "";

            System.out.println(zahtev[0]);
            System.out.println(zahtev[1]);

            if(zahtev[0].equals("PALINDROM")){
                String original = zahtev[1].replaceAll("\\s", "");
                original = original.toLowerCase();
                StringBuilder reverse = new StringBuilder(original);
                reverse.reverse();
                System.out.println(original);
                System.out.println(reverse);

                if(original.equals(reverse.toString())){
                    odg = zahtev[1] + " jeste palindrom";
                }
                else{
                    odg = zahtev[1] + " nije palindrom";
                }

                out.println(odg);



            }
            else{

               String fileName = zahtev[0];
               FileOutputStream fos = new FileOutputStream("DATA_SERVER\\" + fileName);

                byte[] contents = new byte[10000];
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                InputStream is = sockPom.getInputStream();


                odg = "Fajl "+ fileName+" uspesno poslat";
                out.println(odg);

                int bytesRead = 0;
                while((bytesRead=is.read(contents))!=-1){
                    bos.write(contents, 0, bytesRead);
                }
                bos.flush();
                bos.close();
                is.close();

            }



            sockPom.close();
            in.close();
            out.close();
            sock.close();

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

}
