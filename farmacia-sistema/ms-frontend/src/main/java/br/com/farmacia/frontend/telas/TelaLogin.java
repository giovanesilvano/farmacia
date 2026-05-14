package br.com.farmacia.frontend.telas;

import br.com.farmacia.frontend.util.Cores;
import br.com.farmacia.frontend.util.HttpClient;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TelaLogin extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JLabel lblMensagem;

    public TelaLogin() {
        setTitle("Farmácia - Login");
        setSize(420, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        construirTela();
    }

    private void construirTela() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Cores.BRANCO);

        JPanel topo = new JPanel();
        topo.setBackground(Cores.VERDE_FARMACIA);
        topo.setPreferredSize(new Dimension(420, 120));
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel icone = new JLabel("💊", SwingConstants.CENTER);
        icone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        icone.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel("Sistema Farmácia", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Cores.BRANCO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        topo.add(icone);
        topo.add(titulo);

        JPanel centro = new JPanel();
        centro.setBackground(Cores.BRANCO);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBorder(new EmptyBorder(30, 50, 20, 50));

        JLabel lblLogin = new JLabel("Login");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtLogin = new JTextField();
        txtLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Cores.CINZA_BORDA),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        JLabel lblSenha = new JLabel("Senha");
        lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSenha.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtSenha = new JPasswordField();
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Cores.CINZA_BORDA),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        lblMensagem = new JLabel(" ");
        lblMensagem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMensagem.setForeground(Cores.VERMELHO);
        lblMensagem.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setBackground(Cores.VERDE_FARMACIA);
        btnEntrar.setForeground(Cores.BRANCO);
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntrar.addActionListener(e -> fazerLogin());

        txtSenha.addActionListener(e -> fazerLogin());

        centro.add(lblLogin);
        centro.add(Box.createVerticalStrut(5));
        centro.add(txtLogin);
        centro.add(Box.createVerticalStrut(15));
        centro.add(lblSenha);
        centro.add(Box.createVerticalStrut(5));
        centro.add(txtSenha);
        centro.add(Box.createVerticalStrut(20));
        centro.add(btnEntrar);
        centro.add(Box.createVerticalStrut(10));
        centro.add(lblMensagem);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(centro, BorderLayout.CENTER);
        setContentPane(painel);
    }

    private void fazerLogin() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (login.isEmpty() || senha.isEmpty()) {
            lblMensagem.setText("Preencha login e senha.");
            return;
        }

        try {
            Map<String, String> body = new HashMap<>();
            body.put("login", login);
            body.put("senha", senha);
            String resposta = HttpClient.post("http://localhost:8084/api/usuarios/login", body);
            if (resposta != null && !resposta.isEmpty() && !resposta.contains("error")) {
                dispose();
                new TelaPrincipal(login).setVisible(true);
            } else {
                lblMensagem.setText("Login ou senha inválidos.");
            }
        } catch (Exception ex) {
            lblMensagem.setText("Erro ao conectar ao servidor.");
        }
    }
}
