package models;

import java.io.PrintWriter;
import java.net.Socket;

public class ClientList {
    private PrintWriter client;
    private String usuario;
    private Socket socket;

    public ClientList(Socket socket, PrintWriter client, String usuario){
        this.client = client;
        this.usuario = usuario;
        this.socket = socket;
    }
    public PrintWriter getClient() {
        return client;
    }

    public void setClient(PrintWriter client) {
        this.client = client;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
