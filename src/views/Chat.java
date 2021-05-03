package views;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import models.BaseModel;
import models.EnvioMensagem;
import models.RedirecionamentoMensagem;
import models.SolicitacaoChat;
import static views.Main.client;

public class Chat {
    protected static GridPane gridPane = new GridPane();
    protected static TextArea msgTx = new TextArea();
    protected static TextField inputTx = new TextField();
    protected static Button enviarBt = new Button("Enviar");
    protected String nomeUsuario;
    
    public Chat(String nomeUsuario){
        this.nomeUsuario = nomeUsuario;
    }
  
    public Scene getScene(){
        gridPane.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(gridPane, 800, 600);
        
        gridPane.add(inputTx, 0, 4, 2, 2);
        gridPane.add(enviarBt, 3, 4);
        gridPane.add(msgTx, 0, 1, 2, 2);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(15, 15, 15, 15));
        
        msgTx.setEditable(false);
        
        enviarBt.setOnAction(e -> {
            String resposta = enviarMensagem(inputTx.getText());
            msgTx.appendText(nomeUsuario + ": " + inputTx.getText() + System.lineSeparator());
            //resposta = tratarResposta(resposta);
            //msgTx.appendText(resposta + System.lineSeparator());
            inputTx.clear();
        });
                
                
        return scene;
    }
    
    public String iniciarChat() {
        try {
            Gson gson = new Gson();
            String resposta;
            SolicitacaoChat iniciarChat = new SolicitacaoChat();
            iniciarChat.setCodigo("92");
            iniciarChat.setUsuario(this.nomeUsuario);
            String json = gson.toJson(iniciarChat);
            System.out.println ("Enviando para o servidor -> " + json);
            client.onlySend(json);
            resposta = client.listen();
            return resposta;
        } catch (IOException ex) {
            System.err.println(ex);
            return ex.getMessage();
        }
    }
    
    public String tratarResposta(String msg){
        Gson gson = new Gson();
        BaseModel codigo = new Gson().fromJson(msg, BaseModel.class);
        if(codigo.getCodigo().equals("74")){
            RedirecionamentoMensagem mensagem = new Gson().fromJson(msg, RedirecionamentoMensagem.class);
            return mensagem.getOrigem() + ": " + mensagem.getMsg();
        }
        return null;
    }
    
    public String enviarMensagem(String msg){
        try {
            Gson gson = new Gson();
            String resposta;
            EnvioMensagem mensagem = new EnvioMensagem();
            mensagem.setCodigo("73");
            mensagem.setDestino("saude"); // <- USUARIO SAUDE
            mensagem.setMsg(msg);
            String json = gson.toJson(mensagem);
            System.out.println ("Enviando para o servidor -> " + json);
            client.onlySend(json);
            resposta = client.listen();
            System.out.println ("Resposta do Servidor -> " + resposta);
            return resposta;
        } catch (IOException ex) {
            System.err.println(ex);
            return ex.getMessage();
        }
    }
}
