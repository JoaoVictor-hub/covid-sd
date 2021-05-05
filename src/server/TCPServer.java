package server;
import client.Client;
import models.Usuario;
import java.net.*;
import java.sql.SQLException;
import java.io.*;
import com.google.gson.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Avaliacao;

import models.BaseModel;
import models.ClientList;
import models.ConfirmacaoChat;
import models.SolicitacaoChat;
import models.ConfirmacaoLogin;
import models.EnvioMensagem;
import models.RedirecionamentoMensagem;
import models.Resposta;
import models.Respostas;
import views.Server;

public class TCPServer extends Thread {
    private volatile static ArrayList<ClientList> clients = new ArrayList<ClientList>();
    protected Socket con;
    protected ServerSocket serverSocket;
    protected Server serverForm;
    protected int port;
    protected static int qtdClientes = 0;
    private volatile boolean running = true;
    protected BufferedReader in;
    protected PrintWriter out;
    

    public TCPServer(Server serverForm) {
        try {
            this.serverForm = serverForm;
            DBHelper DbHelper = new DBHelper();
            DbHelper.setupDatabase();
            print("Conexão com Socket Criada na porta " + port);
        } catch (SQLException ex) {
            System.err.println("Não foi possível ouvir na porta: " + port + ": " + ex);
            System.exit(1);
        }
    }
    
    public TCPServer(Socket socket){
        try {
            this.con = socket;
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            print("Conexão com Socket Criada na porta " + port);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    public void startServer(int port){
        try {
            this.serverSocket = new ServerSocket(port);
            
            while(true) {
                Socket con = serverSocket.accept();
                TCPServer t = new TCPServer(con);
                t.start();
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    private void printClientStatus(String message, int tipo) {
        if(tipo == 0){
            //message = serverForm.getUserStatusList()+ message + System.lineSeparator();
        }
        else{
           // message = serverForm.getUserStatusList()+ message + System.lineSeparator();
        }
        serverForm.printUserStatus(message);
        System.out.println(message);

    }
    
    private void print(String message) {
        //serverForm.println(message);
        System.out.println(message);
    }

    @Override
    public void run() {
        print("Nova Thread de Comunicação Iniciada");
        try {
            out = new PrintWriter(this.con.getOutputStream());
            
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                
                //System.out.println("Recebendo -> " + inputLine);
                print("Recebendo do cliente -> " + inputLine);
                String resposta;
                String codigo = getCod(inputLine);
                switch (codigo) {
                    case "1":
                        //TCPServer.qtdClientes = TCPServer.qtdClientes + 1;
                        //printClientStatus("Cliente ["+TCPServer.qtdClientes+"]"
                        //                   +"\n\tNome: "+getNomeUsuario(inputLine)
                        //                    +"\n\tPorta: "+this.port+"\n",1);
                        resposta = login(inputLine);
                        clients.add(new ClientList(this.con, out, getNomeUsuario(inputLine)));
                        System.out.println("Client adicionado -> " + getNomeUsuario(inputLine));
                        break;
                    case "5":
                        //clientSocket.close();
                        resposta = "Cliente Deslogado!";
                        break;
                    case "6":
                        resposta = probabilidade(inputLine);
                        break;
                    case "7":
                        resposta = probabilidade(inputLine);
                        break;
                    case "92":
                        resposta = solicitacaoChat(inputLine);
                        break;
                    case "73":
                        resposta = messageToClient(inputLine);
                        break;
                    default:
                        resposta = null;
                }
                print("Enviando para o cliente -> " + resposta);
                if (resposta != null) {
                    out.println(resposta);
                    out.flush();
                }
            }

        } catch (IOException ex) {
            //print(ex.getMessage());
            System.err.println("Problemas com a Comunicação no Servidor");
            System.exit(1);
        }
    }
    
    public String messageToClient(String input)
    {
        try{
            PrintWriter bwS;
            Gson gson = new Gson();
            String resposta = "";
            EnvioMensagem mensagem = new Gson().fromJson(input, EnvioMensagem.class);

            for(ClientList client : clients){
                System.out.println("usuario" + client.getUsuario() + " destino" + mensagem.getDestino() + " client" + client.getClient() + " mensagem" +  mensagem.getMsg());
                if(!client.getUsuario().equals(mensagem.getDestino())){
                    RedirecionamentoMensagem redireciona = new RedirecionamentoMensagem();
                    redireciona.setCodigo("74");
                    redireciona.setMsg(mensagem.getMsg());
                    redireciona.setOrigem(mensagem.getDestino());
                    String json = gson.toJson(redireciona);
                    System.out.println(json);
                    //client.getSocket();
                    PrintWriter out2 = new PrintWriter(client.getSocket().getOutputStream());
                    out2.println(json);
                    out2.flush();
                    //client.getClient().flush();
                }
            }

            return null;
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return null;
    }
    
    public void stopThread() {
        try {
            this.running = false;
            interrupt();
            //print("Thread de Comunicação Encerrada");
        } catch (Exception ex) {
            //print(ex.getMessage());
            System.err.println(ex);
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
        int count = 0;
        List<Resposta> list = respostas.getRespostas();
        for(Resposta el : list) {
            if(el.getResposta().equals("1")){
                count=count+1;
            }
         }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setCodigo("8");
        if(count>=3){
            avaliacao.setCovid("true");
        }
        else{
            avaliacao.setCovid("false");
        }
        resposta = gson.toJson(avaliacao);
        return resposta;
    }
   
    public String solicitacaoChat(String input){
        Gson gson = new Gson();
        ConfirmacaoChat confirmacao = new ConfirmacaoChat();
        SolicitacaoChat solicitacao = new Gson().fromJson(input, SolicitacaoChat.class);
        
        print("Solicitação de Chat feita por: " + solicitacao.getUsuario());
        confirmacao.setCodigo("72");
        confirmacao.setSucesso("true");
        confirmacao.setUsuario("saude"); // <- USUARIO SAUDE
        String json = gson.toJson(confirmacao);
        return json;
    }
    
    public String getNomeUsuario(String input) {
        Usuario login = new Gson().fromJson(input, Usuario.class);
        String usuario = login.getUsuario();
        return usuario;

    }
    
    public String login(String input) {
        try {
            DBHelper DbHelper = new DBHelper();
            Gson gson = new Gson();
            ConfirmacaoLogin confirmacao = new ConfirmacaoLogin();
            Dao<Usuario, ?> usuarioDao = DbHelper.getDao(Usuario.class);
            String json;

            confirmacao.setCodigo("11");
            confirmacao.setSuccess("true");
            confirmacao.setTipo("usuario");
            
            String usuario = getNomeUsuario(input);
            
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