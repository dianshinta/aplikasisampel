package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import controller.Controller;
import model.Pelanggaran;

public class EntryExistingPanel extends JPanel {
    private JTextField nimField, deskripsiField, satgasField, tanggalField;
    private ButtonGroup kegiatanButtonGroup;
    private JRadioButton opsRutinButton, sidakButton, hariBiasaButton;
    private ButtonGroup kategoriPelanggaran;
    private JRadioButton katRinganButton, katSedangButton, katBeratButton;
    private JLabel bbRange, baRange;
    private JButton submitButton;

    public EntryExistingPanel(Controller controller) {
        setLayout(new GridBagLayout());
        setBackground(new Color(234, 231, 224));  
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addComponent(createLabel("NIM:"), 0, 0, gbc);
        nimField = createTextField();
        addComponent(nimField, 1, 0, gbc);

        addComponent(createLabel("Deskripsi Pelanggaran:"), 0, 1, gbc);
        deskripsiField = createTextField();
        addComponent(deskripsiField, 1, 1, gbc);

        addComponent(createLabel("Kategori Pelanggaran:"), 0, 2, gbc);
        kategoriPelanggaran = new ButtonGroup();
        katRinganButton = createRadioButton("Ringan");
        katSedangButton = createRadioButton("Sedang");
        katBeratButton = createRadioButton("Berat");
        JPanel kategoriPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        kategoriPanel.setBackground(new Color(234, 231, 224));  
        kategoriPanel.add(katRinganButton);
        kategoriPanel.add(katSedangButton);
        kategoriPanel.add(katBeratButton);
        kategoriPelanggaran.add(katRinganButton);
        kategoriPelanggaran.add(katSedangButton);
        kategoriPelanggaran.add(katBeratButton);
        addComponent(kategoriPanel, 1, 2, gbc);

        addComponent(createLabel("Poin:"), 0, 3, gbc);
        bbRange = createRangeLabel();
        baRange = createRangeLabel();
        JPanel rangePoinPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rangePoinPanel.setBackground(new Color(234, 231, 224));  
        rangePoinPanel.add(bbRange);
        rangePoinPanel.add(createLabel(" - "));
        rangePoinPanel.add(baRange);
        addComponent(rangePoinPanel, 1, 3, gbc);

        katRinganButton.addActionListener(e -> setRangePoints("1", "5"));
        katSedangButton.addActionListener(e -> setRangePoints("6", "10"));
        katBeratButton.addActionListener(e -> setRangePoints("11", "40"));

        addComponent(createLabel("Tanggal Penindakan:"), 0, 4, gbc);
        tanggalField = createTextField();
        addComponent(tanggalField, 1, 4, gbc);

        addComponent(createLabel("Kegiatan:"), 0, 5, gbc);
        kegiatanButtonGroup = new ButtonGroup();
        opsRutinButton = createRadioButton("Operasi Rutin");
        sidakButton = createRadioButton("Operasi Umum (Sidak)");
        hariBiasaButton = createRadioButton("Hari Biasa");
        JPanel kegiatanPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        kegiatanPanel.setBackground(new Color(234, 231, 224));  
        kegiatanPanel.add(opsRutinButton);
        kegiatanPanel.add(sidakButton);
        kegiatanPanel.add(hariBiasaButton);
        kegiatanButtonGroup.add(opsRutinButton);
        kegiatanButtonGroup.add(sidakButton);
        kegiatanButtonGroup.add(hariBiasaButton);
        addComponent(kegiatanPanel, 1, 5, gbc);

        opsRutinButton.addActionListener(e -> satgasField.setText("Satgas Operasi Rutin"));
        sidakButton.addActionListener(e -> satgasField.setText("Satgas Sidak"));
        hariBiasaButton.addActionListener(e -> satgasField.setText(""));

        addComponent(createLabel("Satgas:"), 0, 6, gbc);
        satgasField = createTextField();
        addComponent(satgasField, 1, 6, gbc);

        submitButton = createButton("Simpan");
        addComponent(submitButton, 1, 7, gbc);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String nim = nimField.getText();
                String deskripsi = deskripsiField.getText();
                int baPoin = Integer.parseInt(baRange.getText());
                int bbPoin = Integer.parseInt(bbRange.getText());
                String tanggal = tanggalField.getText();
                String satgas = satgasField.getText();
                Pelanggaran pelanggaran = new Pelanggaran();

                if (nim.equals("") ||
                        (!opsRutinButton.isSelected() && !sidakButton.isSelected() && !hariBiasaButton.isSelected()) || deskripsi.equals("") ||
                        baPoin == 0 || bbPoin == 0 || tanggal.equals("") || satgas.equals("")) {
                    JOptionPane.showMessageDialog(null, "Semua field harus terisi!", "Gagal", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                pelanggaran.setDeskripsi(deskripsi);
                pelanggaran.setBbPoints(bbPoin);
                pelanggaran.setBaPoints(baPoin);
                pelanggaran.setKategori(katRinganButton.isSelected() ? "Ringan" :
                        katSedangButton.isSelected() ? "Sedang" : "Berat");
                pelanggaran.setKegiatan(opsRutinButton.isSelected() ? "Operasi Rutin" :
                        sidakButton.isSelected() ? "Operasi Umum (Sidak)" : "Hari Biasa");
                pelanggaran.setSatgas(satgas);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                try {
                    LocalDate date = LocalDate.parse(tanggal, formatter);
                    pelanggaran.setTanggal(date);
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(null, "Format tanggal tidak valid. Gunakan format dd/MM/yyyy.", "Gagal", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    controller.addPelanggaranToExisting(nim, pelanggaran);
                    Database.getInstance().insertDataPelanggaranExisting(nim);
                    clearForm();
                    JOptionPane.showMessageDialog(null, "Pelanggaran berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    System.err.println(ex);
                    JOptionPane.showMessageDialog(null, "Gagal menyimpan data", "Gagal", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void setRangePoints(String bb, String ba) {
        bbRange.setText(bb);
        baRange.setText(ba);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        return textField;
    }

    private JLabel createRangeLabel() {
        JLabel label = new JLabel("0");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(60, 60, 60)); 
        return label;
    }

    private JRadioButton createRadioButton(String text) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setFont(new Font("Arial", Font.PLAIN, 16));
        radioButton.setBackground(new Color(234, 231, 224));  
        return radioButton;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(42, 51, 66));  
        button.setForeground(Color.WHITE);
        return button;
    }

    private void addComponent(Component component, int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(component, gbc);
    }

    private void clearForm() {
        nimField.setText("");
        deskripsiField.setText("");
        kategoriPelanggaran.clearSelection();
        setRangePoints("0", "0");
        tanggalField.setText("");
        kegiatanButtonGroup.clearSelection();
        satgasField.setText("");
    }
}
