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

public class TelaEstoque extends JPanel {

    private DefaultTableModel modelo;
    private JTable tabela;

    public TelaEstoque() {
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout());
        construirTela();
        carregarProdutos();
    }

    private void construirTela() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(Cores.CINZA_FUNDO);
        topo.setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel titulo = new JLabel("Gestão de Estoque");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(Cores.VERDE_FARMACIA);

        JButton btnNovo = new JButton("+ Novo Produto");
        btnNovo.setBackground(Cores.VERDE_FARMACIA);
        btnNovo.setForeground(Cores.BRANCO);
        btnNovo.setFocusPainted(false);
        btnNovo.setBorderPainted(false);
        btnNovo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnNovo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNovo.addActionListener(e -> abrirFormulario(null));

        JButton btnAtualizar = new JButton("🔄 Atualizar");
        btnAtualizar.setBackground(Cores.AZUL);
        btnAtualizar.setForeground(Cores.BRANCO);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setBorderPainted(false);
        btnAtualizar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAtualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAtualizar.addActionListener(e -> carregarProdutos());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoes.setBackground(Cores.CINZA_FUNDO);
        botoes.add(btnAtualizar);
        botoes.add(btnNovo);

        topo.add(titulo, BorderLayout.WEST);
        topo.add(botoes, BorderLayout.EAST);

        String[] colunas = {"ID", "Nome", "Código de Barras", "Controlado", "Estoque Mínimo", "Preço"};
        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelo);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setRowHeight(28);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(Cores.VERDE_FARMACIA);
        tabela.getTableHeader().setForeground(Cores.BRANCO);
        tabela.setSelectionBackground(new Color(200, 240, 220));

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(Cores.CINZA_BORDA));

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        rodape.setBackground(Cores.CINZA_FUNDO);
        rodape.setBorder(new EmptyBorder(5, 20, 10, 20));

        JButton btnExcluir = new JButton("🗑 Excluir Selecionado");
        btnExcluir.setBackground(Cores.VERMELHO);
        btnExcluir.setForeground(Cores.BRANCO);
        btnExcluir.setFocusPainted(false);
        btnExcluir.setBorderPainted(false);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExcluir.addActionListener(e -> excluirProduto());
        rodape.add(btnExcluir);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(Cores.CINZA_FUNDO);
        centro.setBorder(new EmptyBorder(0, 20, 0, 20));
        centro.add(scroll, BorderLayout.CENTER);

        add(topo, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);
    }

    private void carregarProdutos() {
        modelo.setRowCount(0);
        try {
            String resposta = HttpClient.get("http://localhost:8081/api/produtos");
            JsonNode lista = HttpClient.getMapper().readTree(resposta);
            for (JsonNode p : lista) {
                modelo.addRow(new Object[]{
                    p.path("id").asText(),
                    p.path("nome").asText(),
                    p.path("codigoBarras").asText(),
                    p.path("controlado").asBoolean() ? "Sim" : "Não",
                    p.path("estoqueMinimo").asInt(),
                    "R$ " + String.format("%.2f", p.path("preco").asDouble())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirFormulario(String id) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Novo Produto", true);
        dialog.setSize(400, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        form.setBackground(Cores.BRANCO);

        JTextField txtNome = new JTextField();
        JTextField txtCodigo = new JTextField();
        JCheckBox chkControlado = new JCheckBox();
        JTextField txtEstoqueMin = new JTextField("10");
        JTextField txtPreco = new JTextField("0.00");

        form.add(new JLabel("Nome:")); form.add(txtNome);
        form.add(new JLabel("Código de Barras:")); form.add(txtCodigo);
        form.add(new JLabel("Controlado:")); form.add(chkControlado);
        form.add(new JLabel("Estoque Mínimo:")); form.add(txtEstoqueMin);
        form.add(new JLabel("Preço (R$):")); form.add(txtPreco);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(Cores.VERDE_FARMACIA);
        btnSalvar.setForeground(Cores.BRANCO);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.addActionListener(e -> {
            try {
                Map<String, Object> body = new HashMap<>();
                body.put("nome", txtNome.getText());
                body.put("codigoBarras", txtCodigo.getText());
                body.put("controlado", chkControlado.isSelected());
                body.put("estoqueMinimo", Integer.parseInt(txtEstoqueMin.getText()));
                body.put("preco", Double.parseDouble(txtPreco.getText()));
                HttpClient.post("http://localhost:8081/api/produtos", body);
                dialog.dispose();
                carregarProdutos();
                JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        form.add(new JLabel()); form.add(btnSalvar);
        dialog.add(form, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void excluirProduto() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
            return;
        }
        String id = modelo.getValueAt(linha, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja excluir este produto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                HttpClient.delete("http://localhost:8081/api/produtos/" + id);
                carregarProdutos();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
