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
import model.Mahasiswa;
import model.Pelanggaran;

public class EntryPanel extends JPanel {
    private JTextField namaField, nimField, deskripsiField, satgasField, tanggalField;
    private ButtonGroup kegiatanButtonGroup;
    private JRadioButton opsRutinButton;
    private JRadioButton sidakButton;
    private JRadioButton hariBiasaButton;
    private ButtonGroup kategoriPelanggaran;
    private JRadioButton katRinganButton;
    private JRadioButton katSedangButton;
    private JRadioButton katBeratButton;
    private JLabel bbRange;
    private JLabel baRange;
    private JButton submitButton;

    public EntryPanel(Controller controller) {
        Mahasiswa mhs = new Mahasiswa();
        Pelanggaran pelanggaran = new Pelanggaran();

        setLayout(new GridBagLayout());
        setBackground(new Color(234, 231, 224));  // Light orange background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addComponent(createLabel("Nama:"), 0, 0, gbc);
        namaField = createTextField();
        addComponent(namaField, 1, 0, gbc);

        addComponent(createLabel("NIM:"), 0, 1, gbc);
        nimField = createTextField();
        addComponent(nimField, 1, 1, gbc);

        addComponent(createLabel("Deskripsi Pelanggaran:"), 0, 2, gbc);
        deskripsiField = createTextField();
        addComponent(deskripsiField, 1, 2, gbc);

        addComponent(createLabel("Kategori Pelanggaran:"), 0, 3, gbc);
        kategoriPelanggaran = new ButtonGroup();
        katRinganButton = createRadioButton("Ringan");
        katSedangButton = createRadioButton("Sedang");
        katBeratButton = createRadioButton("Berat");
        JPanel kategoriPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        kategoriPanel.setBackground(new Color(234, 231, 224));  // Light orange background
        kategoriPanel.add(katRinganButton);
        kategoriPanel.add(katSedangButton);
        kategoriPanel.add(katBeratButton);
        kategoriPelanggaran.add(katRinganButton);
        kategoriPelanggaran.add(katSedangButton);
        kategoriPelanggaran.add(katBeratButton);
        addComponent(kategoriPanel, 1, 3, gbc);

        addComponent(createLabel("Poin:"), 0, 4, gbc);
        bbRange = createLabel("0");
        baRange = createLabel("0");
        JPanel rangePoinPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rangePoinPanel.setBackground(new Color(234, 231, 224));  // Light orange background
        rangePoinPanel.add(bbRange);
        rangePoinPanel.add(createLabel(" - "));
        rangePoinPanel.add(baRange);
        addComponent(rangePoinPanel, 1, 4, gbc);

        katRinganButton.addActionListener(e -> setRangePoints("1", "5"));
        katSedangButton.addActionListener(e -> setRangePoints("6", "10"));
        katBeratButton.addActionListener(e -> setRangePoints("11", "40"));

        addComponent(createLabel("Tanggal Penindakan:"), 0, 5, gbc);
        tanggalField = createTextField();
        addComponent(tanggalField, 1, 5, gbc);

        addComponent(createLabel("Kegiatan:"), 0, 6, gbc);
        kegiatanButtonGroup = new ButtonGroup();
        opsRutinButton = createRadioButton("Operasi Rutin");
        sidakButton = createRadioButton("Operasi Umum (Sidak)");
        hariBiasaButton = createRadioButton("Hari Biasa");
        JPanel kegiatanPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        kegiatanPanel.setBackground(new Color(234, 231, 224));  // Light orange background
        kegiatanPanel.add(opsRutinButton);
        kegiatanPanel.add(sidakButton);
        kegiatanPanel.add(hariBiasaButton);
        kegiatanButtonGroup.add(opsRutinButton);
        kegiatanButtonGroup.add(sidakButton);
        kegiatanButtonGroup.add(hariBiasaButton);
        addComponent(kegiatanPanel, 1, 6, gbc);

        addComponent(createLabel("Satgas:"), 0, 7, gbc);
        satgasField = createTextField();
        addComponent(satgasField, 1, 7, gbc);

        opsRutinButton.addActionListener(e -> satgasField.setText("Satgas Operasi Rutin"));
        sidakButton.addActionListener(e -> satgasField.setText("Satgas Sidak"));
        hariBiasaButton.addActionListener(e -> satgasField.setText(""));

        submitButton = createButton("Simpan");
        addComponent(submitButton, 1, 8, gbc);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String nama = namaField.getText();
                String nim = nimField.getText();
                String deskripsi = deskripsiField.getText();
                int baPoin = Integer.parseInt(baRange.getText());
                int bbPoin = Integer.parseInt(bbRange.getText());
                String tanggal = tanggalField.getText();
                String satgas = satgasField.getText();

                if (nama.equals("") || nim.equals("") ||
                        (!opsRutinButton.isSelected() && !sidakButton.isSelected() && !hariBiasaButton.isSelected()) || deskripsi.equals("") ||
                        baPoin == 0 || bbPoin == 0 || tanggal.equals("") || satgas.equals("")) {
                    JOptionPane.showMessageDialog(null, "Semua field harus terisi!", "Gagal", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                pelanggaran.setDeskripsi(deskripsi);
                pelanggaran.setBbPoints(bbPoin);
                pelanggaran.setBaPoints(baPoin);

                if (katRinganButton.isSelected()) {
                    pelanggaran.setKategori("Ringan");
                } else if (katSedangButton.isSelected()) {
                    pelanggaran.setKategori("Sedang");
                } else if (katBeratButton.isSelected()) {
                    pelanggaran.setKategori("Berat");
                }

                if (opsRutinButton.isSelected()) {
                    pelanggaran.setKegiatan("Operasi Rutin");
                    pelanggaran.setSatgas(satgas);
                } else if (sidakButton.isSelected()) {
                    pelanggaran.setKegiatan("Operasi Umum (Sidak)");
                    pelanggaran.setSatgas(satgas);
                } else if (hariBiasaButton.isSelected()) {
                    pelanggaran.setKegiatan("Hari Biasa");
                    pelanggaran.setSatgas(satgas);
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
                try {
                    LocalDate date = LocalDate.parse(tanggal, formatter);
                    pelanggaran.setTanggal(date);
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(null, "Format tanggal tidak valid. Gunakan format dd/MM/yy.", "Gagal", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                mhs.setNama(nama);
                mhs.setNim(nim);

                try {
                    if (Database.getInstance().findMahasiswaByNim(nim) != null) {
                        controller.addPelanggaranToExisting(nim, pelanggaran);
                        Database.getInstance().insertDataPelanggaranExisting(nim);
                    } else {
                        controller.addPelanggaran(mhs, pelanggaran);
                        Database.getInstance().insertDataPelanggaran(mhs);
                    }
                    clearForm();
                    JOptionPane.showMessageDialog(null, "Data tersimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    new RekapanDataPanel().loadTableData();
                } catch (SQLException ex) {
                    System.err.println(ex);
                    JOptionPane.showMessageDialog(null, "Gagal menyimpan data", "Gagal", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        return textField;
    }

    private JRadioButton createRadioButton(String text) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setBackground(new Color(234, 231, 224));  // Light orange background
        radioButton.setFont(new Font("Arial", Font.PLAIN, 16));
        return radioButton;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(42, 51, 66));  // Darker orange for button
        button.setForeground(Color.WHITE);
        return button;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    private void addComponent(Component component, int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(component, gbc);
    }

    private void setRangePoints(String bb, String ba) {
        bbRange.setText(bb);
        baRange.setText(ba);
    }

    private void clearForm() {
        namaField.setText("");
        nimField.setText("");
        deskripsiField.setText("");
        tanggalField.setText("");
        satgasField.setText("");
        baRange.setText("0");
        bbRange.setText("0");
        kegiatanButtonGroup.clearSelection();
        kategoriPelanggaran.clearSelection();
    }
}
