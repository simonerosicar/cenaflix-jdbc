package br.com.cenaflix.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Utilitário para gerenciar o EntityManagerFactory do JPA.
 * Mantém uma única instância de fábrica durante toda a execução.
 *
 * @author Seu Nome
 * @version 1.0
 */
public class JPAUtil {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("cenaflixPU");

    /**
     * Retorna um novo EntityManager para operações de banco de dados.
     *
     * @return EntityManager
     */
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Fecha a fábrica ao encerrar a aplicação.
     */
    public static void fechar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
