package com.example.projekatop2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class HelloApplication extends Application {

    public Button btnsCheck;
    public int checker = 0;
    public Button btns[][] = new Button[4][4];
    public Button startGame = new Button("START");
    public Label win = new Label();

    @Override
    public void start(Stage stage) throws IOException {


        Separator sepHor = new Separator();
        sepHor.setValignment(VPos.CENTER);

        Separator sepHor1 = new Separator();
        sepHor1.setValignment(VPos.CENTER);

        VBox v1 = new VBox(20);
        Label z1 = new Label("ZADATAK 1");
        TextField palindrom = new TextField();
        palindrom.setAlignment(Pos.CENTER);
        palindrom.setPromptText("Unesite recenicu za proveru");
        Button bCheck = new Button("Palindrom?");
        Label lOdg = new Label("Odgovor servera");
        bCheck.setOnAction(e ->{
            try{
                Socket pomSok = new Socket("localhost", 12345);
                Socket sock = new Socket("localhost", 9000);
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())),true);

                String pal = palindrom.getText();
                if(pal.equals("")){
                    pal = " ";
                }

                String sPol = "PALINDROM#"+pal;
                out.println(sPol);

                String resp = in.readLine();
                lOdg.setText(resp);

                in.close();
                out.close();
                sock.close();
                pomSok.close();

            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
        v1.getChildren().addAll(z1, palindrom, bCheck, lOdg, sepHor);
        v1.setAlignment(Pos.CENTER);
        VBox vbMain = new VBox(10);
        vbMain.getChildren().add(v1);

        Label z2 = new Label("ZADATAK 2");
        Button btnSend = new Button("Odaberite fajl za slanje na server");
        Label lOdg2 = new Label("Odgovor servera");
        File recordsDir = new File("DATA_KLIJENT");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(recordsDir);
        btnSend.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);
            if(file != null){
                String name = file.getName();
                try{
                    Socket sock = new Socket("localhost", 9000);
                    Socket pomSok = new Socket("localhost", 12345);
                    BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())),true);

                    out.println(name+"# ");

                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    OutputStream os = pomSok.getOutputStream();

                    byte[] contents;
                    long fileLength = file.length();
                    long current = 0;


                    while(current!=fileLength) {
                        int size = 10000;
                        if (fileLength - current >= size)
                            current += size;
                        else {
                            size = (int) (fileLength - current);
                            current = fileLength;
                        }
                        contents = new byte[size];
                        bis.read(contents, 0, size);
                        os.write(contents);
                    }

                    os.flush();
                    lOdg2.setText(in.readLine());
                    pomSok.close();
                    out.close();
                    in.close();
                    sock.close();

                }catch (Exception ex){
                    ex.printStackTrace();
                }


            }
            else{
                lOdg2.setText("Morati odabrati fajl!");
            }
        });

        VBox v2 = new VBox(20);
        v2.setAlignment(Pos.CENTER);
        v2.getChildren().addAll(z2, btnSend,lOdg2,sepHor1);

        vbMain.getChildren().add(v2);




        GridPane gp = new GridPane();
        int br = 0;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                btns[i][j] = new Button(""+br++);

                double r = 35;
                btns[i][j].setShape(new Circle(r));
                btns[i][j].setMinSize(2*r, 2*r);
                btns[i][j].setMaxSize(2*r, 2*r);

                gp.add(btns[i][j], j, i);
            }
        }
        gp.setHgap(3);
        gp.setVgap(3);
        gp.setAlignment(Pos.CENTER);
        Label z3 = new Label("ZADATAK 3");
        startGame.setOnAction(e-> startClick());


        VBox v3 = new VBox(20);
        v3.setAlignment(Pos.CENTER);
        v3.getChildren().addAll(z3,gp,startGame, win);


        v1.setPadding(new Insets(10,5,10,5));


        vbMain.getChildren().add(v3);



        String font = ";-fx-font-size: 18";

        for(Node node : vbMain.getChildren()){
            if(node instanceof VBox){
                for(Node n : ((VBox) node).getChildren()){
                    if(n instanceof Label){
                        n.setStyle(" -fx-font: 18px Tahoma; -fx-font-weight: bold; -fx-text-fill: #b47131; -fx-font-size: 18");

                    }
                    if(n instanceof Button){
                        n.setStyle("-fx-text-fill: #96adb7; -fx-background-color: #5c6d48");
                    }
                    if(n instanceof TextField){
                        n.setStyle("-fx-text-fill: #96adb7; -fx-background-color: #497a9b");
                    }
                }
            }

            node.setStyle(node.getStyle() + font);
        }

        for(Node n : gp.getChildren()){
            n.setStyle(n.getStyle() + "; -fx-font-weight: bold; -fx-border-color: #96adb7; -fx-border-width: 2; -fx-text-fill: #79999a; -fx-background-color: #7d638a");

        }





        vbMain.setStyle("-fx-background-color: rgba(43,43,43,255)");

        Scene scene = new Scene(vbMain, 400, 850);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public void startClick(){
        win.setText("");
        startGame.setOnAction(ex -> {});
        startGame.setVisible(false);
        btnsCheck = null;
        checker = 0;
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        for(int i = 0; i < 16; i++) numbers.add(i);
        Collections.shuffle(numbers);


        int brRand = 0;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                btns[i][j].setText(""+numbers.get(brRand++));
                Button pom = btns[i][j];
                btns[i][j].setOnAction(ee->{
                    if(btnsCheck == null){
                        btnsCheck = pom;
                        pom.setStyle(pom.getStyle()+";-fx-border-color: #b47131;"); //96adb7
                    }
                    else{
                        btnsCheck.setStyle(pom.getStyle()+";-fx-border-color: #96adb7;"); //96adb7
                        int numFirst = Integer.parseInt(btnsCheck.getText());
                        int numSecond = Integer.parseInt(pom.getText());


                        if((numFirst == checker || numSecond == checker) && (numSecond + numFirst == 15)){

                            btnsCheck.setText(".");
                            btnsCheck.setOnAction(e->{});
                            pom.setText(".");
                            pom.setOnAction(e->{});
                            checker++;
                            btnsCheck = null;
                            if(checker == 8){
                                startGame.setOnAction(ex -> startClick());
                                startGame.setVisible(true);
                                win.setText("BRAVO");
                            }

                        }
                        else{
                            btnsCheck = null;
                        }
                    }
                });
            }
        }

    }

    public static void main(String[] args) {
        launch();
    }
}