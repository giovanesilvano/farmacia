package br.com.farmacia.frontend.telas;

import br.com.farmacia.frontend.util.Cores;
import br.com.farmacia.frontend.util.HttpClient;
import com.fasterxml.jackson.databind.JsonNode;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaSngpc extends JPanel {

    private DefaultTableModel modelo;

    public TelaSngpc() {
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout());
        construirTela();
        carregarRegistros();
    }

    private void construirTela() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(Cores.CINZA_FUNDO);
        topo.setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel titulo = new JLabel("Registros SNGPC");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(Cores.VERDE_FARMACIA);

        JButton btnAtualizar = new JButton("🔄 Atualizar");
        btnAtualizar.setBackground(Cores.AZUL);
        btnAtualizar.setForeground(Cores.BRANCO);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setBorderPainted(false);
        btnAtualizar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAtualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAtualizar.addActionListener(e -> carregarRegistros());

        topo.add(titulo, BorderLayout.WEST);
        topo.add(btnAtualizar, BorderLayout.EAST);

        String[] colunas = {"ID", "Produto", "Tipo", "Quantidade", "CRM Médico", "Data"};
        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabela = new JTable(modelo);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setRowHeight(28);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(Cores.VERDE_FARMACIA);
        tabela.getTableHeader().setForeground(Cores.BRANCO);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(Cores.CINZA_BORDA));

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(Cores.CINZA_FUNDO);
        centro.setBorder(new EmptyBorder(0, 20, 20, 20));
        centro.add(scroll, BorderLayout.CENTER);

        add(topo, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
    }

    private void carregarRegistros() {
        modelo.setRowCount(0);
        try {
            String resposta = HttpClient.get("http://localhost:8083/api/sngpc");
            JsonNode lista = HttpClient.getMapper().readTree(resposta);
            for (JsonNode r : lista) {
                modelo.addRow(new Object[]{
                    r.path("id").asText(),
                    r.path("nomeProduto").asText(),
                    r.path("tipo").asText(),
                    r.path("quantidade").asInt(),
                    r.path("crmMedico").asText("-"),
                    r.path("dataRegistro").asText("-")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar registros SNGPC: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
