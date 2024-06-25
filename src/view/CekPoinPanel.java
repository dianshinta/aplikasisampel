package view;

import model.Pelanggaran;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.Controller;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CekPoinPanel extends JPanel {
    private JTextField nimField;
    private JLabel resultLabel;
    private JButton checkButton;
    private JTable pelanggaranTable;
    private DefaultTableModel tableModel;

    public CekPoinPanel(Controller controller) {
        setLayout(new GridBagLayout());
        setBackground(new Color(234,231,224)); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nimLabel = new JLabel("NIM:");
        nimLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(nimLabel, gbc);

        gbc.gridx = 1;
        nimField = new JTextField(20);
        nimField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(nimField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        checkButton = new JButton("Cek");
        checkButton.setFont(new Font("Arial", Font.BOLD, 14));
        checkButton.setBackground(new Color(42, 51, 66)); 
        checkButton.setForeground(Color.WHITE);
        checkButton.setFocusPainted(false);
        checkButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 87, 34)),
            BorderFactory.createEmptyBorder(10, 20, 10, 20) 
        ));
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nim = nimField.getText();
                controller.cekTotalPoin(nim);
            }
        });
        add(checkButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        resultLabel = new JLabel("Total Poin: ");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(resultLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        tableModel = new DefaultTableModel(new String[]{"Deskripsi", "Batas Bawah Poin", "Batas Atas Poin", "Kategori", "Kegiatan", "Tanggal", "Satgas"}, 0);
        pelanggaranTable = new JTable(tableModel);
        pelanggaranTable.setFont(new Font("Arial", Font.PLAIN, 12));
        pelanggaranTable.setRowHeight(25);
        pelanggaranTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(pelanggaranTable);
        add(scrollPane, gbc);
    }

    public void updateTotalPoints(int bbPoin, int baPoin) {
        resultLabel.setText("Range Total Poin: " + bbPoin + " - " + baPoin);
    }

    public void updatePelanggaranTable(List<Pelanggaran> pelanggaranList) {
        tableModel.setRowCount(0); 
        for (Pelanggaran pelanggaran : pelanggaranList) {
            tableModel.addRow(new Object[]{
                pelanggaran.getDeskripsi(),
                pelanggaran.getBbPoints(),
                pelanggaran.getBaPoints(),
                pelanggaran.getKategori(),
                pelanggaran.getKegiatan(),
                pelanggaran.getTanggal().toString(),
                pelanggaran.getSatgas()
            });
        }
    }

    public JTextField getNimField() {
        return nimField;
    }
}
