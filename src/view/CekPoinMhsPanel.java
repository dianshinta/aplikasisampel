package view;

import model.Pelanggaran;
import javax.swing.*;
import java.awt.*;
import controller.Controller;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CekPoinMhsPanel extends JPanel {
    private JLabel resultLabel;
    private JTable pelanggaranTable;
    private DefaultTableModel tableModel;

    public CekPoinMhsPanel(Controller controller) {
        setLayout(new GridBagLayout());
        setBackground(new Color(234,231,224)); // Light orange background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Adding padding between components
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Total Points Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        resultLabel = new JLabel("Range Total Poin: ");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(resultLabel, gbc);

        // Table for displaying violations
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        tableModel = new DefaultTableModel(new String[]{"Deskripsi", "Batas Bawah Poin", "Batas Atas Poin", "Kategori", "Kegiatan", "Tanggal", "Satgas", "Kategori"}, 0);
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
        tableModel.setRowCount(0); // Clear existing data
        for (Pelanggaran pelanggaran : pelanggaranList) {
            tableModel.addRow(new Object[]{
                pelanggaran.getDeskripsi(),
                pelanggaran.getBbPoints(),
                pelanggaran.getBaPoints(),
                pelanggaran.getKategori(),
                pelanggaran.getKegiatan(),
                pelanggaran.getTanggal().toString(),
                pelanggaran.getSatgas(),
                pelanggaran.getKategori()
            });
        }
    }
}
