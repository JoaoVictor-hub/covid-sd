package views;

import com.google.gson.Gson;
import java.io.IOException;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import models.Usuario;
import static views.Main.client;

public class Login {
    
    public Scene getScene(){
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(gridPane,400,400);
        Button loginBt = new Button("Entrar");
        TextField usuarioTx = new TextField();
        Label usuarioLb = new Label("Usuário:");
        
        gridPane.add(loginBt, 1, 2);
        gridPane.add(usuarioTx, 1, 1);
        gridPane.add(usuarioLb, 0, 1);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(15, 15, 15, 15));

        GridPane.setHalignment(loginBt, HPos.CENTER);

        loginBt.setOnAction(e -> {
                Usuario login = new Usuario();
                login.setCodigo("1");
                login.setUsuario(usuarioTx.getText());

                Gson gson = new Gson();
                String json = gson.toJson(login);

                System.out.println ("Enviando para o host -> " + json);
                try {
                   String resposta = client.send(json);
                   System.out.println ("Resposta -> " + resposta);

                } catch (IOException ex) {
                    System.out.println (ex);
                }
        });
        
        return scene;
    }
}
