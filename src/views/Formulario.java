package views;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import models.Avaliacao;
import models.BaseModel;
import models.Resposta;
import models.Respostas;
import models.SolicitacaoChat;
import static views.Main.client;

public class Formulario {
    protected static GridPane gridPane = new GridPane();
    protected static ComboBox q1Cb = new ComboBox();
    protected static ComboBox q2Cb = new ComboBox();
    protected static ComboBox q3Cb = new ComboBox();
    protected static ComboBox q5Cb = new ComboBox();
    protected static ComboBox q6Cb = new ComboBox();
    protected static TextField q4Tx = new TextField();
    protected static Label q1Lb = new Label("Você apresentou febre?");
    protected static Label q2Lb = new Label("Você apresentou tosse?");
    protected static Label q3Lb = new Label("Você apresentou dificuldade para respirar?");
    protected static Label q4Lb = new Label("Qual é sua Idade?");
    protected static Label q5Lb = new Label("Você está em isolamento social?");
    protected static Label q6Lb = new Label("Esteve em contato com alguém que apresentou sintomas?");
    protected static Label avisoLb = new Label("É necessário informar todos os campos");
    protected static Label mensagem;
    protected static Button enviarBt = new Button("Enviar");
    protected static Button sairBtn = new Button("Sair");

    
    public Scene getScene(Stage root, String nomeUsuario){
        gridPane.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(gridPane, 800, 600);
        
        Font font = Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR,20);
        mensagem = new Label("Seja bem-vindo(a) "+nomeUsuario);
        mensagem.setFont(font);
        q1Cb.getItems().add("Não");
        q1Cb.getItems().add("Sim");
        
        q2Cb.getItems().add("Não");
        q2Cb.getItems().add("Sim");
        
        q3Cb.getItems().add("Não");
        q3Cb.getItems().add("Sim");
        
        q5Cb.getItems().add("Não");
        q5Cb.getItems().add("Sim");
        
        q6Cb.getItems().add("Não");
        q6Cb.getItems().add("Sim");
        
        q4Tx.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                q4Tx.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        enviarBt.setOnAction(e -> {
            if (this.validarForm()){
                try {
                    avisoLb.setVisible(false);
                    Respostas respostas = preencherObjeto();
                    Gson gson = new Gson();
                    String json = gson.toJson(respostas);
                    System.out.println ("Enviando para o servidor -> " + json);
                    String resposta = client.send(json);
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                    Avaliacao av = new Gson().fromJson(resposta, Avaliacao.class);
                    if (av.getCovid().equals("true")) {
                        a.setTitle("Probabilidade Alta");
                        a.setHeaderText("Você possui mais de 3 sintomas e é possível que tenha coronavírus");
                        a.setContentText("Escolha uma das opções:");
                        a.setAlertType(Alert.AlertType.WARNING);
                        
                        ButtonType buttonTypeChat = new ButtonType("Abrir Chat",ButtonBar.ButtonData.LEFT);
                        ButtonType buttonTypeHospital = new ButtonType("Ver lista de Hospitais",ButtonBar.ButtonData.CANCEL_CLOSE);
                        
                        a.getButtonTypes().setAll(buttonTypeChat,buttonTypeHospital);
                                                
                        System.out.println(a);
                    } else {
                        a.setTitle("Probabilidade Baixa");
                        a.setHeaderText("Você possui menos de 3 sintomas e provavelmente não possui coronavírus!");
                        a.setContentText("Entretanto, fique atento para novos sintomas!");
                        System.out.println(a);
                    }
                    //a.show();
                    Optional<ButtonType> result = a.showAndWait();
                    ButtonType button = result.get();
                    if (button.getButtonData().equals(ButtonData.LEFT)) {
                        gson = new Gson();
                        SolicitacaoChat solicitaChat = new SolicitacaoChat();
                        solicitaChat.setCodigo("92");
                        solicitaChat.setUsuario(nomeUsuario);
                        json = gson.toJson(solicitaChat);
                        System.out.println ("Enviando para o servidor -> " + json);
                        client.onlySend(json);
                        Chat chat  = new Chat(nomeUsuario);
                        root.setScene(chat.getScene());
                    } 

                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
            else
                avisoLb.setVisible(true);
        });
        
        sairBtn.setOnAction(e -> {
            try {
                BaseModel logout = logout();
                Gson gson = new Gson();
                String json = gson.toJson(logout);
                System.out.println ("Enviando para o servidor -> " + json);
                client.onlySend(json);
                client.close();
                Stage stage = (Stage) sairBtn.getScene().getWindow();
                //Login login = new Login();
                //login.getScene(stage);
                //separar a tela principal do main
                stage.close();
            } catch (IOException ex) {
                System.err.println(ex);
            }
       });
        
        q1Cb.setOnAction(e -> {
            
        });
        
        gridPane.add(q1Lb, 0, 1);
        gridPane.add(q2Lb, 0, 2);
        gridPane.add(q3Lb, 0, 3);
        gridPane.add(q4Lb, 0, 4);
        gridPane.add(q5Lb, 0, 5);
        gridPane.add(q6Lb, 0, 6);
        
        gridPane.add(q1Cb, 1, 1);
        gridPane.add(q2Cb, 1, 2);
        gridPane.add(q3Cb, 1, 3);
        gridPane.add(q4Tx, 1, 4);
        gridPane.add(q5Cb, 1, 5);
        gridPane.add(q6Cb, 1, 6);
        
        mensagem.setTextFill(Color.BLUEVIOLET);
        gridPane.add(mensagem, 0, 0);
        
        avisoLb.setTextFill(Color.RED);
        gridPane.add(avisoLb, 0, 7);
        
        avisoLb.setVisible(false);
        sairBtn.setMaxWidth(100);
        enviarBt.setMaxWidth(100);
        q4Tx.setMaxWidth(70);
        
        gridPane.add(enviarBt, 0, 8, 2, 2);
        gridPane.add(sairBtn, 0,8, 2, 2);
        
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(15, 15, 15, 15));
        GridPane.setHalignment(enviarBt, HPos.CENTER);
        GridPane.setHalignment(sairBtn, HPos.RIGHT);
        
        return scene;
    }
    
    public boolean validarForm() {
        boolean valida = false;
        valida = q1Cb.getSelectionModel().getSelectedIndex() >= 0;
        valida = q2Cb.getSelectionModel().getSelectedIndex() >= 0;
        valida = q3Cb.getSelectionModel().getSelectedIndex() >= 0;
        valida = q5Cb.getSelectionModel().getSelectedIndex() >= 0;
        valida = q6Cb.getSelectionModel().getSelectedIndex() >= 0;
        
        return valida;
    }
    
    public void setResposta(String resposta) {
        
        //return valida;
    }
    
    public Respostas preencherObjeto(){
        List<Resposta> listaRespostas = new ArrayList<Resposta>();
        System.out.println (q1Cb.getSelectionModel().getSelectedItem().toString());
        listaRespostas.add(new Resposta("1", String.valueOf(q1Cb.getSelectionModel().getSelectedIndex())));
        listaRespostas.add(new Resposta("2", String.valueOf(q2Cb.getSelectionModel().getSelectedIndex())));
        listaRespostas.add(new Resposta("3", String.valueOf(q3Cb.getSelectionModel().getSelectedIndex())));
        listaRespostas.add(new Resposta("4", q4Tx.getText()));
        listaRespostas.add(new Resposta("5", String.valueOf(q5Cb.getSelectionModel().getSelectedIndex())));
        listaRespostas.add(new Resposta("6", String.valueOf(q6Cb.getSelectionModel().getSelectedIndex())));

        Respostas respostas = new Respostas(listaRespostas);
        respostas.setCodigo("6");
        
        
        return respostas;
    }
    
     public BaseModel logout(){
        BaseModel codigo = new BaseModel();
        codigo.setCodigo("5");
        return codigo;
    }
}
