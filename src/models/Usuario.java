package models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Usuario")
public class Usuario extends BaseModel{
	@DatabaseField(generatedId = true)
	public Integer id;
        
	@DatabaseField(dataType = DataType.STRING, canBeNull = false)
	private String usuario;
	
	@DatabaseField(dataType = DataType.STRING, canBeNull = false)
	private String tipo;
	
	public Usuario() {
		
	}
	
	public Usuario(int id, String usuario, String tipo) {
		this.usuario = usuario;
		this.tipo = tipo;
		this.id = id;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}

