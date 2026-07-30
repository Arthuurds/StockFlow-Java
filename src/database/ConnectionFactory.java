package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    // Configurações do banco de dados (Ajuste conforme seu ambiente MySQL)
    private static final String URL = "jdbc:mysql://localhost:3306/stockflow_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "Arthur2007.";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    // Bloco estático para garantir o carregamento do Driver JDBC do MySQL na memória
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC do MySQL não encontrado no classpath!", e);
        }
    }

    // Método privado para evitar instanciação da classe utilitária
    private ConnectionFactory() {
    }

    /**
     * Abre e retorna uma nova conexão ativa com o banco de dados MySQL.
     * @return Connection objeto de conexão JDBC
     * @throws SQLException caso ocorra falha na autenticação ou conexão
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Utilitário para fechar com segurança conexões abertas
     * @param connection instância de Connection a ser fechada
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
            }
        }
    }
}
