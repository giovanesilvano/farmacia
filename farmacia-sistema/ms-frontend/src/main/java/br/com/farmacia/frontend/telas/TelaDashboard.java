package br.com.farmacia.frontend.telas;

import br.com.farmacia.frontend.util.Cores;
import br.com.farmacia.frontend.util.HttpClient;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaDashboard extends JPanel {

    public TelaDashboard() {
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout());
        construirTela();
    }

    private void construirTela() {
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.setBackground(Cores.CINZA_FUNDO);
        topo.setBorder(new EmptyBorder(20, 20, 10, 20));
        JLabel titulo = new JLabel("Dashboard");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(Cores.VERDE_FARMACIA);
        topo.add(titulo);

        JPanel cards = new JPanel(new GridLayout(2, 2, 15, 15));
        cards.setBackground(Cores.CINZA_FUNDO);
        cards.setBorder(new EmptyBorder(10, 20, 20, 20));

        cards.add(criarCard("📦 Produtos em Estoque", buscarTotal("http://localhost:8081/api/produtos"), Cores.VERDE_FARMACIA));
        cards.add(criarCard("🛒 Vendas Realizadas", buscarTotal("http://localhost:8082/api/vendas"), Cores.AZUL));
        cards.add(criarCard("📋 Registros SNGPC", buscarTotal("http://localhost:8083/api/sngpc"), new Color(150, 80, 200)));
        cards.add(criarCard("👤 Usuários Cadastrados", buscarTotal("http://localhost:8084/api/usuarios"), new Color(200, 120, 0)));

        add(topo, BorderLayout.NORTH);
        add(cards, BorderLayout.CENTER);
    }

    private JPanel criarCard(String titulo, String valor, Color cor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Cores.BRANCO);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Cores.CINZA_BORDA),
            new EmptyBorder(20, 20, 20, 20)));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(Color.DARK_GRAY);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValor.setForeground(cor);
        lblValor.setHorizontalAlignment(SwingConstants.RIGHT);

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);
        return card;
    }

    private String buscarTotal(String url) {
        try {
            String resposta = HttpClient.get(url);
            com.fasterxml.jackson.databind.JsonNode node = HttpClient.getMapper().readTree(resposta);
            if (node.isArray()) return String.valueOf(node.size());
            return "1";
        } catch (Exception e) {
            return "—";
        }
    }
}
