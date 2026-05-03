package view;

import TelaCadastro.TelaCadastro;
import dao.VideoDAO;
import model.Video;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.List;

/**
 * Tela de gerenciamento de vídeos do sistema Cenaflix.
 * <p>
 * Permite consultar, atualizar e excluir registros cadastrados.
 * Exibe os dados em uma {@link JTable} com filtro dinâmico por categoria.
 * </p>
 *
 * <b>Funcionalidades:</b>
 * <ul>
 *   <li>Listagem de todos os vídeos em tabela</li>
 *   <li>Filtro dinâmico por categoria (digitação em tempo real)</li>
 *   <li>Seleção de linha para edição dos campos</li>
 *   <li>Atualização de registro selecionado</li>
 *   <li>Exclusão de registro selecionado</li>
 *   <li>Navegação para a tela de cadastro</li>
 * </ul>
 *
 * @author Simone Cardozo
 * @version 2.0
 * @see VideoDAO
 * @see Video
 */
public class TelaGerenciar extends JFrame {

    // Campos de edição
    private JTextField txtId, txtNome, txtData, txtCategoria;
    private JTextField txtFiltro;

    // Botões de ação
    private JButton btnAtualizar, btnExcluir, btnNovo, btnBuscar;

    // Tabela
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    // DAO
    private final VideoDAO dao = new VideoDAO();

    // Cores do tema Cenaflix
    private static final Color COR_FUNDO   = new Color(18, 18, 18);
    private static final Color COR_PAINEL  = new Color(30, 30, 30);
    private static final Color COR_CAMPO   = new Color(45, 45, 45);
    private static final Color COR_TEXTO   = new Color(230, 230, 230);
    private static final Color COR_LABEL   = new Color(180, 180, 180);
    private static final Color COR_TITULO  = new Color(229, 9, 20);
    private static final Color COR_ATUALIZAR = new Color(30, 120, 200);
    private static final Color COR_EXCLUIR   = new Color(180, 40, 40);
    private static final Color COR_NOVO      = new Color(50, 140, 50);
    private static final Color COR_BUSCAR    = new Color(100, 100, 100);

    /**
     * Construtor da tela de gerenciamento.
     * Inicializa a janela, constrói a interface e carrega os dados.
     */
    public TelaGerenciar() {
        configurarJanela();
        construirInterface();
        configurarEventos();
        carregarTabela(null);
    }

    // =========================================================================
    // CONFIGURAÇÃO DA JANELA
    // =========================================================================

    /**
     * Define as propriedades básicas da janela (título, tamanho, cor de fundo).
     */
    private void configurarJanela() {
        setTitle("Cenaflix — Gerenciar Vídeos");
        setSize(780, 580);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COR_FUNDO);
    }

    // =========================================================================
    // CONSTRUÇÃO DA INTERFACE
    // =========================================================================

    /**
     * Constrói todos os componentes visuais da tela.
     */
    private void construirInterface() {

        // --- Título ---
        JLabel lblTitulo = new JLabel("CENAFLIX", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(COR_TITULO);
        lblTitulo.setBounds(0, 12, 780, 30);
        add(lblTitulo);

        JLabel lblSub = new JLabel("Gerenciamento de Vídeos", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setForeground(COR_LABEL);
        lblSub.setBounds(0, 42, 780, 18);
        add(lblSub);

        JSeparator sep = new JSeparator();
        sep.setBounds(20, 65, 740, 2);
        sep.setForeground(new Color(60, 60, 60));
        add(sep);

        // --- Painel de edição (esquerda) ---
        JPanel painelEdicao = new JPanel(null);
        painelEdicao.setBounds(15, 75, 340, 230);
        painelEdicao.setBackground(COR_PAINEL);
        painelEdicao.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        add(painelEdicao);

        JLabel lblEdicao = new JLabel("Editar registro selecionado");
        lblEdicao.setFont(new Font("Arial", Font.BOLD, 11));
        lblEdicao.setForeground(COR_LABEL);
        lblEdicao.setBounds(10, 8, 320, 18);
        painelEdicao.add(lblEdicao);

        txtId        = criarCampoPainel(painelEdicao, "ID:",         40);
        txtNome      = criarCampoPainel(painelEdicao, "Nome:",       80);
        txtData      = criarCampoPainel(painelEdicao, "Data:",      120);
        txtCategoria = criarCampoPainel(painelEdicao, "Categoria:", 160);

        txtId.setEditable(false);
        txtId.setBackground(new Color(35, 35, 35));

        // --- Botões de ação ---
        btnAtualizar = criarBotao("Atualizar", 15,  320, COR_ATUALIZAR);
        btnExcluir   = criarBotao("Excluir",  130,  320, COR_EXCLUIR);
        btnNovo      = criarBotao("+ Novo",   245,  320, COR_NOVO);

        // --- Painel de filtro (direita) ---
        JPanel painelFiltro = new JPanel(null);
        painelFiltro.setBounds(370, 75, 390, 55);
        painelFiltro.setBackground(COR_PAINEL);
        painelFiltro.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        add(painelFiltro);

        JLabel lblFiltro = new JLabel("Filtrar por categoria:");
        lblFiltro.setFont(new Font("Arial", Font.PLAIN, 11));
        lblFiltro.setForeground(COR_LABEL);
        lblFiltro.setBounds(10, 8, 150, 18);
        painelFiltro.add(lblFiltro);

        txtFiltro = new JTextField();
        txtFiltro.setBounds(10, 28, 240, 18);
        txtFiltro.setBackground(COR_CAMPO);
        txtFiltro.setForeground(COR_TEXTO);
        txtFiltro.setCaretColor(COR_TEXTO);
        txtFiltro.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));
        txtFiltro.setFont(new Font("Arial", Font.PLAIN, 12));
        painelFiltro.add(txtFiltro);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(258, 26, 80, 22);
        btnBuscar.setBackground(COR_BUSCAR);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 11));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        painelFiltro.add(btnBuscar);

        // --- Tabela ---
        String[] colunas = {"ID", "Nome", "Data de Lançamento", "Categoria"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // células não editáveis diretamente
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setBackground(COR_CAMPO);
        tabela.setForeground(COR_TEXTO);
        tabela.setGridColor(new Color(60, 60, 60));
        tabela.setSelectionBackground(new Color(229, 9, 20, 180));
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setFont(new Font("Arial", Font.PLAIN, 12));
        tabela.setRowHeight(24);
        tabela.getTableHeader().setBackground(new Color(40, 40, 40));
        tabela.getTableHeader().setForeground(COR_LABEL);
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Larguras das colunas
        tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(120);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(370, 140, 390, 330);
        scroll.getViewport().setBackground(COR_CAMPO);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        add(scroll);

        // Label contador
        JLabel lblTotal = new JLabel("Selecione um registro na tabela para editar");
        lblTotal.setFont(new Font("Arial", Font.ITALIC, 11));
        lblTotal.setForeground(COR_LABEL);
        lblTotal.setBounds(370, 478, 390, 18);
        add(lblTotal);
    }

    /**
     * Cria um par label + campo de texto dentro de um painel.
     *
     * @param painel painel onde os componentes serão adicionados
     * @param label  texto do rótulo
     * @param y      posição vertical
     * @return o {@link JTextField} criado
     */
    private JTextField criarCampoPainel(JPanel painel, String label, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(10, y, 90, 22);
        lbl.setForeground(COR_LABEL);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        painel.add(lbl);

        JTextField campo = new JTextField();
        campo.setBounds(105, y, 220, 24);
        campo.setBackground(COR_CAMPO);
        campo.setForeground(COR_TEXTO);
        campo.setCaretColor(COR_TEXTO);
        campo.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));
        campo.setFont(new Font("Arial", Font.PLAIN, 12));
        painel.add(campo);

        return campo;
    }

    /**
     * Cria um botão de ação com estilo padrão da tela.
     *
     * @param texto texto exibido no botão
     * @param x     posição horizontal
     * @param y     posição vertical
     * @param cor   cor de fundo
     * @return o {@link JButton} criado
     */
    private JButton criarBotao(String texto, int x, int y, Color cor) {
        JButton btn = new JButton(texto);
        btn.setBounds(x, y, 105, 32);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btn);
        return btn;
    }

    // =========================================================================
    // EVENTOS
    // =========================================================================

    /**
     * Configura todos os listeners de eventos da tela.
     */
    private void configurarEventos() {

        // Clique na linha da tabela → preenche campos de edição
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {
                preencherCampos(tabela.getSelectedRow());
            }
        });

        // Filtro dinâmico ao digitar
        txtFiltro.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) {}
        });

        btnBuscar.addActionListener(e -> filtrar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());
        btnNovo.addActionListener(e -> abrirCadastro());
    }

    // =========================================================================
    // AÇÕES
    // =========================================================================

    /**
     * Carrega os vídeos na tabela, com filtro opcional por categoria.
     *
     * @param categoria texto para filtro; {@code null} ou vazio lista todos
     */
    private void carregarTabela(String categoria) {
        modeloTabela.setRowCount(0);
        List<Video> lista = (categoria == null || categoria.trim().isEmpty())
            ? dao.listarTodos()
            : dao.listarPorCategoria(categoria);

        for (Video v : lista) {
            modeloTabela.addRow(new Object[]{
                v.getId(),
                v.getNome(),
                v.getDataLancamento(),
                v.getCategoria()
            });
        }
        limparCampos();
    }

    /**
     * Aplica o filtro de categoria com base no texto digitado.
     */
    private void filtrar() {
        carregarTabela(txtFiltro.getText());
    }

    /**
     * Preenche os campos de edição com os dados da linha selecionada na tabela.
     *
     * @param linha índice da linha selecionada
     */
    private void preencherCampos(int linha) {
        txtId.setText(modeloTabela.getValueAt(linha, 0).toString());
        txtNome.setText(modeloTabela.getValueAt(linha, 1).toString());
        txtData.setText(modeloTabela.getValueAt(linha, 2).toString());
        txtCategoria.setText(modeloTabela.getValueAt(linha, 3).toString());
    }

    /**
     * Atualiza o registro selecionado com os dados dos campos de edição.
     * Valida os campos antes de persistir.
     */
    private void atualizar() {
        if (txtId.getText().trim().isEmpty()) {
            mostrarAviso("Selecione um registro na tabela para atualizar.");
            return;
        }
        if (txtNome.getText().trim().isEmpty() || txtData.getText().trim().isEmpty() || txtCategoria.getText().trim().isEmpty()) {
            mostrarAviso("Preencha todos os campos antes de atualizar.");
            return;
        }

        Date data;
        try {
            data = Date.valueOf(txtData.getText().trim());
        } catch (Exception ex) {
            mostrarAviso("Data inválida. O formato esperado é AAAA-MM-DD.");
            return;
        }

        Video video = new Video(
            Integer.parseInt(txtId.getText().trim()),
            txtNome.getText().trim(),
            data,
            txtCategoria.getText().trim()
        );

        boolean sucesso = dao.atualizar(video);
        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Registro atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarTabela(txtFiltro.getText());
        } else {
            mostrarAviso("Não foi possível atualizar os dados! Por favor, verifique os valores digitados.");
        }
    }

    /**
     * Exclui o registro selecionado após confirmação do usuário.
     */
    private void excluir() {
        if (txtId.getText().trim().isEmpty()) {
            mostrarAviso("Selecione um registro na tabela para excluir.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Deseja realmente excluir \"" + txtNome.getText() + "\"?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            boolean sucesso = dao.deletar(Integer.parseInt(txtId.getText().trim()));
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Registro excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarTabela(txtFiltro.getText());
            } else {
                mostrarAviso("Não foi possível excluir o registro! Por favor, tente novamente.");
            }
        }
    }

    /**
     * Abre a tela de cadastro e fecha a tela atual.
     */
    private void abrirCadastro() {
        new TelaCadastro().setVisible(true);
        dispose();
    }

    /**
     * Limpa os campos de edição.
     */
    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtData.setText("");
        txtCategoria.setText("");
    }

    /**
     * Exibe uma mensagem de aviso ao usuário.
     *
     * @param mensagem texto a ser exibido
     */
    private void mostrarAviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    // =========================================================================
    // MAIN
    // =========================================================================

    /**
     * Ponto de entrada da aplicação pela tela de gerenciamento.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new TelaGerenciar().setVisible(true));
    }
}
