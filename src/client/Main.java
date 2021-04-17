package client;
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
	static Client client = new Client("127.0.0.1", 10008);
	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane root = new GridPane();
			root.setAlignment(Pos.CENTER);
			Scene scene = new Scene(root,400,400);
			TextField usuarioText = new TextField();
			Label userName = new Label("Usuário:");
			Button button = new Button("Login");
			root.add(userName, 0, 1);
			root.add(usuarioText, 1, 1);
			root.add(button, 1, 2);
			root.setHgap(10);
			root.setVgap(10);
			root.setPadding(new Insets(25, 25, 25, 25));
			GridPane.setHalignment(button, HPos.CENTER);
			
			button.setOnAction(e -> {
				 
				Usuario login = new Usuario();
				login.setCodigo("1");
				login.setUsuario(usuarioText.getText());
				
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
			
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		client.start();
		launch(args);
		client.close();
	}
}
