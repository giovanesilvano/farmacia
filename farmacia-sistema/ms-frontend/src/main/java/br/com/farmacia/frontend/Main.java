package br.com.farmacia.frontend;

import br.com.farmacia.frontend.telas.TelaLogin;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new TelaLogin().setVisible(true);
        });
    }
}
