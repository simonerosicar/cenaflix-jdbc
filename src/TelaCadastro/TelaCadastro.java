package TelaCadastro;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import dao.VideoDAO;
import model.Video;
import view.TelaGerenciar;

/**
 * Tela principal de cadastro de vídeos do sistema Cenaflix.
 * Interface gráfica construída com Java Swing.
 */
public class TelaCadastro extends JFrame {

    private JTextField txtId, txtNome, txtData, txtCategoria;
    private JButton btnSalvar, btnLimpar, btnGerenciar;

    // Cores do tema Cenaflix
    private static final Color COR_FUNDO      = new Color(18, 18, 18);
    private static final Color COR_PAINEL     = new Color(30, 30, 30);
    private static final Color COR_CAMPO      = new Color(45, 45, 45);
    private static final Color COR_TEXTO      = new Color(230, 230, 230);
    private static final Color COR_LABEL      = new Color(180, 180, 180);
    private static final Color COR_SALVAR     = new Color(229, 9, 20);   // vermelho Netflix
    private static final Color COR_LIMPAR     = new Color(80, 80, 80);
    private static final Color COR_TITULO     = new Color(229, 9, 20);

    public TelaCadastro() {
        configurarJanela();
        construirInterface();
        configurarEventos();
    }

    // -------------------------------------------------------------------------
    // Configuração da janela
    // -------------------------------------------------------------------------
    private void configurarJanela() {
        setTitle("Cenaflix — Cadastro de Vídeos");
        setSize(500, 360);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COR_FUNDO);
    }

    // -------------------------------------------------------------------------
    // Construção da interface
    // -------------------------------------------------------------------------
    private void construirInterface() {

        // Título
        JLabel lblTitulo = new JLabel("CENAFLIX", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(COR_TITULO);
        lblTitulo.setBounds(0, 15, 500, 30);
        add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Cadastro de Vídeos", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSubtitulo.setForeground(COR_LABEL);
        lblSubtitulo.setBounds(0, 45, 500, 20);
        add(lblSubtitulo);

        // Separador visual
        JSeparator sep = new JSeparator();
        sep.setBounds(30, 72, 440, 2);
        sep.setForeground(COR_CAMPO);
        add(sep);

        // Campos
        txtId       = criarCampo("ID:",        80);
        txtNome     = criarCampo("Nome:",      120);
        txtData     = criarCampo("Data (DD/MM/AAAA):", 160);
        txtCategoria = criarCampo("Categoria:", 200);

        // Botões
        btnSalvar   = criarBotao("Salvar",    60,  COR_SALVAR);
        btnLimpar   = criarBotao("Limpar",    185, COR_LIMPAR);
        btnGerenciar = criarBotao("Gerenciar", 310, new Color(30, 100, 180));
    }

    /**
     * Cria um par label + campo de texto com o estilo padrão da tela.
     */
    private JTextField criarCampo(String labelTexto, int y) {
        JLabel lbl = new JLabel(labelTexto);
        lbl.setBounds(30, y, 150, 25);
        lbl.setForeground(COR_LABEL);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        add(lbl);

        JTextField campo = new JTextField();
        campo.setBounds(190, y, 220, 28);
        campo.setBackground(COR_CAMPO);
        campo.setForeground(COR_TEXTO);
        campo.setCaretColor(COR_TEXTO);
        campo.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));
        campo.setFont(new Font("Arial", Font.PLAIN, 13));
        add(campo);

        return campo;
    }

    /**
     * Cria um botão com o estilo padrão da tela.
     */
    private JButton criarBotao(String texto, int x, Color cor) {
        JButton btn = new JButton(texto);
        btn.setBounds(x, 270, 100, 35);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btn);
        return btn;
    }

    // -------------------------------------------------------------------------
    // Eventos
    // -------------------------------------------------------------------------
    private void configurarEventos() {

        // Capitalizar primeira letra do nome automaticamente
        txtNome.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { capitalizarPrimeiraLetra(); }
            public void removeUpdate(DocumentEvent e) {}
            public void changedUpdate(DocumentEvent e) {}

            private void capitalizarPrimeiraLetra() {
                SwingUtilities.invokeLater(() -> {
                    String texto = txtNome.getText();
                    if (!texto.isEmpty()) {
                        String corrigido = Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
                        if (!corrigido.equals(texto)) {
                            txtNome.setText(corrigido);
                            txtNome.setCaretPosition(corrigido.length());
                        }
                    }
                });
            }
        });

        // Máscara de data DD/MM/AAAA
        txtData.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { aplicarMascaraData(); }
            public void removeUpdate(DocumentEvent e) {}
            public void changedUpdate(DocumentEvent e) {}

            private void aplicarMascaraData() {
                SwingUtilities.invokeLater(() -> {
                    String texto = txtData.getText().replaceAll("[^0-9]", "");
                    StringBuilder formatado = new StringBuilder();
                    for (int i = 0; i < texto.length() && i < 8; i++) {
                        if (i == 2 || i == 4) formatado.append("/");
                        formatado.append(texto.charAt(i));
                    }
                    String resultado = formatado.toString();
                    if (!resultado.equals(txtData.getText())) {
                        txtData.setText(resultado);
                        txtData.setCaretPosition(resultado.length());
                    }
                });
            }
        });

        // Navegação por Enter entre campos
        txtId.addActionListener(e -> txtNome.requestFocus());
        txtNome.addActionListener(e -> txtData.requestFocus());
        txtData.addActionListener(e -> txtCategoria.requestFocus());
        txtCategoria.addActionListener(e -> salvar());

        // Botões
        btnSalvar.addActionListener(e -> salvar());
        btnLimpar.addActionListener(e -> limpar());
        btnGerenciar.addActionListener(e -> {
            new TelaGerenciar().setVisible(true);
            dispose();
        });
    }

    // -------------------------------------------------------------------------
    // Ações
    // -------------------------------------------------------------------------
    public void salvar() {

        // Validação: campos obrigatórios
        if (txtId.getText().trim().isEmpty() ||
            txtNome.getText().trim().isEmpty() ||
            txtData.getText().trim().isEmpty() ||
            txtCategoria.getText().trim().isEmpty()) {
            mostrarErro("Preencha todos os campos antes de salvar.");
            return;
        }

        // Validação: ID numérico
        int id;
        try {
            id = Integer.parseInt(txtId.getText().trim());
        } catch (NumberFormatException ex) {
            mostrarErro("O campo ID deve conter apenas números.");
            return;
        }

        // Validação: data no formato DD/MM/AAAA
        Date data;
        try {
            String[] partes = txtData.getText().trim().split("/");
            if (partes.length != 3 || partes[2].length() != 4) throw new Exception();
            String dataFormatada = partes[2] + "-" + partes[1] + "-" + partes[0];
            data = Date.valueOf(dataFormatada);
        } catch (Exception ex) {
            mostrarErro("Data inválida. Use o formato DD/MM/AAAA.\nExemplo: 15/06/2023");
            return;
        }

        // Persistência
        Video video = new Video(id, txtNome.getText().trim(), data, txtCategoria.getText().trim());
        VideoDAO dao = new VideoDAO();
        boolean sucesso = dao.inserir(video);

        if (sucesso) {
            JOptionPane.showMessageDialog(this,
                "\"" + video.getNome() + "\" cadastrado com sucesso!",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limpar();
        } else {
            mostrarErro("Não foi possível salvar. Verifique a conexão com o banco de dados.");
        }
    }

    public void limpar() {
        txtId.setText("");
        txtNome.setText("");
        txtData.setText("");
        txtCategoria.setText("");
        txtId.requestFocus();
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    // -------------------------------------------------------------------------
    // Main
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        // Aparência nativa do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new TelaCadastro().setVisible(true));
    }
}
