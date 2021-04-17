package client;
import java.io.*;
import java.net.*;

public class Client {
	Socket socket = null;
	PrintStream out = null;
    BufferedReader in = null;
    
    public Client(String host, int port) {
    	try {
        	this.socket = new Socket(host, port);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void start() {
        System.out.println ("Conectando no host " + socket.getLocalAddress() + " na porta " + socket.getPort());

        try {
            out = new PrintStream(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
        	System.err.println(e);
			System.exit(1);
		}
    }
    
    public String send(String mensagem) throws IOException {
    	out.println(mensagem);
    	out.flush();
    	String resposta = in.readLine(); 
    	return resposta;
    }
    
    public void close() throws IOException {
    	out.close();
    	in.close();
    	socket.close();
    	in = null;
		out = null;
		socket = null;
    }
}
