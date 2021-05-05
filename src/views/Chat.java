package views;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import models.BaseModel;
import models.EnvioMensagem;
import models.RedirecionamentoMensagem;
import models.SolicitacaoChat;
import static views.Formulario.mensagem;
import static views.Main.client;

public class Chat {
    protected static GridPane gridPane = new GridPane();
    protected static TextArea msgTx = new TextArea();
    protected static TextField inputTx = new TextField();
    protected static Label mensagem;
    protected static Button enviarBt = new Button("Enviar");
    protected static Button iniciarBt = new Button("Iniciar");
    protected String nomeUsuario;
    
    public Chat(String nomeUsuario){
        this.nomeUsuario = nomeUsuario;
    }
  
    public Scene getScene(){
        gridPane.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(gridPane, 800, 600);
        Font font = Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR,20);
        mensagem = new Label(nomeUsuario);
        mensagem.setFont(font);
        gridPane.add(inputTx, 0, 4, 2, 2);
        gridPane.add(enviarBt, 3, 4);
        gridPane.add(iniciarBt, 4, 4);
        gridPane.add(msgTx, 0, 1, 2, 2);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(15, 15, 15, 15));
        mensagem.setTextFill(Color.BLUEVIOLET);
        gridPane.add(mensagem, 0, 0);
        msgTx.setEditable(false);
        
        enviarBt.setOnAction(e -> {
            msgTx.appendText(nomeUsuario + ": " + inputTx.getText() + System.lineSeparator());
            enviarMensagem(inputTx.getText());
            inputTx.clear();
        });
        
        iniciarBt.setOnAction(e -> {
            iniciarChat();
            msgTx.appendText("Chat Iniciado" + System.lineSeparator());
        });
                
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        try {
                            listenServer();
                        } catch (IOException ex) {
                            System.err.println(ex);
                        }
                    }
                };

                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }

                    // UI update is run on the Application thread
                    Platform.runLater(updater);
                }
            }
            

        }); 
        
        thread.setDaemon(true);
        thread.start();
        
        return scene;
    }
    
    public void iniciarChat() {
        try {
            Gson gson = new Gson();
            String resposta;
            SolicitacaoChat iniciarChat = new SolicitacaoChat();
            iniciarChat.setCodigo("92");
            iniciarChat.setUsuario(this.nomeUsuario);
            String json = gson.toJson(iniciarChat);
            System.out.println ("Enviando para o servidor -> " + json);
            client.onlySend(json);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    public String tratarResposta(String msg){
        try {
            Gson gson = new Gson();
            BaseModel codigo = new Gson().fromJson(msg, BaseModel.class);
            if(codigo.getCodigo().equals("74")){
                RedirecionamentoMensagem mensagem = new Gson().fromJson(msg, RedirecionamentoMensagem.class);
                return mensagem.getOrigem() + ": " + mensagem.getMsg();
            }
            return null;
        } catch(Exception ex) {
            return null;
        }
    }
    
    public void listenServer() throws IOException {
        String resposta;
        resposta = client.listen();
        if(resposta != null && !resposta.equals("")) {
            msgTx.appendText(resposta + System.lineSeparator());
        }
        
    }
    
    public void enviarMensagem(String msg){
        try {
            Gson gson = new Gson();
            String resposta;
            EnvioMensagem mensagem = new EnvioMensagem();
            mensagem.setCodigo("73");
            mensagem.setDestino(this.nomeUsuario.equals("saude") ? "saude" : "usuario"); // <- DESTINO SERÁ SETADO AQUI APÓS A CONFIRMAÇÃO DE CHAT DO SERVIDOR QUE RETORNARÁ O DESTINO
            mensagem.setMsg(msg);
            String json = gson.toJson(mensagem);
            System.out.println ("Enviando para o servidor -> " + json);
            client.onlySend(json);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
