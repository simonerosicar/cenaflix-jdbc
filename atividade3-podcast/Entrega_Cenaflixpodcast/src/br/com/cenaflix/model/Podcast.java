package br.com.cenaflix.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 * Entidade JPA que representa um podcast.
 *
 * @author Seu Nome
 * @version 1.0
 */
@Entity
@Table(name = "podcasts")
public class Podcast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "produtor", nullable = false, length = 150)
    private String produtor;

    @Column(name = "nome_episodio", nullable = false, length = 200)
    private String nomeEpisodio;

    @Column(name = "numero_episodio", nullable = false)
    private Integer numeroEpisodio;

    @Column(name = "duracao", nullable = false)
    private Integer duracao;

    @Column(name = "url_repositorio", nullable = false, length = 500)
    private String urlRepositorio;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    /**
     * Construtor padrão exigido pelo JPA.
     */
    public Podcast() {
    }

    /**
     * Construtor com todos os campos obrigatórios.
     *
     * @param produtor       nome do produtor
     * @param nomeEpisodio   título do episódio
     * @param numeroEpisodio número sequencial
     * @param duracao        duração em minutos
     * @param urlRepositorio URL de acesso
     */
    public Podcast(String produtor, String nomeEpisodio, Integer numeroEpisodio,
                   Integer duracao, String urlRepositorio) {
        this.produtor = produtor;
        this.nomeEpisodio = nomeEpisodio;
        this.numeroEpisodio = numeroEpisodio;
        this.duracao = duracao;
        this.urlRepositorio = urlRepositorio;
    }

    /** Define a data de criação automaticamente antes de persistir. */
    @PrePersist
    private void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }

    // ===== Getters e Setters =====

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getProdutor() { return produtor; }
    public void setProdutor(String produtor) { this.produtor = produtor; }

    public String getNomeEpisodio() { return nomeEpisodio; }
    public void setNomeEpisodio(String nomeEpisodio) { this.nomeEpisodio = nomeEpisodio; }

    public Integer getNumeroEpisodio() { return numeroEpisodio; }
    public void setNumeroEpisodio(Integer numeroEpisodio) { this.numeroEpisodio = numeroEpisodio; }

    public Integer getDuracao() { return duracao; }
    public void setDuracao(Integer duracao) { this.duracao = duracao; }

    public String getUrlRepositorio() { return urlRepositorio; }
    public void setUrlRepositorio(String urlRepositorio) { this.urlRepositorio = urlRepositorio; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    @Override
    public String toString() {
        return "Podcast{id=" + id + ", produtor='" + produtor + "'}";
    }
}
