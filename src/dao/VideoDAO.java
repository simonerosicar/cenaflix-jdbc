package dao;

import conexao.Conexao;
import model.Video;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de acesso a dados (DAO) para a entidade {@link Video}.
 * Implementa todas as operações CRUD sobre a tabela videos
 * do banco de dados ATIVIDADE1, utilizando JDBC para
 * comunicação com o MySQL.
 *
 * @author Simone Cardozo
 * @version 2.0
 * @see conexao.Conexao
 * @see model.Video
 */
public class VideoDAO {

    /**
     * Insere um novo vídeo na tabela videos.
     *
     * @param video objeto Video com os dados a serem inseridos
     * @return true se inserido com sucesso, false caso contrário
     */
    public boolean inserir(Video video) {
        String sql = "INSERT INTO videos (id, nome, data_lancamento, categoria) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, video.getId());
            stmt.setString(2, video.getNome());
            stmt.setDate(3, video.getDataLancamento());
            stmt.setString(4, video.getCategoria());
            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retorna todos os vídeos cadastrados na tabela videos,
     * ordenados pelo nome em ordem alfabética.
     *
     * @return lista de objetos Video; lista vazia se não houver registros
     */
    public List<Video> listarTodos() {
        List<Video> lista = new ArrayList<>();
        String sql = "SELECT * FROM videos ORDER BY nome";

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Video v = new Video();
                v.setId(rs.getInt("id"));
                v.setNome(rs.getString("nome"));
                v.setDataLancamento(rs.getDate("data_lancamento"));
                v.setCategoria(rs.getString("categoria"));
                lista.add(v);
            }

        } catch (SQLException e) {
            System.out.println("Não foi possível consultar os dados! "
                + e.getMessage());
        }
        return lista;
    }

    /**
     * Retorna vídeos filtrados pela categoria informada.
     * A busca é parcial usando LIKE.
     *
     * @param categoria texto da categoria a filtrar
     * @return lista de Video que correspondem ao filtro
     */
    public List<Video> filtrarPorCategoria(String categoria) {
        List<Video> lista = new ArrayList<>();
        String sql = "SELECT * FROM videos WHERE categoria LIKE ? ORDER BY nome";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + categoria + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Video v = new Video();
                v.setId(rs.getInt("id"));
                v.setNome(rs.getString("nome"));
                v.setDataLancamento(rs.getDate("data_lancamento"));
                v.setCategoria(rs.getString("categoria"));
                lista.add(v);
            }

        } catch (SQLException e) {
            System.out.println("Não foi possível filtrar os dados! "
                + e.getMessage());
        }
        return lista;
    }

    /**
     * Atualiza os dados de um vídeo existente no banco de dados.
     *
     * @param video objeto Video com os dados atualizados
     * @return true se atualizado com sucesso, false caso contrário
     */
    public boolean atualizar(Video video) {
        String sql = "UPDATE videos SET nome=?, data_lancamento=?, categoria=? "
                   + "WHERE id=?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, video.getNome());
            stmt.setDate(2, video.getDataLancamento());
            stmt.setString(3, video.getCategoria());
            stmt.setInt(4, video.getId());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Não foi possível atualizar os dados! "
                + e.getMessage());
            return false;
        }
    }

    /**
     * Remove um vídeo do banco de dados com base no ID informado.
     *
     * @param id identificador do vídeo a ser excluído
     * @return true se excluído com sucesso, false caso contrário
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM videos WHERE id=?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Não foi possível excluir o registro! "
                + e.getMessage());
            return false;
        }
    }
}