package models;

import java.io.PrintWriter;

public class ClientList {
    private PrintWriter client;
    private String usuario;

    public ClientList(PrintWriter client, String usuario){
        this.client = client;
        this.usuario = usuario;
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
}
