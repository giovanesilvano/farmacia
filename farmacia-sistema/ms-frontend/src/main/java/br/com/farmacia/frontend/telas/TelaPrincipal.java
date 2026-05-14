package br.com.farmacia.frontend.telas;

import br.com.farmacia.frontend.util.Cores;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    private final String usuarioLogado;
    private JPanel painelConteudo;

    public TelaPrincipal(String usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        setTitle("Farmácia - Sistema de Gestão");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        construirTela();
    }

    private void construirTela() {
        setLayout(new BorderLayout());

        JPanel menu = construirMenu();
        add(menu, BorderLayout.WEST);

        painelConteudo = new JPanel(new BorderLayout());
        painelConteudo.setBackground(Cores.CINZA_FUNDO);
        add(painelConteudo, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setBackground(Cores.VERDE_FARMACIA);
        rodape.setBorder(new EmptyBorder(5, 10, 5, 10));
        JLabel lblUsuario = new JLabel("Usuário: " + usuarioLogado);
        lblUsuario.setForeground(Cores.BRANCO);
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rodape.add(lblUsuario);
        add(rodape, BorderLayout.SOUTH);

        mostrarTela(new TelaDashboard());
    }

    private JPanel construirMenu() {
        JPanel menu = new JPanel();
        menu.setBackground(Cores.VERDE_FARMACIA);
        menu.setPreferredSize(new Dimension(200, 650));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel logo = new JLabel("💊 Farmácia", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        logo.setForeground(Cores.BRANCO);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.add(logo);
        menu.add(Box.createVerticalStrut(30));

        adicionarBotaoMenu(menu, "🏠  Dashboard", () -> mostrarTela(new TelaDashboard()));
        adicionarBotaoMenu(menu, "📦  Estoque", () -> mostrarTela(new TelaEstoque()));
        adicionarBotaoMenu(menu, "🛒  Vendas", () -> mostrarTela(new TelaVendas()));
        adicionarBotaoMenu(menu, "👤  Usuários", () -> mostrarTela(new TelaUsuarios()));
        adicionarBotaoMenu(menu, "📋  SNGPC", () -> mostrarTela(new TelaSngpc()));

        menu.add(Box.createVerticalGlue());

        JButton btnSair = criarBotaoMenu("🚪  Sair");
        btnSair.setBackground(Cores.VERMELHO);
        btnSair.addActionListener(e -> {
            dispose();
            new TelaLogin().setVisible(true);
        });
        menu.add(btnSair);

        return menu;
    }

    private void adicionarBotaoMenu(JPanel menu, String texto, Runnable acao) {
        JButton btn = criarBotaoMenu(texto);
        btn.addActionListener(e -> acao.run());
        menu.add(btn);
        menu.add(Box.createVerticalStrut(5));
    }

    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(Cores.VERDE_CLARO);
        btn.setForeground(Cores.BRANCO);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(5, 15, 5, 5));
        return btn;
    }

    public void mostrarTela(JPanel tela) {
        painelConteudo.removeAll();
        painelConteudo.add(tela, BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }
}
