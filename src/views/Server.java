package views;

import com.google.gson.Gson;
import java.util.Objects;
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
    protected static Button iniciarBt = new Button("Iniciar Servidor");
    protected static Button fecharBt = new Button("Encerrar Servidor");
    protected static GridPane gridPane = new GridPane();
    protected Parent root;
    TCPServer t = null;
    
    public Server(int port) {
        this.t = new TCPServer(port, this);
    }
    public Scene getScene(){
        Scene scene = new Scene(gridPane, 800, 300);
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.add(iniciarBt, 0, 1);
        gridPane.add(fecharBt, 1, 1);
        gridPane.add(textArea, 0, 3, 3, 3);
        
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(15, 15, 15, 15));
        
        textArea.setEditable(false);
        GridPane.setHalignment(fecharBt, HPos.RIGHT);
        GridPane.setHalignment(iniciarBt, HPos.LEFT);
        
        iniciarBt.setOnAction(e -> {
            this.t.start();
        });
        
        fecharBt.setOnAction(e -> {
            this.t.stopThread();
        });
       
        
        return scene;
    }

    
    public void println(final String text) {
        textArea.appendText(text + System.lineSeparator());
    }

}
