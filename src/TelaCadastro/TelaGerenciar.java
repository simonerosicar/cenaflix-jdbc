package TelaCadastro;

import dao.VideoDAO;
import model.Video;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

/**
 * Tela principal de gerenciamento de vídeos da plataforma Cenaflix.
 * <p>
 * Exibe os vídeos cadastrados em uma {@link JTable} com suporte a:
 * <ul>
 *   <li>Consulta de todos os registros</li>
 *   <li>Filtro dinâmico por categoria</li>
 *   <li>Atualização de registros selecionados</li>
 *   <li>Exclusão de registros com confirmação</li>
 * </ul>
 *
 * @author Simone Cardozo

 * @version 2.0
 * @see dao.VideoDAO
 * @see model.Video
 * @see TelaCadastro
 */
public class TelaGerenciar extends JFrame {

    /** DAO para operações no banco de dados */
    private final VideoDAO dao = new VideoDAO();

    /** Modelo de dados usado pela JTable */
    private DefaultTableModel modeloTabela;

    /** Componentes da interface */
    private JTable tblVideos;
    private JTextField txtId, txtNome, txtData;
    private JComboBox<String> cboCategoria, cboFiltro;
    private JButton btnBuscar, btnAtualizar, btnExcluir,
                    btnNovoCadastro, btnLimpar;

    /**
     * Construtor que inicializa e configura todos os componentes
     * da tela de gerenciamento, carregando os dados ao abrir.
     */
    public TelaGerenciar() {
        setTitle("Cenaflix — Gerenciar Vídeos");
        setSize(750, 550);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ── Título ────────────────────────────────────────────
        JLabel lblTitulo = new JLabel("Gerenciar Vídeos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBounds(0, 8, 750, 30);
        add(lblTitulo);

        // ── Filtro por categoria ──────────────────────────────
        JLabel lblFiltro = new JLabel("Filtrar por Categoria:");
        lblFiltro.setBounds(20, 50, 150, 25);
        add(lblFiltro);

        String[] categorias = {
            "(Todas)", "Ação", "Aventura", "Comédia",
            "Drama", "Ficção Científica", "Romance", "Terror"
        };

        cboFiltro = new JComboBox<>(categorias);
        cboFiltro.setBounds(175, 50, 180, 25);
        add(cboFiltro);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(365, 50, 90, 25);
        btnBuscar.setBackground(new Color(30, 100, 200));
        btnBuscar.setForeground(Color.WHITE);
        add(btnBuscar);

        // ── Tabela ────────────────────────────────────────────
        modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Nome", "Data Lançamento", "Categoria"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // células não editáveis diretamente
            }
        };

        tblVideos = new JTable(modeloTabela);
        tblVideos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblVideos.getTableHeader().setReorderingAllowed(false);

        // Ajusta largura das colunas
        tblVideos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblVideos.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblVideos.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblVideos.getColumnModel().getColumn(3).setPreferredWidth(120);

        JScrollPane scroll = new JScrollPane(tblVideos);
        scroll.setBounds(20, 90, 700, 200);
        add(scroll);

        // Ao clicar na linha → preenche os campos automaticamente
        tblVideos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherCampos();
        });

        // ── Campos de edição ──────────────────────────────────
        JLabel lblSeparador = new JLabel("─── Editar registro selecionado ───────────────────────────────────────────");
        lblSeparador.setFont(new Font("Arial", Font.PLAIN, 10));
        lblSeparador.setForeground(Color.GRAY);
        lblSeparador.setBounds(20, 300, 700, 20);
        add(lblSeparador);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(20, 325, 80, 25);
        add(lblId);
        txtId = new JTextField();
        txtId.setBounds(100, 325, 80, 25);
        txtId.setEditable(false);          // ID não pode ser alterado
        txtId.setBackground(new Color(230, 230, 230));
        add(txtId);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(200, 325, 60, 25);
        add(lblNome);
        txtNome = new JTextField();
        txtNome.setBounds(265, 325, 180, 25);
        add(txtNome);

        JLabel lblData = new JLabel("Data:");
        lblData.setBounds(20, 365, 80, 25);
        add(lblData);
        txtData = new JTextField();
        txtData.setBounds(100, 365, 100, 25);
        add(txtData);
        JLabel lblDica = new JLabel("(DD/MM/YYYY)");
        lblDica.setFont(new Font("Arial", Font.ITALIC, 10));
        lblDica.setForeground(Color.GRAY);
        lblDica.setBounds(205, 365, 100, 25);
        add(lblDica);

        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setBounds(315, 365, 80, 25);
        add(lblCategoria);
        cboCategoria = new JComboBox<>(new String[]{
            "Ação", "Aventura", "Comédia", "Drama",
            "Ficção Científica", "Romance", "Terror"
        });
        cboCategoria.setBounds(400, 365, 150, 25);
        add(cboCategoria);

        // ── Botões de ação ────────────────────────────────────
        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(20, 415, 120, 32);
        btnAtualizar.setBackground(new Color(200, 130, 0));
        btnAtualizar.setForeground(Color.WHITE);
        add(btnAtualizar);

        btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(155, 415, 120, 32);
        btnExcluir.setBackground(new Color(200, 50, 50));
        btnExcluir.setForeground(Color.WHITE);
        add(btnExcluir);

        btnLimpar = new JButton("Limpar Seleção");
        btnLimpar.setBounds(290, 415, 140, 32);
        btnLimpar.setBackground(new Color(100, 100, 100));
        btnLimpar.setForeground(Color.WHITE);
        add(btnLimpar);

        btnNovoCadastro = new JButton("+ Novo Cadastro");
        btnNovoCadastro.setBounds(445, 415, 150, 32);
        btnNovoCadastro.setBackground(new Color(50, 150, 50));
        btnNovoCadastro.setForeground(Color.WHITE);
        add(btnNovoCadastro);

        // ── Eventos ───────────────────────────────────────────
        btnBuscar.addActionListener(e -> buscar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limparCampos());
        btnNovoCadastro.addActionListener(e -> abrirCadastro());

        // Carrega dados ao abrir
        carregarTabela(dao.listarTodos());
    }

    /**
     * Carrega uma lista de vídeos na JTable, substituindo os dados anteriores.
     *
     * @param lista lista de objetos {@link Video} a exibir
     */
    private void carregarTabela(List<Video> lista) {
        modeloTabela.setRowCount(0); // limpa linhas anteriores
        for (Video v : lista) {
            modeloTabela.addRow(new Object[]{
                v.getId(),
                v.getNome(),
                v.getDataLancamento(),
                v.getCategoria()
            });
        }
    }

    /**
     * Preenche os campos de edição com os dados da linha
     * selecionada na {@link JTable}.
     */
    private void preencherCampos() {
        int linha = tblVideos.getSelectedRow();
        if (linha < 0) return;

        txtId.setText(modeloTabela.getValueAt(linha, 0).toString());
        txtNome.setText(modeloTabela.getValueAt(linha, 1).toString());

        // Converte Date para DD/MM/YYYY
        Object dataObj = modeloTabela.getValueAt(linha, 2);
        if (dataObj != null) {
            Date d = Date.valueOf(dataObj.toString());
            String[] p = d.toString().split("-"); // YYYY-MM-DD
            txtData.setText(p[2] + "/" + p[1] + "/" + p[0]);
        }

        // Seleciona a categoria no ComboBox
        String cat = modeloTabela.getValueAt(linha, 3).toString();
        for (int i = 0; i < cboCategoria.getItemCount(); i++) {
            if (cboCategoria.getItemAt(i).equalsIgnoreCase(cat)) {
                cboCategoria.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * Executa o filtro dinâmico na tabela com base na categoria
     * selecionada no {@link JComboBox} de filtro.
     * Se "(Todas)" for selecionado, exibe todos os registros.
     */
    private void buscar() {
        String cat = cboFiltro.getSelectedItem().toString();
        if (cat.equals("(Todas)")) {
            carregarTabela(dao.listarTodos());
        } else {
            carregarTabela(dao.filtrarPorCategoria(cat));
        }
        limparCampos();
    }

    /**
     * Valida os campos de edição, converte os dados e chama o DAO
     * para atualizar o registro no banco de dados.
     * Exibe mensagem de confirmação ou erro ao usuário.
     */
    private void atualizar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Selecione um vídeo na tabela para atualizar!",
                "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (txtNome.getText().trim().isEmpty() || txtData.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Preencha todos os campos antes de atualizar!",
                "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date data;
        try {
            String[] p = txtData.getText().trim().split("/");
            data = Date.valueOf(p[2] + "-" + p[1] + "-" + p[0]);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Data inválida! Use DD/MM/YYYY.",
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Video video = new Video();
        video.setId(Integer.parseInt(txtId.getText()));
        video.setNome(txtNome.getText().trim());
        video.setDataLancamento(data);
        video.setCategoria(cboCategoria.getSelectedItem().toString());

        if (dao.atualizar(video)) {
            JOptionPane.showMessageDialog(this, "✅ Vídeo atualizado com sucesso!");
            carregarTabela(dao.listarTodos());
            limparCampos();
        } else {
            JOptionPane.showMessageDialog(this,
                "❌ Não foi possível atualizar os dados!\n"
                + "Por favor, verifique os valores digitados!",
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Solicita confirmação do usuário e, em caso positivo,
     * chama o DAO para remover o vídeo selecionado do banco de dados.
     */
    private void excluir() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Selecione um vídeo na tabela para excluir!",
                "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(this,
            "Deseja realmente excluir o vídeo:\n" + txtNome.getText() + "?",
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {
            if (dao.excluir(Integer.parseInt(txtId.getText()))) {
                JOptionPane.showMessageDialog(this, "✅ Vídeo excluído com sucesso!");
                carregarTabela(dao.listarTodos());
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this,
                    "❌ Não foi possível excluir o registro!\n"
                    + "Verifique se o ID informado existe!",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Limpa os campos de edição e desfaz a seleção na tabela.
     */
    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtData.setText("");
        cboCategoria.setSelectedIndex(0);
        tblVideos.clearSelection();
    }

    /**
     * Abre a tela de cadastro ({@link TelaCadastro}) e fecha a atual.
     */
    private void abrirCadastro() {
        new TelaCadastro().setVisible(true);
        this.dispose();
    }
}