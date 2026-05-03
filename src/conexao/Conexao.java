package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados.
 * As credenciais são carregadas do arquivo db.properties.
 */
public class Conexao {

    private static final String PROPERTIES_FILE = "/db.properties";

    public static Connection conectar() {
        try {
            Properties props = new Properties();
            InputStream input = Conexao.class.getResourceAsStream(PROPERTIES_FILE);

            if (input != null) {
                props.load(input);
            } else {
                // Fallback para variáveis de ambiente
                props.setProperty("db.url",  System.getenv().getOrDefault("DB_URL",  "jdbc:mysql://localhost:3306/ATIVIDADE1"));
                props.setProperty("db.user", System.getenv().getOrDefault("DB_USER", "root"));
                props.setProperty("db.pass", System.getenv().getOrDefault("DB_PASS", ""));
            }

            Connection conn = DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.pass")
            );
            return conn;

        } catch (Exception e) {
            System.out.println("Erro na conexão: " + e.getMessage());
            return null;
        }
    }
}
