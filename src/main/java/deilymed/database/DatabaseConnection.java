package deilymed.database;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    // Define o nome do arquivo do banco de dados.
    // Ele será criado na raiz do projeto.
    private static final String DATABASE_FILE = "deilymed.db";
    public static final String URL = "jdbc:sqlite:" + DATABASE_FILE;

    /**
     * Obtém uma nova conexão com o banco de dados.
     * @return um objeto Connection.
     */
    public static Connection getConnection() {
        try {
            // PRAGMA foreign_keys=ON precisa ser ativado para cada conexão
            // para garantir a integridade referencial.
            org.sqlite.SQLiteConfig config = new org.sqlite.SQLiteConfig();
            config.enforceForeignKeys(true);
            return DriverManager.getConnection(URL, config.toProperties());
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            return null;
        }
    }

    /**
     * Inicializa o banco de dados.
     * Verifica se o arquivo .db existe. Se não, executa o script schema.sql.
     */
    public static void initializeDatabase() {
        File dbFile = new File(DATABASE_FILE);
        if (!dbFile.exists()) {
            System.out.println("Banco de dados não encontrado. Criando tabelas...");
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {

                // Lê o arquivo schema.sql do diretório resources
                InputStream is = DatabaseConnection.class.getResourceAsStream("/sql/schema.sql");
                if (is == null) {
                    System.err.println("ERRO CRÍTICO: arquivo schema.sql não encontrado nos resources!");
                    return;
                }

                String script = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                // SQLite JDBC não suporta executar múltiplos comandos de uma vez
                // então dividimos o script por ponto e vírgula.
                for (String command : script.split(";")) {
                    if (!command.trim().isEmpty()) {
                        stmt.execute(command);
                    }
                }
                System.out.println("Tabelas criadas com sucesso!");

            } catch (Exception e) {
                System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
                // Se a inicialização falhar, apaga o arquivo para tentar de novo na próxima vez.
                dbFile.delete();
            }
        } else {
            System.out.println("Banco de dados já existe. Conectando...");
        }
    }
}
