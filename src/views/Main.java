package views;
import client.Client;
import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


public class Main extends Application {
    public static Client client = null;
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
                this.client = new Client(hostTx.getText(), Integer.parseInt(portTx.getText()));
                this.client.start();
                Login login  = new Login();
                login.getScene(window);
            });
            
            serverBt.setOnAction(e -> {
                Server server = new Server(Integer.parseInt(portTx.getText()));
                window.setScene(server.getScene());
            });
            
            window.setScene(scene);
            window.show();
        } catch(Exception ex) {
            System.err.println(ex);
        }
    }
    
    @Override
    public void stop() {
        try {
            if (client != null) {
                client.close();
            }
            window.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
