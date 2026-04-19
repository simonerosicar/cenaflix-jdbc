package conexao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Classe utilitária responsável por gerenciar a conexão com o
 * banco de dados MySQL {@code ATIVIDADE1}.
 * <p>
 * Fornece um método estático para obtenção de uma nova conexão
 * JDBC a cada chamada.
 * </p>
 *
 * @author SeuNome
 * @version 2.0
 */
public class Conexao {

    /** URL de conexão com o banco de dados */
    private static final String URL    = "jdbc:mysql://localhost:3306/ATIVIDADE1";

    /** Usuário do banco de dados */
    private static final String USUARIO = "root";

    /** Senha do banco de dados */
  private static final String SENHA = System.getenv("DB_PASSWORD") != null 
    ? System.getenv("DB_PASSWORD") 
    : "";

    /**
     * Estabelece e retorna uma conexão ativa com o banco de dados.
     *
     * @return objeto {@link Connection} pronto para uso,
     *         ou {@code null} em caso de falha na conexão
     */
    public static Connection conectar() {
        try {
            Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            return conn;
        } catch (Exception e) {
            System.out.println("Erro na conexão: " + e);
            return null;
        }
    }
}