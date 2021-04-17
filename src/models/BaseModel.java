package models;

import com.j256.ormlite.field.DatabaseField;

public class BaseModel {
	@DatabaseField(generatedId = true)
	public int id;
	
	private String cod;

	public String getCodigo() {
		return cod;
	}

	public void setCodigo(String codigo) {
		this.cod = codigo;
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
