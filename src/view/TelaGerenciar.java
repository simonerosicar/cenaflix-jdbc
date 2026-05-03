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
 * Sprint 2 — Atividade 2.
 *
 * @author Simone Cardozo
 * @version 2.0
 */
public class TelaGerenciar extends JFrame {

    private JTextField txtId, txtNome, txtData, txtCategoria;
    private JTextField txtFiltro;
    private JButton btnAtualizar, btnExcluir, btnNovo, btnBuscar;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final VideoDAO dao = new VideoDAO();

    private static final Color COR_FUNDO = new Color(220, 220, 220);
    private static final Color COR_CAMPO = Color.WHITE;
    private static final Color COR_TEXTO = Color.BLACK;
    private static final Color COR_LABEL = Color.BLACK;
    private static final Color COR_BORDA = new Color(180, 180, 180);

    /**
     * Construtor da tela de gerenciamento.
     */
    public TelaGerenciar() {
        configurarJanela();
        construirInterface();
        configurarEventos();
        carregarTabela(null);
    }

    // =========================================================================
    // JANELA
    // =========================================================================
    private void configurarJanela() {
        setTitle("Cenaflix — Gerenciar Vídeos");
        setSize(860, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(700, 500));
    }

    // =========================================================================
    // INTERFACE — usa BorderLayout para ser proporcional
    // =========================================================================
    private void construirInterface() {
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(COR_FUNDO);

        // ---- PAINEL TOPO (título + campos + filtro) ----
        JPanel painelTopo = new JPanel(new BorderLayout(10, 5));
        painelTopo.setBackground(COR_FUNDO);
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        // Título
        JPanel painelTitulo = new JPanel(new GridLayout(2, 1, 0, 2));
        painelTitulo.setBackground(COR_FUNDO);

        JLabel lblTitulo = new JLabel("CENAFLIX", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(COR_TEXTO);
        painelTitulo.add(lblTitulo);

        JLabel lblSub = new JLabel("GERENCIAR VÍDEOS", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.BOLD, 14));
        lblSub.setForeground(COR_TEXTO);
        painelTitulo.add(lblSub);

        painelTopo.add(painelTitulo, BorderLayout.NORTH);

        // Campos de edição + filtro
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBackground(COR_FUNDO);
        painelCampos.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(3, 5, 3, 5);
        g.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0 — labels
        g.gridy = 0; g.weightx = 0.08;
        g.gridx = 0; painelCampos.add(rotulo("ID:"), g);
        g.gridx = 1; g.weightx = 0.35; painelCampos.add(rotulo("Nome:"), g);
        g.gridx = 2; g.weightx = 0.25; painelCampos.add(rotulo("Data (AAAA-MM-DD):"), g);
        g.gridx = 3; g.weightx = 0.25; painelCampos.add(rotulo("Categoria:"), g);
        g.gridx = 4; g.weightx = 0.07; painelCampos.add(rotulo("Filtrar por categoria:"), g);

        // Linha 1 — campos
        g.gridy = 1;
        txtId = campo(); txtId.setEditable(false); txtId.setBackground(new Color(200,200,200));
        g.gridx = 0; g.weightx = 0.08; painelCampos.add(txtId, g);

        txtNome = campo();
        g.gridx = 1; g.weightx = 0.35; painelCampos.add(txtNome, g);
        adicionarPlaceholder(txtNome, "Selecione um registro");

        txtData = campo();
        g.gridx = 2; g.weightx = 0.25; painelCampos.add(txtData, g);
        adicionarPlaceholder(txtData, "Ex: 2023-06-15");

        txtCategoria = campo();
        g.gridx = 3; g.weightx = 0.25; painelCampos.add(txtCategoria, g);
        adicionarPlaceholder(txtCategoria, "Ex: Ação");

        // Filtro + botão buscar
        JPanel painelFiltro = new JPanel(new BorderLayout(5, 0));
        painelFiltro.setBackground(COR_FUNDO);
        txtFiltro = campo();
        adicionarPlaceholder(txtFiltro, "Ex: Ação");
        btnBuscar = botao("Buscar");
        painelFiltro.add(txtFiltro, BorderLayout.CENTER);
        painelFiltro.add(btnBuscar, BorderLayout.EAST);
        g.gridx = 4; g.weightx = 0.07; painelCampos.add(painelFiltro, g);

        painelTopo.add(painelCampos, BorderLayout.CENTER);
        add(painelTopo, BorderLayout.NORTH);

        // ---- PAINEL CENTRO (tabela) ----
        String[] colunas = {"ID", "Nome", "Data de Lançamento", "Categoria"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabela = new JTable(modeloTabela);
        tabela.setBackground(COR_CAMPO);
        tabela.setForeground(COR_TEXTO);
        tabela.setGridColor(COR_BORDA);
        tabela.setSelectionBackground(new Color(180, 180, 180));
        tabela.setSelectionForeground(Color.BLACK);
        tabela.setFont(new Font("Arial", Font.PLAIN, 13));
        tabela.setRowHeight(26);
        tabela.getTableHeader().setBackground(new Color(200, 200, 200));
        tabela.getTableHeader().setForeground(COR_TEXTO);
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(280);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(140);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(140);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, COR_BORDA));

        JPanel painelCentro = new JPanel(new BorderLayout());
        painelCentro.setBackground(COR_FUNDO);
        painelCentro.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        painelCentro.add(scroll, BorderLayout.CENTER);
        add(painelCentro, BorderLayout.CENTER);

        // ---- PAINEL RODAPÉ (botões) ----
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        painelRodape.setBackground(COR_FUNDO);
        painelRodape.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        btnAtualizar = botao("Atualizar");
        btnExcluir   = botao("Excluir");
        btnNovo      = botao("Cadastrar");

        painelRodape.add(btnAtualizar);
        painelRodape.add(btnExcluir);
        painelRodape.add(btnNovo);
        add(painelRodape, BorderLayout.SOUTH);
    }

    /** Cria um JLabel de rótulo padrão. */
    private JLabel rotulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(COR_LABEL);
        return lbl;
    }

    /** Cria um JTextField padrão. */
    private JTextField campo() {
        JTextField f = new JTextField();
        f.setBackground(COR_CAMPO);
        f.setForeground(COR_TEXTO);
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        f.setPreferredSize(new Dimension(100, 28));
        return f;
    }

    /** Cria um JButton padrão. */
    private JButton botao(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(COR_FUNDO);
        btn.setForeground(COR_TEXTO);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 35));
        return btn;
    }

    /**
     * Adiciona placeholder cinza a um campo de texto.
     *
     * @param campo campo de texto
     * @param texto texto de dica
     */
    private void adicionarPlaceholder(JTextField campo, String texto) {
        campo.setForeground(Color.GRAY);
        campo.setText(texto);
        campo.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (campo.getText().equals(texto) && campo.getForeground().equals(Color.GRAY)) {
                    campo.setText("");
                    campo.setForeground(COR_TEXTO);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setForeground(Color.GRAY);
                    campo.setText(texto);
                }
            }
        });
    }

    // =========================================================================
    // EVENTOS
    // =========================================================================
    private void configurarEventos() {
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0)
                preencherCampos(tabela.getSelectedRow());
        });

        txtFiltro.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { filtrar(); }
            public void removeUpdate(DocumentEvent e)  { filtrar(); }
            public void changedUpdate(DocumentEvent e) {}
        });

        btnBuscar.addActionListener(e -> filtrar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());
        btnNovo.addActionListener(e -> { new TelaCadastro().setVisible(true); dispose(); });
    }

    // =========================================================================
    // AÇÕES
    // =========================================================================

    /**
     * Carrega os vídeos na tabela com filtro opcional por categoria.
     *
     * @param categoria texto para filtro; null lista todos
     */
    private void carregarTabela(String categoria) {
        modeloTabela.setRowCount(0);
        List<Video> lista = (categoria == null || categoria.trim().isEmpty())
            ? dao.listarTodos()
            : dao.listarPorCategoria(categoria);
        for (Video v : lista)
            modeloTabela.addRow(new Object[]{ v.getId(), v.getNome(), v.getDataLancamento(), v.getCategoria() });
        limparCampos();
    }

    private void filtrar() {
        String f = txtFiltro.getForeground().equals(Color.GRAY) ? "" : txtFiltro.getText();
        carregarTabela(f);
    }

    /**
     * Preenche os campos com os dados da linha selecionada.
     *
     * @param linha índice da linha selecionada
     */
    private void preencherCampos(int linha) {
        txtId.setText(modeloTabela.getValueAt(linha, 0).toString());
        txtId.setForeground(COR_TEXTO);
        txtNome.setText(modeloTabela.getValueAt(linha, 1).toString());
        txtNome.setForeground(COR_TEXTO);
        txtData.setText(modeloTabela.getValueAt(linha, 2).toString());
        txtData.setForeground(COR_TEXTO);
        txtCategoria.setText(modeloTabela.getValueAt(linha, 3).toString());
        txtCategoria.setForeground(COR_TEXTO);
    }

    /**
     * Atualiza o registro selecionado com os dados dos campos.
     */
    private void atualizar() {
        if (txtId.getText().trim().isEmpty() || txtId.getForeground().equals(Color.GRAY)) {
            mostrarAviso("Selecione um registro na tabela para atualizar.");
            return;
        }
        Date data;
        try {
            data = Date.valueOf(txtData.getText().trim());
        } catch (Exception ex) {
            mostrarAviso("Data inválida. O formato esperado é AAAA-MM-DD.\nEx: 2023-06-15");
            return;
        }
        Video video = new Video(Integer.parseInt(txtId.getText().trim()),
            txtNome.getText().trim(), data, txtCategoria.getText().trim());
        if (dao.atualizar(video)) {
            JOptionPane.showMessageDialog(this, "Registro atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarTabela(null);
        } else {
            mostrarAviso("Não foi possível atualizar os dados! Por favor, verifique os valores digitados.");
        }
    }

    /**
     * Exclui o registro selecionado após confirmação.
     */
    private void excluir() {
        if (txtId.getText().trim().isEmpty() || txtId.getForeground().equals(Color.GRAY)) {
            mostrarAviso("Selecione um registro na tabela para excluir.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this,
            "Deseja realmente excluir \"" + txtNome.getText() + "\"?",
            "Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            if (dao.deletar(Integer.parseInt(txtId.getText().trim()))) {
                JOptionPane.showMessageDialog(this, "Registro excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarTabela(null);
            } else {
                mostrarAviso("Não foi possível excluir o registro! Por favor, tente novamente.");
            }
        }
    }

    private void limparCampos() {
        txtId.setText(""); txtId.setForeground(Color.GRAY);
        adicionarPlaceholder(txtNome,     "Selecione um registro");
        adicionarPlaceholder(txtData,     "Ex: 2023-06-15");
        adicionarPlaceholder(txtCategoria,"Ex: Ação");
    }

    private void mostrarAviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    // =========================================================================
    // MAIN
    // =========================================================================

    /**
     * Ponto de entrada pela tela de gerenciamento.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new TelaGerenciar().setVisible(true));
    }
}
