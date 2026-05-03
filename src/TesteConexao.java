import conexao.Conexao;
import java.sql.Connection;

/**
 * Classe utilitária para testar a conexão com o banco de dados.
 * Execute esta classe para verificar se as configurações estão corretas.
 */
public class TesteConexao {
    public static void main(String[] args) {
        System.out.println("Testando conexão com o banco de dados...");
        Connection conn = Conexao.conectar();
        if (conn != null) {
            System.out.println("✔ Conexão estabelecida com sucesso!");
        } else {
            System.out.println("✘ Falha na conexão. Verifique o arquivo db.properties.");
        }
    }
}
