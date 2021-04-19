package views;

import com.google.gson.Gson;
import java.io.IOException;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.ConfirmacaoLogin;
import models.Usuario;
import static views.Main.client;

public class Login {
    
    public void getScene(Stage root){
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

                System.out.println ("Enviando para o servidor -> " + json);
                try {
                   String resposta = client.send(json);
                   System.out.println ("Resposta do servidor-> " + resposta);
                   
                   ConfirmacaoLogin confirmacao = new Gson().fromJson(resposta, ConfirmacaoLogin.class);
                   
                   if (confirmacao.getSuccess().equals("true")){
                       Formulario form  = new Formulario();
                       root.setScene(form.getScene(usuarioTx.getText()));
                   }
                   

                } catch (IOException ex) {
                    System.err.println (ex);
                }
        });
        root.setScene(scene);
    }
}
