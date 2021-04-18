package models;

import java.util.List;

public class Respostas extends BaseModel {
    private List<Resposta> respostas;
    
    public Respostas(List<Resposta> respostas) {
        this.respostas = respostas;
    }
    public List<Resposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<Resposta> respostas) {
        this.respostas = respostas;
    }
}