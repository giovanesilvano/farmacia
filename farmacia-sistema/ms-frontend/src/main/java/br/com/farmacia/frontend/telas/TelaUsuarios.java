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

public class TelaUsuarios extends JPanel {

    private DefaultTableModel modelo;

    public TelaUsuarios() {
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout());
        construirTela();
        carregarUsuarios();
    }

    private void construirTela() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(Cores.CINZA_FUNDO);
        topo.setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel titulo = new JLabel("Gestão de Usuários");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(Cores.VERDE_FARMACIA);

        JButton btnNovo = new JButton("+ Novo Usuário");
        btnNovo.setBackground(Cores.VERDE_FARMACIA);
        btnNovo.setForeground(Cores.BRANCO);
        btnNovo.setFocusPainted(false);
        btnNovo.setBorderPainted(false);
        btnNovo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnNovo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNovo.addActionListener(e -> abrirFormulario());

        JButton btnAtualizar = new JButton("🔄 Atualizar");
        btnAtualizar.setBackground(Cores.AZUL);
        btnAtualizar.setForeground(Cores.BRANCO);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setBorderPainted(false);
        btnAtualizar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAtualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAtualizar.addActionListener(e -> carregarUsuarios());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoes.setBackground(Cores.CINZA_FUNDO);
        botoes.add(btnAtualizar);
        botoes.add(btnNovo);

        topo.add(titulo, BorderLayout.WEST);
        topo.add(botoes, BorderLayout.EAST);

        String[] colunas = {"ID", "Nome", "Login", "Perfil"};
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

    private void carregarUsuarios() {
        modelo.setRowCount(0);
        try {
            String resposta = HttpClient.get("http://localhost:8084/api/usuarios");
            JsonNode lista = HttpClient.getMapper().readTree(resposta);
            for (JsonNode u : lista) {
                modelo.addRow(new Object[]{
                    u.path("id").asText(),
                    u.path("nome").asText(),
                    u.path("login").asText(),
                    u.path("perfil").asText()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirFormulario() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Novo Usuário", true);
        dialog.setSize(380, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        form.setBackground(Cores.BRANCO);

        JTextField txtNome = new JTextField();
        JTextField txtLogin = new JTextField();
        JPasswordField txtSenha = new JPasswordField();
        JComboBox<String> cbPerfil = new JComboBox<>(new String[]{"ADMIN", "FARMACEUTICO", "ATENDENTE"});

        form.add(new JLabel("Nome:")); form.add(txtNome);
        form.add(new JLabel("Login:")); form.add(txtLogin);
        form.add(new JLabel("Senha:")); form.add(txtSenha);
        form.add(new JLabel("Perfil:")); form.add(cbPerfil);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(Cores.VERDE_FARMACIA);
        btnSalvar.setForeground(Cores.BRANCO);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.addActionListener(e -> {
            try {
                Map<String, Object> body = new HashMap<>();
                body.put("nome", txtNome.getText());
                body.put("login", txtLogin.getText());
                body.put("senha", new String(txtSenha.getPassword()));
                body.put("perfil", cbPerfil.getSelectedItem().toString());
                HttpClient.post("http://localhost:8084/api/usuarios", body);
                dialog.dispose();
                carregarUsuarios();
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        form.add(new JLabel()); form.add(btnSalvar);
        dialog.add(form, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}
