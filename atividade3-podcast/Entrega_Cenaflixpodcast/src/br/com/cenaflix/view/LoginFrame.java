package br.com.cenaflix.view;

import br.com.cenaflix.dao.UsuarioDAO;
import br.com.cenaflix.model.Usuario;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Tela de login do sistema Cenaflix.
 *
 * @author Seu Nome
 * @version 1.0
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtSenha;

    /**
     * Constrói e exibe a tela de login.
     */
    public LoginFrame() {
        setTitle("Cenaflix - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(210, 210, 210));

        GridBagConstraints gbc = new GridBagConstraints();

        // ===== TÍTULO =====
        JLabel lblCenaflix = new JLabel("CENAFLIX");
        lblCenaflix.setFont(new Font("Arial", Font.BOLD, 64));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 50, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(lblCenaflix, gbc);

        // ===== LABEL LOGIN =====
        JLabel lblUsuario = new JLabel("Login:");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 4, 0);
        gbc.anchor = GridBagConstraints.WEST;
        painel.add(lblUsuario, gbc);

        // ===== CAMPO LOGIN com placeholder =====
        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
        txtUsuario.setPreferredSize(new Dimension(420, 42));
        txtUsuario.setBackground(Color.WHITE);
        txtUsuario.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        aplicarPlaceholder(txtUsuario, "Digite seu usuário");
        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) txtSenha.requestFocus();
            }
        });
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(txtUsuario, gbc);

        // ===== LABEL SENHA =====
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 4, 0);
        gbc.anchor = GridBagConstraints.WEST;
        painel.add(lblSenha, gbc);

        // ===== CAMPO SENHA com placeholder =====
        txtSenha = new JPasswordField();
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 16));
        txtSenha.setPreferredSize(new Dimension(420, 42));
        txtSenha.setBackground(Color.WHITE);
        txtSenha.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        aplicarPlaceholderSenha(txtSenha, "Digite sua senha");
        txtSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) fazerLogin(null);
            }
        });
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 40, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(txtSenha, gbc);

        // ===== BOTÃO LOGIN =====
        JButton btnEntrar = new JButton("LOGIN");
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 18));
        btnEntrar.setPreferredSize(new Dimension(200, 45));
        btnEntrar.setBackground(new Color(230, 230, 230));
        btnEntrar.setForeground(Color.BLACK);
        btnEntrar.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntrar.addActionListener(this::fazerLogin);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(btnEntrar, gbc);

        add(painel);
        setVisible(true);
    }

    /**
     * Adiciona comportamento de placeholder a um JTextField.
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
                    campo.setText(placeholder);
                    campo.setForeground(new Color(160, 160, 160));
                }
            }
        });
    }

    /**
     * Adiciona comportamento de placeholder a um JPasswordField.
     *
     * @param campo       campo de senha
     * @param placeholder texto de dica
     */
    private void aplicarPlaceholderSenha(JPasswordField campo, String placeholder) {
        campo.setText(placeholder);
        campo.setEchoChar((char) 0); // mostra o placeholder como texto
        campo.setForeground(new Color(160, 160, 160));
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(campo.getPassword()).equals(placeholder)) {
                    campo.setText("");
                    campo.setEchoChar('•');
                    campo.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getPassword().length == 0) {
                    campo.setText(placeholder);
                    campo.setEchoChar((char) 0);
                    campo.setForeground(new Color(160, 160, 160));
                }
            }
        });
    }

    /**
     * Realiza a autenticação e abre a listagem em caso de sucesso.
     *
     * @param evt evento de ação
     */
    private void fazerLogin(ActionEvent evt) {
        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        // Ignora se ainda está com placeholder
        if (usuario.equals("Digite seu usuário")) usuario = "";
        if (senha.equals("Digite sua senha")) senha = "";

        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, preencha usuário e senha!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioLogado = UsuarioDAO.autenticar(usuario, senha);

        if (usuarioLogado != null) {
            JOptionPane.showMessageDialog(this,
                    "Olá " + usuarioLogado.getUsuario()
                    + ", sua permissão é de " + usuarioLogado.getTipoUsuario()
                    + ". Seja bem-vindo!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            new ListagemFrame(usuarioLogado);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Usuário ou senha incorretos!",
                    "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
            txtSenha.setText("");
            txtSenha.setEchoChar((char) 0);
            txtSenha.setText("Digite sua senha");
            txtSenha.setForeground(new Color(160, 160, 160));
            txtUsuario.requestFocus();
        }
    }
}
