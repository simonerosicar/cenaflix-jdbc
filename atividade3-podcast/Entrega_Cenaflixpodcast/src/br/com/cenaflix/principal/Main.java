package br.com.cenaflix.principal;

import br.com.cenaflix.util.JPAUtil;
import br.com.cenaflix.view.LoginFrame;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Ponto de entrada da aplicação Cenaflix.
 * Verifica a conexão JPA antes de abrir a tela de login.
 *
 * @author Seu Nome
 * @version 1.0
 */
public class Main {

    /**
     * Método principal.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        if (testarConexaoJPA()) {
            SwingUtilities.invokeLater(LoginFrame::new);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Não foi possível conectar ao banco de dados!\n"
                    + "Verifique as configurações em persistence.xml.",
                    "Erro de Conexão",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    /**
     * Testa a conexão com o banco via JPA.
     *
     * @return {@code true} se a conexão foi estabelecida com sucesso
     */
    private static boolean testarConexaoJPA() {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            System.out.println("✓ Conexão JPA estabelecida com sucesso!");
            return em != null && em.isOpen();
        } catch (Exception e) {
            System.err.println("✗ Erro na conexão JPA: " + e.getMessage());
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
