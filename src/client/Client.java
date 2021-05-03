package client;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    protected Socket socket;
    PrintWriter out = null;
    BufferedReader in = null;
    
    public Client(String host, int port) {
    	try {
            this.socket = new Socket(host, port);
            this.start();
    	} catch (Exception ex) {
            System.err.println(ex);
        }
    }
    
    public void start() {
        System.out.println ("Conectando no host " + this.socket.getLocalAddress() + " na porta " + this.socket.getPort());

        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream());
        } catch (Exception ex) {
            System.err.println(ex);
            System.exit(1);
        }
    }
    
    public String send(String mensagem) throws IOException {
    	out.println(mensagem);
    	out.flush();
        String resposta = in.readLine(); 
        return resposta;
    }
    
    public void onlySend(String mensagem) throws IOException {
    	out.println(mensagem);
    	out.flush();
    }
    
    public String listen() throws IOException{
        String resposta = null;
        while(!in.ready()) {
            System.out.println ("Esperando pela resposta");
            if(in.ready()){
                resposta = in.readLine();
                System.out.println (resposta);
                break;
            }
            
        }
        return resposta;
    }
    
    public void close() throws IOException {
    	out.close();
    	in.close();
    	socket.close();
    	in = null;
        out = null;
        socket = null;
        System.out.println("Cliente desconectado\n");

    }
}
