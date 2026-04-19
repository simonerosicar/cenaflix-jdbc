package model;

import java.sql.Date;

/**
 * Classe modelo que representa um vídeo/filme na plataforma Cenaflix.
 * Contém os atributos mapeados para as colunas da tabela {@code videos}
 * no banco de dados {@code ATIVIDADE1}.
 *
 * @author Simone Cardozo
 * @version 2.0
 */
public class Video {

    /** Identificador numérico único do vídeo */
    private int id;

    /** Nome ou título do vídeo */
    private String nome;

    /** Data de lançamento do vídeo */
    private Date dataLancamento;

    /** Categoria ou gênero do vídeo */
    private String categoria;

    // ── Getters ──────────────────────────────────────────────

    /**
     * Retorna o ID do vídeo.
     * @return identificador numérico
     */
    public int getId() { return id; }

    /**
     * Retorna o nome do vídeo.
     * @return título do vídeo
     */
    public String getNome() { return nome; }

    /**
     * Retorna a data de lançamento do vídeo.
     * @return data no formato {@link java.sql.Date}
     */
    public Date getDataLancamento() { return dataLancamento; }

    /**
     * Retorna a categoria do vídeo.
     * @return gênero/categoria
     */
    public String getCategoria() { return categoria; }

    // ── Setters ──────────────────────────────────────────────

    /**
     * Define o ID do vídeo.
     * @param id identificador numérico
     */
    public void setId(int id) { this.id = id; }

    /**
     * Define o nome do vídeo.
     * @param nome título do vídeo
     */
    public void setNome(String nome) { this.nome = nome; }

    /**
     * Define a data de lançamento do vídeo.
     * @param dataLancamento data no formato {@link java.sql.Date}
     */
    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    /**
     * Define a categoria do vídeo.
     * @param categoria gênero/categoria
     */
    public void setCategoria(String categoria) { this.categoria = categoria; }
}