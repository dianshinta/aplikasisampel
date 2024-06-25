package view;

import javax.swing.*;
import controller.Controller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class HomeMhsPanel extends JPanel {
    private JLabel judulAppLabel;
    private JLabel menuLabel;
    private JButton btnCheckPoints;
    private JButton btnUpdatePoints;
    private JButton btnExportData;
    private Controller controller;

    public HomeMhsPanel(Controller controller) {
        this.controller = controller;
        initComponents();
        setBackground(new Color(234, 231, 224)); 
    }

    private void initComponents() {
        judulAppLabel = new JLabel("Selamat Datang!");
        judulAppLabel.setHorizontalAlignment(JLabel.CENTER);
        judulAppLabel.setFont(new Font("Segoe UI", Font.BOLD, 20)); 

        menuLabel = new JLabel("Menu: ");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 17)); 

        btnCheckPoints = createGradientButton("Cek Total Poin");
        btnUpdatePoints = createGradientButton("Perbarui Poin Anda");
        btnExportData = createGradientButton("Ekspor Data ke File .csv");

        btnCheckPoints.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    Database db = Database.getInstance();
                    System.out.println(db.getTotalBbPointsByNim(db.getLoggedInNim()));
                    System.out.println(db.getTotalBaPointsByNim(db.getLoggedInNim()));
                } catch (SQLException e) {
                    System.out.println("nothing");
                }
                controller.showCekPoinMhsPanel();
            }
        });

        btnUpdatePoints.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                controller.showUpdateFixPoinPanel();
                new RekapanDataPanel().loadTableData();
            }
        });

        btnExportData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                controller.exportDataMhs();
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); 
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(judulAppLabel, gbc);
        gbc.gridy++;
        add(menuLabel, gbc);
        gbc.gridy++;
        add(btnCheckPoints, gbc);
        gbc.gridy++;
        add(btnUpdatePoints, gbc);
        gbc.gridy++;
        add(btnExportData, gbc);
    }

    private JButton createGradientButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color color1 = new Color(42, 51, 66); 
                Color color2 = new Color(255, 87, 34); 
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                setForeground(Color.WHITE);

                g2d.dispose();

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 87, 34)); 
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();
            }
        };

        button.setFont(new Font("Arial", Font.BOLD, 16)); 
        button.setForeground(Color.WHITE); 
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 87, 34)),
            BorderFactory.createEmptyBorder(10, 20, 10, 20) 
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 160, 0)); 
                button.setOpaque(true);
                button.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(null);
                button.setOpaque(false);
                button.repaint();
            }
        });

        return button;
    }

    public JButton getBtnCheckPoints() {
        return btnCheckPoints;
    }

    public JButton getBtnExportData() {
        return btnExportData;
    }
}
