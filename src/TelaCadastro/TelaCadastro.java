package TelaCadastro;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.sql.Date;
import dao.VideoDAO;
import model.Video;

/**
 * Tela de cadastro de vídeos da plataforma Cenaflix.
 * Permite inserir um novo vídeo no banco de dados
 * ATIVIDADE1, com validação de campos e formatação
 * automática da data no padrão DD/MM/YYYY.
 *
 * @author SeuNome
 * @version 2.0
 * @see dao.VideoDAO
 * @see model.Video
 */
public class TelaCadastro extends JFrame {

    /** Campo para o identificador numérico do vídeo */
    JTextField txtId;

    /** Campo para o nome do vídeo */
    JTextField txtNome;

    /** Campo para a data de lançamento */
    JTextField txtData;

    /** Campo para a categoria do vídeo */
    JTextField txtCategoria;

    /** Botão para salvar o cadastro */
    JButton btnSalvar;

    /** Botão para limpar os campos */
    JButton btnLimpar;

    /** Botão para abrir a tela de gerenciamento */
    JButton btnGerenciar;

    /**
     * Construtor que inicializa e configura todos os
     * componentes visuais da tela de cadastro.
     */
    public TelaCadastro() {

        setTitle("Cenaflix — Cadastro de Vídeos");
        setSize(420, 360);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblTitulo = new JLabel("Cadastro de Vídeos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBounds(0, 10, 420, 30);
        add(lblTitulo);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(20, 55, 130, 25);
        add(lblId);
        txtId = new JTextField();
        txtId.setBounds(160, 55, 200, 25);
        add(txtId);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(20, 95, 130, 25);
        add(lblNome);
        txtNome = new JTextField();
        txtNome.setBounds(160, 95, 200, 25);
        add(txtNome);

        txtNome.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    String texto = txtNome.getText();
                    if (!texto.isEmpty()) {
                        String corrigido = texto.substring(0, 1).toUpperCase()
                                         + texto.substring(1);
                        if (!corrigido.equals(texto)) {
                            txtNome.setText(corrigido);
                            txtNome.setCaretPosition(corrigido.length());
                        }
                    }
                });
            }
            public void removeUpdate(DocumentEvent e) {}
            public void changedUpdate(DocumentEvent e) {}
        });

        JLabel lblData = new JLabel("Data (DD/MM/YYYY):");
        lblData.setBounds(20, 135, 130, 25);
        add(lblData);
        txtData = new JTextField();
        txtData.setBounds(160, 135, 200, 25);
        add(txtData);

        txtData.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
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
            public void removeUpdate(DocumentEvent e) {}
            public void changedUpdate(DocumentEvent e) {}
        });

        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setBounds(20, 175, 130, 25);
        add(lblCategoria);
        txtCategoria = new JTextField();
        txtCategoria.setBounds(160, 175, 200, 25);
        add(txtCategoria);

        btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(30, 230, 100, 30);
        btnSalvar.setBackground(new Color(50, 150, 50));
        btnSalvar.setForeground(Color.WHITE);
        add(btnSalvar);

        btnLimpar = new JButton("Limpar");
        btnLimpar.setBounds(155, 230, 100, 30);
        btnLimpar.setBackground(new Color(200, 50, 50));
        btnLimpar.setForeground(Color.WHITE);
        add(btnLimpar);

        btnGerenciar = new JButton("Gerenciar");
        btnGerenciar.setBounds(275, 230, 100, 30);
        btnGerenciar.setBackground(new Color(30, 100, 200));
        btnGerenciar.setForeground(Color.WHITE);
        add(btnGerenciar);

        txtId.addActionListener(e -> txtNome.requestFocus());
        txtNome.addActionListener(e -> txtData.requestFocus());
        txtData.addActionListener(e -> txtCategoria.requestFocus());
        txtCategoria.addActionListener(e -> salvar());

        btnSalvar.addActionListener(e -> salvar());
        btnLimpar.addActionListener(e -> limpar());
        btnGerenciar.addActionListener(e -> abrirGerenciar());
    }

    /**
     * Valida os campos, cria um objeto Video e chama o DAO
     * para inserção no banco. Exibe alerta de sucesso ou erro.
     */
    public void salvar() {

        if (txtId.getText().trim().isEmpty()
                || txtNome.getText().trim().isEmpty()
                || txtData.getText().trim().isEmpty()
                || txtCategoria.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "Preencha todos os campos!",
                "Atenção",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(txtId.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                "O campo ID deve ser numérico!",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date data;
        try {
            String[] partes = txtData.getText().trim().split("/");
            String dataFormatada = partes[2] + "-" + partes[1] + "-" + partes[0];
            data = Date.valueOf(dataFormatada);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                "Data inválida! Use DD/MM/YYYY.\nEx: 10/10/2020",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        Video video = new Video();
        video.setId(id);
        video.setNome(txtNome.getText().trim());
        video.setDataLancamento(data);
        video.setCategoria(txtCategoria.getText().trim());

        VideoDAO dao = new VideoDAO();
        boolean sucesso = dao.inserir(video);

        if (sucesso) {
            JOptionPane.showMessageDialog(null,
                "✅ " + txtNome.getText().trim() + " cadastrado com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
            limpar();
        } else {
            JOptionPane.showMessageDialog(null,
                "❌ Não foi possível cadastrar!\n"
                + "O ID " + id + " já existe no banco.\n"
                + "Por favor, use um ID diferente.",
                "Erro ao cadastrar",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Limpa todos os campos do formulário e
     * retorna o foco ao campo ID.
     */
    public void limpar() {
        txtId.setText("");
        txtNome.setText("");
        txtData.setText("");
        txtCategoria.setText("");
        txtId.requestFocus();
    }

    /**
     * Abre a tela de gerenciamento e fecha a tela atual.
     */
    public void abrirGerenciar() {
        new TelaGerenciar().setVisible(true);
        this.dispose();
    }

    /**
     * Ponto de entrada da aplicação Cenaflix.
     *
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) {
        new TelaCadastro().setVisible(true);
    }
}