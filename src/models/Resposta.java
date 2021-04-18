package models;

public class Resposta {
    private String id;
    private String resposta;
    
    public Resposta(String id, String resposta){
        this.id = id;
        this.resposta = resposta;
    }
    
    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
}
