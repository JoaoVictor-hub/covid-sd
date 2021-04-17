package models;

public class ConfirmacaoLogin extends BaseModel {
	private String success;
	private String tipo;
	
	public ConfirmacaoLogin() {
		
	}
	
	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
