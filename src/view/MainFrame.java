package view;

import controller.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class MainFrame extends JFrame {
    private JScrollPane contentScrollPane;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem menuHome;
    private JMenuItem menuLogOut;

    private Controller controller;

    public MainFrame(Controller controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistem Aplikasi Manajemen Pelanggaran Mahasiswa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        contentScrollPane = new JScrollPane();
        getContentPane().add(contentScrollPane, BorderLayout.CENTER);

        createMenuBar();
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");

        menuHome = new JMenuItem("Home");
        menuLogOut = new JMenuItem("Log Out");

        menuHome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                controller.showHomePanel();
            }
        });

        menuLogOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                controller.logout();
            }
        });

        menu.add(menuHome);
        menu.add(menuLogOut);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    public void setContentPanel(JPanel panel) {
        contentScrollPane.setViewportView(panel);
        
        if (panel instanceof LoginPanel) {
            setJMenuBar(null);
        } else {
            setJMenuBar(menuBar);
            updateMenuForPanel(panel);
        }

        revalidate();
        repaint();
    }

    private void updateMenuForPanel(JPanel panel) {
        if (panel instanceof HomeMhsPanel || panel instanceof CekPoinMhsPanel || panel instanceof UpdateFixPoinPanel) {
            menuHome.setText("Home");
            menuHome.removeActionListener(menuHome.getActionListeners()[0]); 
            menuHome.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    controller.showHomeMhsPanel();
                }
            });
        } else {
            menuHome.setText("Home");
            menuHome.removeActionListener(menuHome.getActionListeners()[0]); 
            menuHome.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    controller.showHomePanel();
                }
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Controller controller = new Controller();
                MainFrame mainFrame = new MainFrame(controller);
                mainFrame.setVisible(true);
                controller.start(mainFrame);
                
                try {
                    Database db = new Database();
                    Connection conn = db.getConnection();
                    System.out.println("Connection successful: " + (conn != null));
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
