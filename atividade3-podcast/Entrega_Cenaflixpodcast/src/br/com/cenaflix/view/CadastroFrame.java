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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Tela de cadastro de podcasts.
 * <p>
 * Acessível para <b>Administrador</b> e <b>Operador</b>.
 *
 * @author Seu Nome
 * @version 1.0
 */
public class CadastroFrame extends JFrame {

    private final ListagemFrame telaListagem;
    private JTextField txtProdutor;
    private JTextField txtNomeEpisodio;
    private JTextField txtNumeroEpisodio;
    private JTextField txtDuracao;
    private JTextField txtUrlRepositorio;

    // Placeholders de cada campo
    private static final String PH_PRODUTOR   = "Ex: Spotify, Podcast Brasileiro...";
    private static final String PH_NOME       = "Ex: Episódio 1: Introdução";
    private static final String PH_NUMERO     = "Ex: 1";
    private static final String PH_DURACAO    = "Ex: 45";
    private static final String PH_URL        = "Ex: https://spotify.com/podcast/ep1";

    /**
     * Constrói e exibe a tela de cadastro.
     *
     * @param usuario      usuário logado
     * @param telaListagem referência à listagem para atualizar após cadastro
     */
    public CadastroFrame(Usuario usuario, ListagemFrame telaListagem) {
        this.telaListagem = telaListagem;

        setTitle("Cenaflix - Cadastro de Podcast");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 720);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(210, 210, 210));

        // ===== TOPO =====
        JPanel painelTitulo = new JPanel();
        painelTitulo.setLayout(new BoxLayout(painelTitulo, BoxLayout.Y_AXIS));
        painelTitulo.setBackground(new Color(210, 210, 210));
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));

        JLabel lblCenaflix = new JLabel("CENAFLIX");
        lblCenaflix.setFont(new Font("Arial", Font.BOLD, 48));
        lblCenaflix.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(lblCenaflix);

        JLabel lblCadastro = new JLabel("CADASTRAR PODCAST");
        lblCadastro.setFont(new Font("Arial", Font.BOLD, 22));
        lblCadastro.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(lblCadastro);

        painel.add(painelTitulo, BorderLayout.NORTH);

        // ===== CAMPOS =====
        JPanel painelCentral = new JPanel(new GridBagLayout());
        painelCentral.setBackground(new Color(210, 210, 210));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;

        adicionarLabel(painelCentral, gbc, "Produtor", 0);
        txtProdutor = adicionarCampo(painelCentral, gbc, 1, 320, 32, PH_PRODUTOR);

        adicionarLabel(painelCentral, gbc, "Nome do Episódio", 2);
        txtNomeEpisodio = adicionarCampo(painelCentral, gbc, 3, 320, 32, PH_NOME);

        adicionarLabel(painelCentral, gbc, "Nº do Episódio", 4);
        txtNumeroEpisodio = adicionarCampo(painelCentral, gbc, 5, 100, 32, PH_NUMERO);

        adicionarLabel(painelCentral, gbc, "Duração (minutos)", 6);
        txtDuracao = adicionarCampo(painelCentral, gbc, 7, 320, 32, PH_DURACAO);

        adicionarLabel(painelCentral, gbc, "URL do Repositório", 8);
        txtUrlRepositorio = adicionarCampo(painelCentral, gbc, 9, 320, 32, PH_URL);

        // Navegação Enter entre campos
        txtProdutor.addKeyListener(enterFocus(txtNomeEpisodio));
        txtNomeEpisodio.addKeyListener(enterFocus(txtNumeroEpisodio));
        txtNumeroEpisodio.addKeyListener(enterFocus(txtDuracao));
        txtDuracao.addKeyListener(enterFocus(txtUrlRepositorio));
        txtUrlRepositorio.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) salvarPodcast();
            }
        });

        painel.add(painelCentral, BorderLayout.CENTER);

        // ===== BOTÕES =====
        // Cadastrar (esquerda) | Restaurar (centro) | Ver Listagem (direita)
        JPanel painelBotoes = new JPanel(new BorderLayout());
        painelBotoes.setBackground(new Color(210, 210, 210));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        JButton btnCadastrar = criarBotao("Cadastrar");
        btnCadastrar.setToolTipText("Salvar o novo podcast (Enter no último campo)");
        btnCadastrar.addActionListener(e -> salvarPodcast());

        JButton btnRestaurar = criarBotao("Restaurar");
        btnRestaurar.setToolTipText("Limpar todos os campos");
        btnRestaurar.addActionListener(e -> limparCampos());

        JButton btnVerListagem = criarBotao("Ver Listagem");
        btnVerListagem.setToolTipText("Voltar à tela de listagem");
        btnVerListagem.addActionListener(e -> this.dispose());

        JPanel esquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        esquerda.setBackground(new Color(210, 210, 210));
        esquerda.add(btnCadastrar);

        JPanel centro = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centro.setBackground(new Color(210, 210, 210));
        centro.add(btnRestaurar);

        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        direita.setBackground(new Color(210, 210, 210));
        direita.add(btnVerListagem);

        painelBotoes.add(esquerda, BorderLayout.WEST);
        painelBotoes.add(centro, BorderLayout.CENTER);
        painelBotoes.add(direita, BorderLayout.EAST);

        painel.add(painelBotoes, BorderLayout.SOUTH);

        add(painel);
        setVisible(true);
    }

    // ===== Helpers de UI =====

    private void adicionarLabel(JPanel painel, GridBagConstraints gbc,
                                String texto, int linha) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.insets = new Insets(12, 0, 2, 0);
        painel.add(label, gbc);
    }

    private JTextField adicionarCampo(JPanel painel, GridBagConstraints gbc,
                                      int linha, int largura, int altura,
                                      String placeholder) {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Arial", Font.PLAIN, 13));
        campo.setPreferredSize(new Dimension(largura, altura));
        campo.setBackground(Color.WHITE);
        campo.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        aplicarPlaceholder(campo, placeholder);
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.insets = new Insets(0, 0, 0, 0);
        painel.add(campo, gbc);
        return campo;
    }

    private JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setBackground(new Color(230, 230, 230));
        btn.setForeground(Color.BLACK);
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        btn.setFocusPainted(false);
        return btn;
    }

    /**
     * Aplica comportamento de placeholder (texto de dica) a um campo de texto.
     *
     * @param campo       campo de texto
     * @param placeholder texto exibido quando o campo está vazio
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

    private java.awt.event.KeyAdapter enterFocus(JTextField proximo) {
        return new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) proximo.requestFocus();
            }
        };
    }

    // ===== Lógica =====

    /**
     * Retorna o valor real do campo, ignorando o placeholder.
     */
    private String valor(JTextField campo, String placeholder) {
        String txt = campo.getText().trim();
        return txt.equals(placeholder) ? "" : txt;
    }

    /**
     * Valida os campos e persiste o podcast via {@link PodcastDAO}.
     */
    private void salvarPodcast() {
        String produtor  = valor(txtProdutor,       PH_PRODUTOR);
        String nome      = valor(txtNomeEpisodio,   PH_NOME);
        String numero    = valor(txtNumeroEpisodio, PH_NUMERO);
        String duracao   = valor(txtDuracao,        PH_DURACAO);
        String url       = valor(txtUrlRepositorio, PH_URL);

        if (produtor.isEmpty() || nome.isEmpty() || numero.isEmpty()
                || duracao.isEmpty() || url.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível inserir os dados! Por favor, verifique os valores digitados!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int numeroEpisodio = Integer.parseInt(numero);
            int duracaoMin     = Integer.parseInt(duracao);

            Podcast podcast = new Podcast(produtor, nome, numeroEpisodio, duracaoMin, url);

            if (PodcastDAO.inserir(podcast)) {
                JOptionPane.showMessageDialog(this,
                        "Podcast cadastrado com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                telaListagem.atualizarTabela();
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao cadastrar podcast!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Nº do episódio e duração devem ser números inteiros!",
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Restaura todos os campos para o estado inicial (com placeholders).
     */
    private void limparCampos() {
        reporPlaceholder(txtProdutor,       PH_PRODUTOR);
        reporPlaceholder(txtNomeEpisodio,   PH_NOME);
        reporPlaceholder(txtNumeroEpisodio, PH_NUMERO);
        reporPlaceholder(txtDuracao,        PH_DURACAO);
        reporPlaceholder(txtUrlRepositorio, PH_URL);
        txtProdutor.requestFocus();
    }

    private void reporPlaceholder(JTextField campo, String placeholder) {
        campo.setText(placeholder);
        campo.setForeground(new Color(160, 160, 160));
    }
}
