package server;
import models.Usuario;
import java.net.*;
import java.sql.SQLException;
import java.io.*;
import com.google.gson.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import models.Avaliacao;

import models.BaseModel;
import models.ConfirmacaoLogin;
import models.Respostas;
import views.Server;

public class TCPServer extends Thread {
    protected Socket clientSocket = null;
    protected ServerSocket serverSocket = null;
    protected Server serverForm = null;
    private volatile boolean running = true;

    public TCPServer(int port, Server serverForm){
        try {
            this.serverForm = serverForm;
            DBHelper DbHelper = new DBHelper();
            DbHelper.setupDatabase(); 
            serverSocket = new ServerSocket(port);
            print("Conexão com Socket Criada na porta " + port);
        } catch (IOException | SQLException ex) {
            System.err.println("Não foi possível ouvir na porta: " + port + ": " + ex);
            System.exit(1);
        }
    }
    
    private void print(String message) {
        serverForm.println(message);
        System.out.println(message);
    }

    private void nowServe(Socket clientSoc) {
        clientSocket = clientSoc;
        try {
            PrintStream out = new PrintStream(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                if (this.interrupted()) {
                    out.close();
                    in.close();
                    clientSocket.close();
                    return;
                }
                //System.out.println("Recebendo -> " + inputLine);
                print("Recebendo do cliente -> " + inputLine);
                String resposta = null;
                String codigo = getCod(inputLine);
                switch (codigo) {
                case "1":
                    resposta = login(inputLine);
                    break;
                case "7":
                    resposta = probabilidade(inputLine);
                    break;
                default:
                     resposta = "Funcionalidade não implementada";
                }
                print("Enviando para o cliente -> " + resposta);
                out.println(resposta);

            }

            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException ex) {
            print(ex.getMessage());
            System.err.println("Problemas com a Comunicação no Servidor");
            System.exit(1);
        }
    }

    @Override
    public void run() {
        print("Nova Thread de Comunicação Iniciada");
        //System.out.println("Nova Thread de Comunicação Iniciada");
        try {
            while (this.running) {
                print("Esperando pela conexão");
                nowServe(serverSocket.accept());
                if (this.interrupted()) {
                    serverSocket.close();
                    return;
                }
            }
        } catch (IOException ex) {
            System.err.println("Conexão Falhou" + ex);
            System.exit(1);
        }
        
    }
    
    public void stopThread() {
        try {
            this.running = false;
            interrupt();
            print("Thread de Comunicação Encerrada");
        } catch (Exception ex) {
            print(ex.getMessage());
            System.err.println("ex");
            System.exit(1);
        }
    }

    public String getCod(String input) {
        try {
            BaseModel codigo = new Gson().fromJson(input, BaseModel.class);
            System.out.println("Codigo: " + codigo.getCodigo());
            return codigo.getCodigo();
        } catch (Exception ex) {
            return "0";
        }
    }
    public String probabilidade(String input) {
        Gson gson = new Gson();
        String resposta = "";
        Respostas respostas = new Gson().fromJson(input, Respostas.class);
        
        //TODO: Calcular probabilidade com base nas respostas do formulario
        
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setCodigo("8");
        avaliacao.setCovid("true");
        
        resposta = gson.toJson(avaliacao);
        return resposta;
    }
    
    public String login(String input) {
        try {
            DBHelper DbHelper = new DBHelper();
            Gson gson = new Gson();
            ConfirmacaoLogin confirmacao = new ConfirmacaoLogin();
            Dao<Usuario, ?> usuarioDao = DbHelper.getDao(Usuario.class);
            String json;
            Usuario login = new Gson().fromJson(input, Usuario.class);

            confirmacao.setCodigo("11");
            confirmacao.setSuccess("false");
            confirmacao.setTipo("");
            String usuario = login.getUsuario();

            QueryBuilder<Usuario, ?> queryBuilder = usuarioDao.queryBuilder();
            queryBuilder.where().eq("usuario", usuario);
            PreparedQuery<Usuario> preparedQuery = queryBuilder.prepare();
            Usuario usuarioRetornado = usuarioDao.queryForFirst(preparedQuery);

            if (usuarioRetornado != null) {
                    confirmacao.setTipo(usuarioRetornado.getTipo());
                    confirmacao.setSuccess("true");
                    json = gson.toJson(confirmacao);
                    return json;
            }

            json = gson.toJson(confirmacao);
            return json;
        }
        catch (Exception ex) {
            System.err.println(ex);
            return ex.getMessage();
        }
    }
}