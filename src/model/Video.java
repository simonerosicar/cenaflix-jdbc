package model;

import java.sql.Date;

/**
 * Modelo que representa um vídeo no sistema Cenaflix.
 */
public class Video {

    private int id;
    private String nome;
    private Date dataLancamento;
    private String categoria;

    // Construtor padrão
    public Video() {}

    // Construtor completo
    public Video(int id, String nome, Date dataLancamento, String categoria) {
        this.id = id;
        this.nome = nome;
        this.dataLancamento = dataLancamento;
        this.categoria = categoria;
    }

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public Date getDataLancamento() { return dataLancamento; }
    public String getCategoria() { return categoria; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDataLancamento(Date dataLancamento) { this.dataLancamento = dataLancamento; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return "Video{id=" + id + ", nome='" + nome + "', dataLancamento=" + dataLancamento + ", categoria='" + categoria + "'}";
    }
}
