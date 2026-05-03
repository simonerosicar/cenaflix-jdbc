package dao;

import conexao.Conexao;
import model.Video;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de acesso a dados (DAO) para a entidade {@link Video}.
 * <p>
 * Implementa as quatro operações do CRUD:
 * <ul>
 *   <li>Create  — {@link #inserir(Video)}</li>
 *   <li>Read    — {@link #listarTodos()} e {@link #listarPorCategoria(String)}</li>
 *   <li>Update  — {@link #atualizar(Video)}</li>
 *   <li>Delete  — {@link #deletar(int)}</li>
 * </ul>
 *
 * @author Simone Cardozo
 * @version 2.0
 */
public class VideoDAO {

    // -------------------------------------------------------------------------
    // INSERT
    // -------------------------------------------------------------------------

    /**
     * Insere um novo vídeo no banco de dados.
     *
     * @param video objeto {@link Video} com os dados a serem inseridos
     * @return {@code true} se a inserção foi bem-sucedida; {@code false} caso contrário
     */
    public boolean inserir(Video video) {
        String sql = "INSERT INTO videos (id, nome, data_lancamento, categoria) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, video.getId());
            stmt.setString(2, video.getNome());
            stmt.setDate(3, video.getDataLancamento());
            stmt.setString(4, video.getCategoria());

            stmt.execute();
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Não foi possível inserir os dados! ID " + video.getId() + " já existe na base.");
            return false;
        } catch (Exception e) {
            System.out.println("Não foi possível inserir os dados! Por favor, verifique os valores digitados. Detalhe: " + e.getMessage());
            return false;
        }
    }

    // -------------------------------------------------------------------------
    // SELECT ALL
    // -------------------------------------------------------------------------

    /**
     * Retorna todos os vídeos cadastrados, ordenados por nome.
     *
     * @return lista de {@link Video}; lista vazia se não houver registros
     */
    public List<Video> listarTodos() {
        List<Video> lista = new ArrayList<>();
        String sql = "SELECT * FROM videos ORDER BY nome";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (Exception e) {
            System.out.println("Não foi possível listar os dados! Por favor, verifique a conexão com o banco. Detalhe: " + e.getMessage());
        }

        return lista;
    }

    // -------------------------------------------------------------------------
    // SELECT BY CATEGORIA (filtro dinâmico)
    // -------------------------------------------------------------------------

    /**
     * Retorna vídeos cuja categoria contenha o texto informado (busca parcial).
     * <p>
     * Passando uma string vazia ou {@code null}, retorna todos os registros.
     *
     * @param categoria texto para filtro; pode ser parcial (ex.: "ação")
     * @return lista de {@link Video} correspondentes ao filtro
     */
    public List<Video> listarPorCategoria(String categoria) {
        List<Video> lista = new ArrayList<>();
        String sql = "SELECT * FROM videos WHERE categoria LIKE ? ORDER BY nome";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String filtro = (categoria == null || categoria.trim().isEmpty()) ? "%" : "%" + categoria.trim() + "%";
            stmt.setString(1, filtro);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (Exception e) {
            System.out.println("Não foi possível realizar a busca! Por favor, verifique os valores digitados. Detalhe: " + e.getMessage());
        }

        return lista;
    }

    // -------------------------------------------------------------------------
    // SELECT BY ID
    // -------------------------------------------------------------------------

    /**
     * Busca um vídeo pelo seu identificador único.
     *
     * @param id identificador do vídeo
     * @return objeto {@link Video} encontrado, ou {@code null} se não existir
     */
    public Video buscarPorId(int id) {
        String sql = "SELECT * FROM videos WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapear(rs);
            }

        } catch (Exception e) {
            System.out.println("Não foi possível buscar o registro! Detalhe: " + e.getMessage());
        }

        return null;
    }

    // -------------------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------------------

    /**
     * Atualiza os dados de um vídeo existente no banco de dados.
     *
     * @param video objeto {@link Video} com os novos dados (o ID deve corresponder ao registro existente)
     * @return {@code true} se a atualização foi bem-sucedida; {@code false} caso contrário
     */
    public boolean atualizar(Video video) {
        String sql = "UPDATE videos SET nome = ?, data_lancamento = ?, categoria = ? WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, video.getNome());
            stmt.setDate(2, video.getDataLancamento());
            stmt.setString(3, video.getCategoria());
            stmt.setInt(4, video.getId());

            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (Exception e) {
            System.out.println("Não foi possível atualizar os dados! Por favor, verifique os valores digitados. Detalhe: " + e.getMessage());
            return false;
        }
    }

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------

    /**
     * Remove um vídeo do banco de dados pelo seu identificador.
     *
     * @param id identificador do vídeo a ser removido
     * @return {@code true} se a exclusão foi bem-sucedida; {@code false} caso contrário
     */
    public boolean deletar(int id) {
        String sql = "DELETE FROM videos WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (Exception e) {
            System.out.println("Não foi possível excluir o registro! Detalhe: " + e.getMessage());
            return false;
        }
    }

    // -------------------------------------------------------------------------
    // HELPER
    // -------------------------------------------------------------------------

    /**
     * Mapeia uma linha do {@link ResultSet} para um objeto {@link Video}.
     *
     * @param rs ResultSet posicionado na linha desejada
     * @return objeto {@link Video} preenchido
     * @throws SQLException se ocorrer erro ao ler as colunas
     */
    private Video mapear(ResultSet rs) throws SQLException {
        return new Video(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getDate("data_lancamento"),
            rs.getString("categoria")
        );
    }
}
