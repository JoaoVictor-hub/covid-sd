package views;
import client.Client;
import java.io.IOException;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import models.Usuario;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


public class Main extends Application {
    //static Client client = new Client("127.0.0.1", 10008);
    Stage window;
    @Override
    public void start(Stage primaryStage) {
        try {
            window = primaryStage;
            GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            Scene scene = new Scene(root,400,400);
            Button clientBt = new Button("Client");
            Button serverBt = new Button("Server");
            TextField hostTx = new TextField("127.0.0.1");
            Label hostLb = new Label("Host:");
            TextField portTx = new TextField("10008");
            Label portLb = new Label("Port:");
            
            root.add(clientBt, 1, 4);
            root.add(serverBt, 0, 4);
            root.add(hostTx, 1, 1);
            root.add(hostLb, 0, 1);
            root.add(portTx, 1, 2);
            root.add(portLb, 0, 2);
            root.setHgap(10);
            root.setVgap(10);
            root.setPadding(new Insets(15, 15, 15, 15));
            
            GridPane.setHalignment(clientBt, HPos.CENTER);
            GridPane.setHalignment(serverBt, HPos.CENTER);
            
            clientBt.setOnAction(e -> {
                    //client.start();
                    Usuario login = new Usuario();
                    login.setCodigo("1");
                    //login.setUsuario(usuarioText.getText());

                    Gson gson = new Gson();
                    String json = gson.toJson(login);

                    System.out.println ("Enviando para o host -> " + json);
                    //try {

                            //String resposta = client.send(json);
                     //       System.out.println ("Resposta -> " + resposta);

                    //} catch (IOException ex) {
                    //        System.out.println (ex);
                    //
                    //client.close();
            });
            window.setScene(scene);
            window.show();
        } catch(Exception ex) {
            System.err.println(ex);
        }
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
