package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.border.EmptyBorder;

import controller.Controller;

public class LoginPanel extends JPanel {
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Controller controller;

    public LoginPanel(Controller controller) {
        this.controller = controller;

        initComponents();
        setBackground(new Color(234, 231, 224)); // Light cream background
    }

    private void initComponents() {
        titleLabel = new JLabel("SAMPEL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size and style
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        usernameLabel = new JLabel("Username: ");
        passwordLabel = new JLabel("Password: ");
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Log in") {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBackground() != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.dispose();
                }
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 87, 34)); // Darker orange color for border
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };

        // Set component styles
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(42, 51, 66)); // Dark blue background
        loginButton.setForeground(Color.WHITE); // Set text color to white
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(false); // Remove default button content fill
        loginButton.setOpaque(false); // Make it non-opaque to allow custom painting
        loginButton.setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding inside the button

        // Hover effect for login button
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(255, 160, 0)); // Slightly lighter orange
                loginButton.repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(42, 51, 66)); // Original dark blue
                loginButton.repaint();
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Set layout constraints for title label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across 2 columns
        gbc.anchor = GridBagConstraints.CENTER; // Center align
        gbc.insets = new Insets(20, 10, 20, 10);
        add(titleLabel, gbc);

        // Set layout constraints for username label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST; // Left align
        gbc.insets = new Insets(10, 10, 10, 10); // Left inset to remove space
        add(usernameLabel, gbc);

        // Set layout constraints for username field
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST; // Right align
        gbc.insets = new Insets(10, 10, 10, 10); // Right inset to remove space
        add(usernameField, gbc);

        // Set layout constraints for password label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST; // Left align
        gbc.insets = new Insets(10, 10, 10, 10); // Left inset to remove space
        add(passwordLabel, gbc);

        // Set layout constraints for password field
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST; // Right align
        gbc.insets = new Insets(10, 10, 10, 10); // Right inset to remove space
        add(passwordField, gbc);

        // Set layout constraints for login button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 20, 10);
        add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    if (controller.authenticate(username, password)) {
                        controller.showHomePanel();
                    } else if (controller.authenticateMhs(username, password)) {
                        controller.showHomeMhsPanel();
                    } else {
                        JOptionPane.showMessageDialog(null, "Username atau password salah.", "Login Gagal", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada database.", "Login Gagal", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
