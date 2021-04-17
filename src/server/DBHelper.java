package server;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import models.Usuario;

public class DBHelper {
	private final static String DATABASE_URL = "jdbc:sqlite:sd.db";
	ConnectionSource connection = null;
	// private Dao<Usuario, Integer> usuarioDao;
	
	public DBHelper() throws SQLException {
		connection = new JdbcConnectionSource(DATABASE_URL);
	}
	
	public void setupDatabase() throws SQLException {
		TableUtils.createTableIfNotExists(connection, Usuario.class);
		Dao<Usuario, ?> dao = getDao(Usuario.class);
		dao.createIfNotExists(new Usuario(1, "admin", "admin"));
    }

	public <T> Dao<T, ?> getDao(Class<T> dataClass) throws SQLException {
		return DaoManager.createDao(connection, dataClass);
	}
}
