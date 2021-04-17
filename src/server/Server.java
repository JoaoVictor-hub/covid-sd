package server;

import java.net.*;
import java.sql.SQLException;
import java.util.Scanner;

import models.Usuario;
import java.io.*;
import com.google.gson.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import models.BaseModel;
import models.ConfirmacaoLogin;

public class Server extends Thread {
	protected Socket clientSocket;
	
	public static void main(String[] args) throws IOException, SQLException {
		ServerSocket serverSocket = null;
		int port = 0;

		try {
			DBHelper DbHelper = new DBHelper();
			DbHelper.setupDatabase();
			Scanner sc = new Scanner(System.in);
			System.out.print("Informe a porta: ");  
			port = Integer.parseInt(sc.nextLine());  
			serverSocket = new ServerSocket(port);
			System.out.println("Conexão com Socket Criada");
			try {
				while (true) {
					System.out.println("Esperando pela conexão");
					new Server(serverSocket.accept());
				}
			} catch (IOException e) {
				System.err.println("Conexão Falhou");
				System.exit(1);
			}
		} catch (IOException | SQLException e) {
			System.err.println("Não foi possível ouvir na porta: " + port + ": " + e);
			System.exit(1);
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("Não foi possível fechar a porta: " + port);
				System.exit(1);
			}
		}
	}

	private Server(Socket clientSoc) {
		clientSocket = clientSoc;
		start();
	}

	public void run() {
		System.out.println("Nova Thread de Comunicação Iniciada");

		try {
			PrintStream out = new PrintStream(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				System.out.println("Recebendo -> " + inputLine);
				String resposta = null;
				String codigo = getCod(inputLine);
				switch (codigo) {
				case "1":
					resposta = Login(inputLine);
					break;
				default:
					// code block
				}
				out.println(resposta);

			}

			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Problemas com a Comunicação no Servidor");
			System.exit(1);
		}
	}

	public String getCod(String input) {
		try {
			BaseModel codigo = new Gson().fromJson(input, BaseModel.class);
			System.out.println("Código: " + codigo.getCodigo());
			return codigo.getCodigo();
		} catch (Exception ex) {
			return "0";
		}
	}

	public String Login(String input) {
		try {
			DBHelper DbHelper = new DBHelper();
			Gson gson = new Gson();
			ConfirmacaoLogin confirmacao = new ConfirmacaoLogin();
			Dao<Usuario, ?> usuarioDao = DbHelper.getDao(Usuario.class);
			String json;
			Usuario login = new Gson().fromJson(input, Usuario.class);
			
			confirmacao.setCodigo("11");
			confirmacao.setSuccess("false");
			String usuario = login.getUsuario();
			
			QueryBuilder<Usuario, ?> queryBuilder = usuarioDao.queryBuilder();
		    queryBuilder.where().eq("usuario", usuario);
		    PreparedQuery<Usuario> preparedQuery = queryBuilder.prepare();
			Usuario usuarioRetornado = usuarioDao.queryForFirst(preparedQuery);
			
			if (usuarioRetornado != null) {
				confirmacao.setTipo(usuarioRetornado.getTipo());
				confirmacao.setSuccess("true");
				json = gson.toJson(confirmacao);
				return json;
			}
			
			json = gson.toJson(confirmacao);
			return json;
		}
		catch (Exception e) {
			System.err.println(e);
			return e.getMessage();
		}

	}
}