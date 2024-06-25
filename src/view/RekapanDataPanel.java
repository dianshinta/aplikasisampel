package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

import model.Mahasiswa;
import model.Pelanggaran;

public class RekapanDataPanel extends JPanel {
    private JTable rekapanTable;
    private JScrollPane contentScrollPane;

    public RekapanDataPanel() {
        setBackground(new Color(234,231,224)); 
        setLayout(new BorderLayout());
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(255, 223, 186));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        
        contentScrollPane = new JScrollPane();
        rekapanTable = new JTable();

        rekapanTable.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NIM", "Nama", "Pelanggaran", "Kategori", "Batas Bawah Poin", "Batas Atas Poin", "Kegiatan", "Tanggal", "Satgas"
            }
        ) {
            Class[] types = new Class [] {
                String.class, String.class, String.class, String.class, Integer.class, Integer.class, String.class, String.class, String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });

        rekapanTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(255, 241, 213)); 
                    } else {
                        c.setBackground(new Color(255, 224, 178));
                    }
                }
                return c;
            }
        });

        rekapanTable.getColumnModel().getColumn(4).setCellRenderer(new PoinCellRenderer());
        rekapanTable.getColumnModel().getColumn(5).setCellRenderer(new PoinCellRenderer());

        contentScrollPane.setViewportView(rekapanTable);
        tablePanel.add(contentScrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
    }   

    public void loadTableData() {

        DefaultTableModel dtm = (DefaultTableModel) rekapanTable.getModel();
        while(dtm.getRowCount()>0) {
            dtm.removeRow(0);
        }
        
        try {
            for (Mahasiswa mhs : Database.getInstance().getListMahasiswa()) {
                for (Pelanggaran pelanggaran : mhs.getpelanggaran()) {
                    dtm.addRow(new Object[]{mhs.getNim(), mhs.getNama(), pelanggaran.getDeskripsi(), pelanggaran.getKategori(), pelanggaran.getBbPoints(), 
                        pelanggaran.getBaPoints(), pelanggaran.getKegiatan(), pelanggaran.getTanggal().toString(), pelanggaran.getSatgas()});
                }
            }
        }   catch (SQLException ex) {
            System.err.println(ex);
            JOptionPane.showMessageDialog(null, "Gagal mengambil data", "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public JTable getRekapanTable() {
        return rekapanTable;
    }

    private class PoinCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            int poinValue = Integer.parseInt(value.toString());
            if (poinValue <= 5) {
                c.setBackground(new Color(144, 238, 144)); 
            } else if (poinValue <= 10) {
                c.setBackground(new Color(255, 215, 0)); 
            } else {
                c.setBackground(new Color(255, 99, 71)); 
            }
            return c;
        }
    }
}