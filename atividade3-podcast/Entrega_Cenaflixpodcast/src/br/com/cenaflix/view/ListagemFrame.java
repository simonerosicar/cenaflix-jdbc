package br.com.cenaflix.view;

import br.com.cenaflix.dao.PodcastDAO;
import br.com.cenaflix.model.Podcast;
import br.com.cenaflix.model.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Tela de listagem de podcasts com filtro por produtor.
 * <p>
 * Permissões:
 * <ul>
 *   <li><b>Administrador</b> – Cadastrar, Deletar e Sair</li>
 *   <li><b>Operador</b>      – Cadastrar e Sair</li>
 *   <li><b>Usuário</b>       – Sair</li>
 * </ul>
 *
 * @author Seu Nome
 * @version 1.0
 */
public class ListagemFrame extends JFrame {

    private final Usuario usuarioAtual;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JTextField txtFiltroProdutor;

    private static final String PH_FILTRO = "Digite o produtor...";

    /**
     * Constrói e exibe a tela de listagem.
     *
     * @param usuario usuário autenticado
     */
    public ListagemFrame(Usuario usuario) {
        this.usuarioAtual = usuario;

        setTitle("Cenaflix - Listagem de Podcasts");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 680);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(210, 210, 210));

        // ===== TOPO: título + filtro =====
        JPanel painelNorth = new JPanel(new BorderLayout());
        painelNorth.setBackground(new Color(210, 210, 210));

        // Título
        JPanel painelTitulo = new JPanel();
        painelTitulo.setLayout(new BoxLayout(painelTitulo, BoxLayout.Y_AXIS));
        painelTitulo.setBackground(new Color(210, 210, 210));
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(25, 20, 10, 20));

        JLabel lblCenaflix = new JLabel("CENAFLIX");
        lblCenaflix.setFont(new Font("Arial", Font.BOLD, 48));
        lblCenaflix.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(lblCenaflix);

        JLabel lblListagem = new JLabel("LISTAGEM");
        lblListagem.setFont(new Font("Arial", Font.BOLD, 28));
        lblListagem.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(lblListagem);

        painelNorth.add(painelTitulo, BorderLayout.NORTH);

        // Filtro — label + campo + Enter para filtrar
        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        painelFiltro.setBackground(new Color(210, 210, 210));
        painelFiltro.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));

        JLabel lblProdutor = new JLabel("Pesquisar podcast por produtor:");
        lblProdutor.setFont(new Font("Arial", Font.BOLD, 14));
        painelFiltro.add(lblProdutor);

        txtFiltroProdutor = new JTextField();
        txtFiltroProdutor.setFont(new Font("Arial", Font.PLAIN, 13));
        txtFiltroProdutor.setPreferredSize(new Dimension(260, 34));
        txtFiltroProdutor.setBackground(Color.WHITE);
        txtFiltroProdutor.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        txtFiltroProdutor.setToolTipText("Digite o produtor e pressione Enter para filtrar");
        aplicarPlaceholder(txtFiltroProdutor, PH_FILTRO);
        txtFiltroProdutor.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) aplicarFiltro();
            }
        });
        painelFiltro.add(txtFiltroProdutor);

        // Botão Filtrar
        JButton btnFiltrar = criarBotao("Filtrar", 90, 34);
        btnFiltrar.setToolTipText("Filtrar por produtor (ou pressione Enter no campo)");
        btnFiltrar.addActionListener(e -> aplicarFiltro());
        painelFiltro.add(btnFiltrar);

        // Botão Limpar filtro
        JButton btnLimpar = criarBotao("Limpar", 90, 34);
        btnLimpar.setToolTipText("Remover filtro e listar todos");
        btnLimpar.addActionListener(e -> {
            reporPlaceholder(txtFiltroProdutor, PH_FILTRO);
            carregarTodos();
        });
        painelFiltro.add(btnLimpar);

        painelNorth.add(painelFiltro, BorderLayout.SOUTH);
        painel.add(painelNorth, BorderLayout.NORTH);

        // ===== TABELA =====
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        painelCentral.setBackground(new Color(210, 210, 210));

        String[] colunas = {"ID", "Produtor", "Nome do Episódio", "Nº Episódio", "Duração (min)", "URL do Repositório"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabela = new JTable(modeloTabela);
        tabela.setFont(new Font("Arial", Font.PLAIN, 13));
        tabela.setRowHeight(30);
        tabela.setBackground(Color.WHITE);
        tabela.setGridColor(new Color(200, 200, 200));
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(new Color(220, 220, 220));
        tabela.setSelectionBackground(new Color(173, 216, 230));

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }

        // Larguras das colunas
        tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(90);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(280);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160), 1));
        painelCentral.add(scrollPane, BorderLayout.CENTER);
        painel.add(painelCentral, BorderLayout.CENTER);

        // ===== BOTÕES DE AÇÃO =====
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        painelBotoes.setBackground(new Color(210, 210, 210));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));

        configurarBotoesPorPermissao(painelBotoes);

        painel.add(painelBotoes, BorderLayout.SOUTH);

        add(painel);
        carregarTodos();
        setVisible(true);
    }

    /**
     * Adiciona os botões conforme o tipo de usuário logado.
     *
     * @param painelBotoes painel de destino
     */
    private void configurarBotoesPorPermissao(JPanel painelBotoes) {
        String tipo = usuarioAtual.getTipoUsuario();

        if (tipo.equals("Administrador") || tipo.equals("Operador")) {
            JButton btnCadastro = criarBotao("Cadastrar", 130, 40);
            btnCadastro.setToolTipText("Cadastrar um novo podcast");
            btnCadastro.addActionListener(e -> abrirCadastro());
            painelBotoes.add(btnCadastro);
        }

        if (tipo.equals("Administrador")) {
            JButton btnDeletar = criarBotao("Deletar", 130, 40);
            btnDeletar.setToolTipText("Deletar o podcast selecionado");
            btnDeletar.addActionListener(e -> deletarPodcast());
            painelBotoes.add(btnDeletar);
        }

        JButton btnSair = criarBotao("Sair", 130, 40);
        btnSair.setToolTipText("Fazer logout e voltar ao login");
        btnSair.addActionListener(e -> sair());
        painelBotoes.add(btnSair);
    }

    // ===== Helpers de UI =====

    private JButton criarBotao(String texto, int largura, int altura) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(largura, altura));
        btn.setBackground(new Color(230, 230, 230));
        btn.setForeground(Color.BLACK);
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        btn.setFocusPainted(false);
        return btn;
    }

    /**
     * Aplica comportamento de placeholder a um campo de texto.
     *
     * @param campo       campo de texto
     * @param placeholder texto de dica
     */
    private void aplicarPlaceholder(JTextField campo, String placeholder) {
        campo.setText(placeholder);
        campo.setForeground(new Color(160, 160, 160));
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    reporPlaceholder(campo, placeholder);
                }
            }
        });
    }

    private void reporPlaceholder(JTextField campo, String placeholder) {
        campo.setText(placeholder);
        campo.setForeground(new Color(160, 160, 160));
    }

    // ===== Lógica =====

    /**
     * Carrega todos os podcasts na tabela.
     */
    private void carregarTodos() {
        modeloTabela.setRowCount(0);
        try {
            List<Podcast> podcasts = PodcastDAO.obterTodos();
            for (Podcast p : podcasts) {
                modeloTabela.addRow(new Object[]{
                    p.getId(), p.getProdutor(), p.getNomeEpisodio(),
                    p.getNumeroEpisodio(), p.getDuracao(), p.getUrlRepositorio()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar podcasts: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Filtra os podcasts pelo produtor digitado.
     */
    private void aplicarFiltro() {
        String produtor = txtFiltroProdutor.getText().trim();

        if (produtor.isEmpty() || produtor.equals(PH_FILTRO)) {
            JOptionPane.showMessageDialog(this,
                    "Digite o nome do produtor para filtrar!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modeloTabela.setRowCount(0);
        try {
            List<Podcast> podcasts = PodcastDAO.obterPorProdutor(produtor);
            if (podcasts.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Nenhum podcast encontrado para: " + produtor,
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                carregarTodos();
                return;
            }
            for (Podcast p : podcasts) {
                modeloTabela.addRow(new Object[]{
                    p.getId(), p.getProdutor(), p.getNomeEpisodio(),
                    p.getNumeroEpisodio(), p.getDuracao(), p.getUrlRepositorio()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao filtrar: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abre a tela de cadastro.
     */
    private void abrirCadastro() {
        new CadastroFrame(usuarioAtual, this);
    }

    /**
     * Deleta o podcast selecionado após confirmação.
     */
    private void deletarPodcast() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um podcast para deletar!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente deletar este podcast?",
                "Confirmação", JOptionPane.YES_NO_OPTION);

        if (opcao == JOptionPane.YES_OPTION) {
            int id = (Integer) modeloTabela.getValueAt(linha, 0);
            if (PodcastDAO.deletar(id)) {
                JOptionPane.showMessageDialog(this,
                        "Podcast deletado com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarTodos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao deletar podcast!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Confirma logout e retorna à tela de login.
     */
    private void sair() {
        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente sair?",
                "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            new LoginFrame();
            this.dispose();
        }
    }

    /**
     * Atualiza a tabela. Chamado pela {@link CadastroFrame} após novo cadastro.
     */
    public void atualizarTabela() {
        carregarTodos();
    }
}
