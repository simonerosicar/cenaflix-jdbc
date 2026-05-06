package br.com.cenaflix.dao;

import br.com.cenaflix.model.Podcast;
import br.com.cenaflix.util.JPAUtil;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * DAO de Podcast utilizando JPA (JPQL com parâmetros nomeados para
 * prevenir SQL Injection).
 *
 * @author Seu Nome
 * @version 1.0
 */
public class PodcastDAO {

    /**
     * Insere um novo podcast no banco de dados.
     *
     * @param podcast objeto a ser persistido
     * @return {@code true} se a operação foi bem-sucedida
     */
    public static boolean inserir(Podcast podcast) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(podcast);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erro ao inserir podcast: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Retorna todos os podcasts ordenados pelo ID decrescente.
     *
     * @return lista de podcasts ou lista vazia em caso de erro
     */
    public static List<Podcast> obterTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Podcast> query = em.createQuery(
                    "SELECT p FROM Podcast p ORDER BY p.id DESC", Podcast.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao obter podcasts: " + e.getMessage());
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    /**
     * Busca podcasts cujo produtor contenha o texto informado (case-insensitive).
     * Utiliza parâmetro nomeado para evitar SQL Injection.
     *
     * @param produtor texto a pesquisar
     * @return lista filtrada ou lista vazia
     */
    public static List<Podcast> obterPorProdutor(String produtor) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Podcast> query = em.createQuery(
                    "SELECT p FROM Podcast p WHERE LOWER(p.produtor) LIKE :produtor "
                    + "ORDER BY p.numeroEpisodio", Podcast.class);
            query.setParameter("produtor", "%" + produtor.toLowerCase() + "%");
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao filtrar por produtor: " + e.getMessage());
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    /**
     * Busca um podcast pelo seu ID.
     *
     * @param id identificador
     * @return Podcast encontrado ou {@code null}
     */
    public static Podcast obterPorId(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Podcast.class, id);
        } catch (Exception e) {
            System.err.println("Erro ao buscar podcast por ID: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Atualiza os dados de um podcast existente.
     *
     * @param podcast objeto com dados atualizados (deve ter ID preenchido)
     * @return {@code true} se a operação foi bem-sucedida
     */
    public static boolean atualizar(Podcast podcast) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(podcast);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erro ao atualizar podcast: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Remove um podcast pelo ID.
     *
     * @param id identificador do podcast a deletar
     * @return {@code true} se a operação foi bem-sucedida
     */
    public static boolean deletar(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Podcast podcast = em.find(Podcast.class, id);
            if (podcast != null) {
                em.remove(podcast);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erro ao deletar podcast: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
}
