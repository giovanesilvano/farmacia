package br.com.farmacia.frontend.telas;

import br.com.farmacia.frontend.util.Cores;
import br.com.farmacia.frontend.util.HttpClient;
import com.fasterxml.jackson.databind.JsonNode;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TelaVendas extends JPanel {

    private DefaultTableModel modelo;

    public TelaVendas() {
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout());
        construirTela();
        carregarVendas();
    }

    private void construirTela() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(Cores.CINZA_FUNDO);
        topo.setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel titulo = new JLabel("Registro de Vendas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(Cores.VERDE_FARMACIA);

        JButton btnNova = new JButton("+ Nova Venda");
        btnNova.setBackground(Cores.VERDE_FARMACIA);
        btnNova.setForeground(Cores.BRANCO);
        btnNova.setFocusPainted(false);
        btnNova.setBorderPainted(false);
        btnNova.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnNova.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNova.addActionListener(e -> abrirFormularioVenda());

        JButton btnAtualizar = new JButton("🔄 Atualizar");
        btnAtualizar.setBackground(Cores.AZUL);
        btnAtualizar.setForeground(Cores.BRANCO);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setBorderPainted(false);
        btnAtualizar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAtualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAtualizar.addActionListener(e -> carregarVendas());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoes.setBackground(Cores.CINZA_FUNDO);
        botoes.add(btnAtualizar);
        botoes.add(btnNova);

        topo.add(titulo, BorderLayout.WEST);
        topo.add(botoes, BorderLayout.EAST);

        String[] colunas = {"ID", "Código Barras", "Quantidade", "Forma Pagamento", "CRM Médico", "Data"};
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

    private void carregarVendas() {
        modelo.setRowCount(0);
        try {
            String resposta = HttpClient.get("http://localhost:8082/api/vendas");
            JsonNode lista = HttpClient.getMapper().readTree(resposta);
            for (JsonNode v : lista) {
                modelo.addRow(new Object[]{
                    v.path("id").asText(),
                    v.path("codigoBarras").asText(),
                    v.path("quantidade").asInt(),
                    v.path("formaPagamento").asText(),
                    v.path("crmMedico").asText("-"),
                    v.path("dataVenda").asText("-")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar vendas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirFormularioVenda() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nova Venda", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        form.setBackground(Cores.BRANCO);

        JTextField txtCodigo = new JTextField();
        JTextField txtQtd = new JTextField("1");
        JComboBox<String> cbPagamento = new JComboBox<>(new String[]{"DINHEIRO", "CARTAO_CREDITO", "CARTAO_DEBITO", "PIX"});
        JTextField txtCrm = new JTextField();

        form.add(new JLabel("Código de Barras:")); form.add(txtCodigo);
        form.add(new JLabel("Quantidade:")); form.add(txtQtd);
        form.add(new JLabel("Forma de Pagamento:")); form.add(cbPagamento);
        form.add(new JLabel("CRM Médico (controlado):")); form.add(txtCrm);

        JButton btnSalvar = new JButton("Registrar Venda");
        btnSalvar.setBackground(Cores.VERDE_FARMACIA);
        btnSalvar.setForeground(Cores.BRANCO);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.addActionListener(e -> {
            try {
                Map<String, Object> body = new HashMap<>();
                body.put("codigoBarras", txtCodigo.getText());
                body.put("quantidade", Integer.parseInt(txtQtd.getText()));
                body.put("formaPagamento", cbPagamento.getSelectedItem().toString());
                body.put("crmMedico", txtCrm.getText());
                HttpClient.post("http://localhost:8082/api/vendas", body);
                dialog.dispose();
                carregarVendas();
                JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        form.add(new JLabel()); form.add(btnSalvar);
        dialog.add(form, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}
