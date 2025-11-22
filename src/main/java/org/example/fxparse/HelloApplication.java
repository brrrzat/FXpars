package org.example.fxparse;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    private Label labelDrop;
    public StackPane dropZone;

    public static void main(String[] args) {
        launch();
    }

    public void onDragOver(){
        dropZone.setOnDragOver(e -> {
            if(e.getDragboard().hasFiles()){
                e.acceptTransferModes(TransferMode.COPY);
            }
            e.consume();
        });
    }

    public void onDragEntered(){
        dropZone.setOnDragEntered(e ->{
            if(e.getDragboard().hasFiles()){
                labelDrop.setText("Claimed");
                labelDrop.setStyle("-fx-text-fill: green;");
            }
        });
    }
    public void onDragExict(){
        dropZone.setOnDragExited(e -> {
            dropZone.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 40; -fx-border-color: gray; -fx-border-style: dashed;");
        });
    }

    public void onDragDropped(){
        dropZone.setOnDragDropped(event ->{
            var db = event.getDragboard();
            boolean success = false;

            if (db.hasFiles()){
                success = true;
                db.getFiles().forEach(file -> {
                    try(BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))){
                        String line;
                        while ((line = reader.readLine()) != null) {
                            try {

                                HttpResponse<String> response = Unirest.get("https://imdb236.p.rapidapi.com/api/imdb/" + line + apiUrl)
                                        .header("x-rapidapi-key", "53b2cadf83mshc449934fcadb761p124d7ejsn9acb70ce0983")
                                        .header("x-rapidapi-host", "imdb236.p.rapidapi.com")
                                        .asString();


                                String body = response.getBody();


                                if (choice == 3) {
                                    JSONArray arr = new JSONArray(body);
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject writerObj = arr.getJSONObject(i);
                                        String writerName = writerObj.getString(target);
                                        System.out.println(writerName);
                                    }
                                } else {
                                    JSONObject obj = new JSONObject(body);
                                    double rating = obj.getDouble(target);
                                    System.out.println(rating);
                                }


                            } catch (UnirestException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }


        });
}}