package controller;

import model.Mahasiswa;
import model.Pelanggaran;
import view.*;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private List<Mahasiswa> mahasiswaList;
    private MainFrame mainFrame;
    private EntryPanel entryPanel;
    private EntryExistingPanel entryExistingPanel;
    private CekPoinPanel cekPoinPanel;
    private CekPoinMhsPanel cekPoinMhsPanel;
    private RekapanDataPanel rekapanPelanggaran; 
    private HomePanel homePanel;
    private HomeMhsPanel homeMhsPanel;
    private UpdateFixPoinPanel updateFixPoinPanel;
    private LoginPanel loginPanel;

    public Controller() {
        mahasiswaList = new ArrayList<>();
        mainFrame = new MainFrame(this);
        entryPanel = new EntryPanel(this);
        entryExistingPanel = new EntryExistingPanel(this);
        cekPoinPanel = new CekPoinPanel(this);
        cekPoinMhsPanel = new CekPoinMhsPanel(this);
        updateFixPoinPanel = new UpdateFixPoinPanel(this);
        rekapanPelanggaran = new RekapanDataPanel();
        homePanel = new HomePanel(this);
        homeMhsPanel = new HomeMhsPanel(this);
        loginPanel = new LoginPanel(this);
    }

    public void start(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        mainFrame.setContentPanel(new LoginPanel(this));
    }

    public void addPelanggaran(Mahasiswa mahasiswa, Pelanggaran pelanggaran) {
        mahasiswa.addPelanggaran(pelanggaran);
        mahasiswaList.add(mahasiswa);
    }

    public void addPelanggaranToExisting(String nim, Pelanggaran pelanggaran) {
        Mahasiswa mahasiswa = findMahasiswaByNim(nim);
        if (mahasiswa != null) {
            mahasiswa.addPelanggaran(pelanggaran);
        } else {
            JOptionPane.showMessageDialog(null, "Mahasiswa not found.");
        }
    }

    public void cekTotalPoin(String nim) {
        try {
            Database db = Database.getInstance();
            int totalBbPoints = db.getTotalBbPointsByNim(nim);
            int totalBaPoints = db.getTotalBaPointsByNim(nim);
            Mahasiswa mahasiswa = db.findMahasiswaByNim(nim);
            if (mahasiswa != null) {
                List<Pelanggaran> pelanggaranList = db.loadPelanggaranByNim(nim);
                cekPoinPanel.updateTotalPoints(totalBbPoints, totalBaPoints);
                cekPoinPanel.updatePelanggaranTable(pelanggaranList);
            } else {
                JOptionPane.showMessageDialog(null, "Mahasiswa not found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error accessing database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cekTotalMhsPoin() {
        try {
            Database db = Database.getInstance();
            String nimLoggedIn = db.getLoggedInNim();
            if (nimLoggedIn == null) {
                JOptionPane.showMessageDialog(null, "No user is currently logged in.", "User Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int totalBbPoints = db.getTotalBbPointsByNim(nimLoggedIn);
            int totalBaPoints = db.getTotalBaPointsByNim(nimLoggedIn);
            
            List<Pelanggaran> pelanggaranList = db.loadPelanggaranByNim(nimLoggedIn);
            cekPoinMhsPanel.updateTotalPoints(totalBbPoints, totalBaPoints);
            cekPoinMhsPanel.updatePelanggaranTable(pelanggaranList);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error accessing database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Mahasiswa findMahasiswaByNim(String nim) {
        try {
            return Database.getInstance().findMahasiswaByNim(nim);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error accessing database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void showHomePanel() {
        mainFrame.setContentPanel(homePanel);
    }

    public void showHomeMhsPanel() {
        try {
            Database db = Database.getInstance();
            System.out.println(db.getLoggedInNim());
        } catch (Exception e) {
            System.out.println("no logged in nim");
        }
        mainFrame.setContentPanel(homeMhsPanel);
    }

    public void showEntryPanel() {
        mainFrame.setContentPanel(entryPanel);
    }

    public void showEntryExistingPanel() {
        mainFrame.setContentPanel(entryExistingPanel);
    }

    public void showCekPoinPanel() {
        rekapanPelanggaran.loadTableData();
        mainFrame.setContentPanel(cekPoinPanel);
    }

    public void showCekPoinMhsPanel() {
        cekPoinMhsPanel = new CekPoinMhsPanel(this);
        cekTotalMhsPoin();
        mainFrame.setContentPanel(cekPoinMhsPanel);
    }

    public void showRekapanPelanggaranPanel() {
        rekapanPelanggaran.loadTableData();
        mainFrame.setContentPanel(rekapanPelanggaran);
    }

    public void showUpdateFixPoinPanel() {
        initializeUpdateFixPoinPanel();
        mainFrame.setContentPanel(updateFixPoinPanel);
    }

    public void showLoginPanel() {
        mainFrame.setContentPanel(loginPanel);
    }

    public void logout() {
        try {
            Database db = Database.getInstance();
            db.clearLoggedInNim();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        showLoginPanel();
    }

    public void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        fileChooser.setSelectedFile(new File("REKAPAN_DATA PELANGGARAN.csv"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                Database.getInstance().exportDataToCSV(fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "Data successfully exported to " + fileToSave.getAbsolutePath());
            } catch (SQLException | IOException e) {
                JOptionPane.showMessageDialog(null, "Error exporting data: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void exportDataMhs() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        fileChooser.setSelectedFile(new File("REKAPAN_DATA_PELANGGARAN.csv"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                Database db = Database.getInstance();
                String nimLoggedIn = db.getLoggedInNim();
                if (nimLoggedIn != null) {
                    db.exportDataToCSVForNim(fileToSave.getAbsolutePath(), nimLoggedIn);
                    JOptionPane.showMessageDialog(null, "Data successfully exported to " + fileToSave.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(null, "No user is currently logged in.", "User Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | IOException e) {
                JOptionPane.showMessageDialog(null, "Error exporting data: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean authenticate(String username, String password) throws SQLException {
        try {
            Database database = Database.getInstance();
            return database.authenticate(username, password);
        } catch (SQLException ex) {
            throw new SQLException("Error authenticating user: " + ex.getMessage());
        }
    }

    public boolean authenticateMhs(String username, String password) throws SQLException {
        try {
            Database database = Database.getInstance();
            return database.authenticateMhs(username, password);
        } catch (SQLException ex) {
            throw new SQLException("Error authenticating user: " + ex.getMessage());
        }
    }

    public void start() {
        mainFrame.setVisible(true);
    }

    // New methods for updating points and refreshing label
    public void updateUserPoints(int newPoints) {
        try {
            Database db = Database.getInstance();
            String nimLoggedIn = db.getLoggedInNim();
            if (nimLoggedIn != null) {
                db.updateUserPoints(nimLoggedIn, newPoints);
                int updatedPoints = db.getTotalPoints(nimLoggedIn);
                updateFixPoinPanel.updatePointsLabel(updatedPoints);
            } else {
                JOptionPane.showMessageDialog(null, "No user is currently logged in.", "User Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating points: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeUpdateFixPoinPanel() {
        try {
            Database db = Database.getInstance();
            String nimLoggedIn = db.getLoggedInNim();
            if (nimLoggedIn != null) {
                int currentPoints = db.getTotalPoints(nimLoggedIn);
                updateFixPoinPanel.updatePointsLabel(currentPoints);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching points: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getLoggedInNim() {
        try {
            Database db = Database.getInstance();
            String nimLoggedIn = db.getLoggedInNim();
            return nimLoggedIn;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }
}
