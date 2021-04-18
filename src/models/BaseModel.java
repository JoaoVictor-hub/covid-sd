package models;

import com.j256.ormlite.field.DatabaseField;

public class BaseModel {
	
	private String cod;

	public String getCodigo() {
		return cod;
	}

	public void setCodigo(String codigo) {
		this.cod = codigo;
	}
}
