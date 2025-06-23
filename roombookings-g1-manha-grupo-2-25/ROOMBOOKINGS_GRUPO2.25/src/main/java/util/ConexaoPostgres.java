package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoPostgres {
    private static final String URL = "jdbc:postgresql://localhost:5432/roombookings";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "1234";

    public static Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
