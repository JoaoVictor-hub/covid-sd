package views;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import models.Usuario;
import server.TCPServer;

public class Server {
    protected static TextArea textArea = new TextArea();
    protected static TextArea usuariosLogados = new TextArea();
    protected static Button iniciarBt = new Button("Iniciar Servidor");
    protected static Button fecharBt = new Button("Encerrar Servidor");
    protected static GridPane gridPane = new GridPane();
    protected Parent root;
    TCPServer t = null;
    protected ServerSocket serverSocket = null;
    protected int port;
    
    
    public Server(int port) throws IOException {
        this.port = port;
        
    }
    public Scene getScene() throws IOException{
        Scene scene = new Scene(gridPane, 800, 300);
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.add(iniciarBt, 0, 1);
        gridPane.add(fecharBt, 1, 1);
        gridPane.add(textArea, 0, 3, 3, 3);
        gridPane.add(usuariosLogados, 3, 3, 8, 3);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(15, 15, 15, 15));
        
        textArea.setEditable(false);
        usuariosLogados.setEditable(false);

        GridPane.setHalignment(fecharBt, HPos.RIGHT);
        GridPane.setHalignment(iniciarBt, HPos.LEFT);
        
        
        
        iniciarBt.setOnAction(e -> {
            t = new TCPServer(this);
            t.startServer(this.port);
        });
        
        fecharBt.setOnAction(e -> {
            this.t.stopThread();
        });
        
       
        
        return scene;
    }
    
    

    
    public void println(final String text) {
        textArea.appendText(text + System.lineSeparator());
    }
    
    public String getUserStatusList() {
        return usuariosLogados.getText();
    }
    
    public void printUserStatus(final String text) {
        usuariosLogados.setText(text);
    }

}
