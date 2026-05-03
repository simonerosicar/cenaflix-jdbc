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
 * Tela de cadastro de vídeos do sistema Cenaflix.
 * Sprint 1 — Atividade 1.
 *
 * @author Simone Cardozo
 * @version 1.0
 */
public class TelaCadastro extends JFrame {

    private JTextField txtId, txtNome, txtData, txtCategoria;
    private JButton btnSalvar, btnLimpar, btnGerenciar;

    // Cores do tema Cenaflix (wireframe)
    private static final Color COR_FUNDO  = new Color(220, 220, 220);
    private static final Color COR_CAMPO  = Color.WHITE;
    private static final Color COR_TEXTO  = Color.BLACK;
    private static final Color COR_LABEL  = Color.BLACK;
    private static final Color COR_BORDA  = new Color(180, 180, 180);

    /**
     * Construtor da tela de cadastro.
     */
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
        setSize(500, 450);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(COR_FUNDO);
    }

    // -------------------------------------------------------------------------
    // Construção da interface
    // -------------------------------------------------------------------------
    private void construirInterface() {

        // Título CENAFLIX
        JLabel lblTitulo = new JLabel("CENAFLIX", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(COR_TEXTO);
        lblTitulo.setBounds(0, 18, 500, 35);
        add(lblTitulo);

        // Subtítulo
        JLabel lblSub = new JLabel("CADASTRO DE VÍDEOS", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.BOLD, 14));
        lblSub.setForeground(COR_TEXTO);
        lblSub.setBounds(0, 55, 500, 22);
        add(lblSub);

        // Campos
        txtId        = criarCampo("ID:",                  95);
        txtNome      = criarCampo("Nome:",                145);
        txtData      = criarCampo("Data (DD/MM/AAAA):",  195);
        txtCategoria = criarCampo("Categoria:",           245);

        // Placeholders
        adicionarPlaceholder(txtId,        "Ex: 1");
        adicionarPlaceholder(txtNome,      "Ex: Interestelar");
        adicionarPlaceholder(txtData,      "Ex: 10/11/2014");
        adicionarPlaceholder(txtCategoria, "Ex: Ficção Científica");

        // Botões — mais afastados do último campo
        btnSalvar    = criarBotao("Salvar",       60,  345);
        btnLimpar    = criarBotao("Limpar",       195, 345);
        btnGerenciar = criarBotao("Ver Listagem", 330, 345);
    }

    /**
     * Adiciona texto placeholder (dica) a um campo de texto.
     * O texto aparece em cinza quando o campo está vazio e some ao digitar.
     *
     * @param campo  campo de texto
     * @param texto  texto de dica
     */
    private void adicionarPlaceholder(JTextField campo, String texto) {
        campo.setForeground(Color.GRAY);
        campo.setText(texto);

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(texto)) {
                    campo.setText("");
                    campo.setForeground(COR_TEXTO);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setForeground(Color.GRAY);
                    campo.setText(texto);
                }
            }
        });
    }

    /**
     * Cria um par label + campo de texto com o estilo do wireframe.
     *
     * @param labelTexto texto do rótulo
     * @param y          posição vertical
     * @return o campo de texto criado
     */
    private JTextField criarCampo(String labelTexto, int y) {
        JLabel lbl = new JLabel(labelTexto);
        lbl.setBounds(80, y, 160, 22);
        lbl.setForeground(COR_LABEL);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        add(lbl);

        JTextField campo = new JTextField();
        campo.setBounds(80, y + 22, 340, 28);
        campo.setBackground(COR_CAMPO);
        campo.setForeground(COR_TEXTO);
        campo.setFont(new Font("Arial", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createLineBorder(COR_BORDA));
        add(campo);

        return campo;
    }

    /**
     * Cria um botão com o estilo do wireframe.
     *
     * @param texto texto do botão
     * @param x     posição horizontal
     * @param y     posição vertical
     * @return o botão criado
     */
    private JButton criarBotao(String texto, int x, int y) {
        JButton btn = new JButton(texto);
        btn.setBounds(x, y, 120, 35);
        btn.setBackground(COR_FUNDO);
        btn.setForeground(COR_TEXTO);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btn);
        return btn;
    }

    // -------------------------------------------------------------------------
    // Eventos
    // -------------------------------------------------------------------------
    private void configurarEventos() {

        // Capitalizar primeira letra do nome
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

        // Navegação por Enter
        txtId.addActionListener(e -> txtNome.requestFocus());
        txtNome.addActionListener(e -> txtData.requestFocus());
        txtData.addActionListener(e -> txtCategoria.requestFocus());
        txtCategoria.addActionListener(e -> salvar());

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

    private String getValor(JTextField campo) {
        String texto = campo.getText().trim();
        // Ignora se for placeholder (texto cinza)
        if (campo.getForeground().equals(Color.GRAY)) return "";
        return texto;
    }

    /**
     * Valida os campos e salva o vídeo no banco de dados.
     */
    public void salvar() {
        String id       = getValor(txtId);
        String nome     = getValor(txtNome);
        String data     = getValor(txtData);
        String categoria = getValor(txtCategoria);

        if (id.isEmpty() || nome.isEmpty() || data.isEmpty() || categoria.isEmpty()) {
            mostrarErro("Preencha todos os campos antes de salvar.");
            return;
        }

        int idInt;
        try {
            idInt = Integer.parseInt(id);
        } catch (NumberFormatException ex) {
            mostrarErro("O campo ID deve conter apenas números.");
            return;
        }

        Date dataSQL;
        try {
            String[] partes = data.split("/");
            if (partes.length != 3 || partes[2].length() != 4) throw new Exception();
            dataSQL = Date.valueOf(partes[2] + "-" + partes[1] + "-" + partes[0]);
        } catch (Exception ex) {
            mostrarErro("Data inválida. Use o formato DD/MM/AAAA.\nExemplo: 15/06/2023");
            return;
        }

        Video video = new Video(idInt, nome, dataSQL, categoria);
        VideoDAO dao = new VideoDAO();
        boolean sucesso = dao.inserir(video);

        if (sucesso) {
            JOptionPane.showMessageDialog(this,
                "\"" + video.getNome() + "\" cadastrado com sucesso!",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limpar();
        } else {
            mostrarErro("Não foi possível inserir os dados! Por favor, verifique os valores digitados.");
        }
    }

    /**
     * Limpa todos os campos da tela e restaura os placeholders.
     */
    public void limpar() {
        adicionarPlaceholder(txtId,        "Ex: 1");
        adicionarPlaceholder(txtNome,      "Ex: Interestelar");
        adicionarPlaceholder(txtData,      "Ex: 10/11/2014");
        adicionarPlaceholder(txtCategoria, "Ex: Ficção Científica");
        txtId.requestFocus();
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    // -------------------------------------------------------------------------
    // Main
    // -------------------------------------------------------------------------

    /**
     * Ponto de entrada da aplicação.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new TelaCadastro().setVisible(true));
    }
}
