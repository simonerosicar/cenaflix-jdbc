package br.com.cenaflix.dao;

import br.com.cenaflix.model.Usuario;
import br.com.cenaflix.util.JPAUtil;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * DAO de Usuario utilizando JPA (JPQL com parâmetros nomeados para
 * prevenir SQL Injection).
 *
 * @author Seu Nome
 * @version 1.0
 */
public class UsuarioDAO {

    /**
     * Autentica um usuário verificando login, senha e status ativo.
     * Utiliza parâmetros nomeados para evitar SQL Injection.
     *
     * @param usuario nome de login
     * @param senha   senha em texto plano
     * @return objeto {@link Usuario} autenticado ou {@code null} se inválido
     */
    public static Usuario autenticar(String usuario, String senha) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.usuario = :usuario "
                    + "AND u.senha = :senha AND u.ativo = true", Usuario.class);
            query.setParameter("usuario", usuario);
            query.setParameter("senha", senha);

            List<Usuario> resultado = query.getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } catch (Exception e) {
            System.err.println("Erro ao autenticar usuário: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }
}
