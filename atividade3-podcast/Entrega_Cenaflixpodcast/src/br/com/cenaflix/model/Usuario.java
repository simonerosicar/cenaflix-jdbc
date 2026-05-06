package br.com.cenaflix.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entidade JPA que representa um usuário do sistema.
 * <p>
 * Tipos de usuário disponíveis:
 * <ul>
 *   <li><b>Administrador</b> – cadastrar, excluir e listar</li>
 *   <li><b>Operador</b>      – cadastrar e listar</li>
 *   <li><b>Usuário</b>       – somente listar</li>
 * </ul>
 *
 * @author Seu Nome
 * @version 1.0
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario", nullable = false, unique = true, length = 100)
    private String usuario;

    @Column(name = "senha", nullable = false, length = 100)
    private String senha;

    /**
     * Tipo de usuário: "Administrador", "Operador" ou "Usuário".
     */
    @Column(name = "tipo_usuario", nullable = false, length = 50)
    private String tipoUsuario;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    /**
     * Construtor padrão exigido pelo JPA.
     */
    public Usuario() {
    }

    /**
     * Construtor completo.
     *
     * @param usuario     login do usuário
     * @param senha       senha
     * @param tipoUsuario tipo de acesso
     */
    public Usuario(String usuario, String senha, String tipoUsuario) {
        this.usuario = usuario;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.ativo = true;
    }

    // ===== Getters e Setters =====

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", usuario='" + usuario + "'}";
    }
}
