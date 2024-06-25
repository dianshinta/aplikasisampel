package view;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Mahasiswa;
import model.Pelanggaran;

public class Database implements Serializable {
    public static Database instance;
    List<Mahasiswa> mhsList;
    private String loggedInNim = null;

    private final String DB_TYPE = "sqlite"; 
    private final String DB_FILE = "uaspbo.db"; // Use forward slashes

    public Database() throws SQLException {
        mhsList = new ArrayList<>();
    }

    public static synchronized Database getInstance() throws SQLException {
        if(instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private void loadAllMahasiswa() throws SQLException {
        String sql = "SELECT * FROM rekappelanggaran";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            mhsList.clear();
            Mahasiswa mhs = null;
            while(rs.next()) {
                String nim = rs.getString("nim");
                if (mhs == null || !mhs.getNim().equals(nim)) {
                    mhs = new Mahasiswa();
                    mhs.setNama(rs.getString("nama"));
                    mhs.setNim(rs.getString("nim"));
                    mhsList.add(mhs);
                }
                Pelanggaran pelanggaran = new Pelanggaran();
                pelanggaran.setDeskripsi(rs.getString("pelanggaran"));
                pelanggaran.setKategori(rs.getString("kategori"));
                pelanggaran.setBbPoints(rs.getInt("bbPoin"));
                pelanggaran.setBaPoints(rs.getInt("baPoin"));
                pelanggaran.setKegiatan(rs.getString("kegiatan"));
                pelanggaran.setTanggal(LocalDate.parse(rs.getString("tanggal")));
                pelanggaran.setSatgas(rs.getString("satgas"));
                mhs.addPelanggaran(pelanggaran);
            }
        }
    }

    public List<Pelanggaran> loadPelanggaranByNim(String nim) throws SQLException {
        String sql = "SELECT * FROM rekappelanggaran WHERE nim = ?";
        List<Pelanggaran> pelanggaranList = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nim);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Pelanggaran pelanggaran = new Pelanggaran();
                    pelanggaran.setDeskripsi(rs.getString("pelanggaran"));
                    pelanggaran.setKategori(rs.getString("kategori"));
                    pelanggaran.setBbPoints(rs.getInt("bbPoin"));
                    pelanggaran.setBaPoints(rs.getInt("baPoin"));
                    pelanggaran.setKegiatan(rs.getString("kegiatan"));
                    pelanggaran.setTanggal(LocalDate.parse(rs.getString("tanggal")));
                    pelanggaran.setSatgas(rs.getString("satgas"));
                    pelanggaranList.add(pelanggaran);
                }
            }
        }
        return pelanggaranList;
    }
    

    public Mahasiswa findMahasiswaByNim(String nim) {
        for (Mahasiswa mhs : mhsList) {
            if (mhs.getNim().equals(nim)) {
                return mhs;
            }
        }
        return null;
    }

    public void insertDataPelanggaran(Mahasiswa mhs) throws SQLException {
        int n = mhs.getpelanggaran().size();
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO rekappelanggaran (nama, nim, pelanggaran, bbPoin, baPoin, kegiatan, tanggal, satgas, kategori) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, mhs.getNama());
            pstmt.setString(2, mhs.getNim());
            Pelanggaran pelanggaran = mhs.getpelanggaran().get(n - 1);
            pstmt.setString(3, pelanggaran.getDeskripsi());
            pstmt.setInt(4, pelanggaran.getBbPoints());
            pstmt.setInt(5, pelanggaran.getBaPoints());
            pstmt.setString(6, pelanggaran.getKegiatan());
            pstmt.setString(7, pelanggaran.getTanggal().toString());
            pstmt.setString(8, pelanggaran.getSatgas());
            pstmt.setString(9, pelanggaran.getKategori());
            pstmt.executeUpdate();
            if (!mhsList.contains(mhs)) {
                mhsList.add(mhs);
            }
        }
    }

    public void insertDataPelanggaranExisting(String nim) throws SQLException {
        Mahasiswa mhs = findMahasiswaByNim(nim);
        if (mhs != null) {
            int n = mhs.getpelanggaran().size();
            try (Connection conn = getConnection()) {
                String sql = "INSERT INTO rekappelanggaran (nama, nim, pelanggaran, bbPoin, baPoin, kegiatan, tanggal, satgas, kategori) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, mhs.getNama());
                pstmt.setString(2, mhs.getNim());
                Pelanggaran pelanggaran = mhs.getpelanggaran().get(n - 1);
                pstmt.setString(3, pelanggaran.getDeskripsi());
                pstmt.setInt(4, pelanggaran.getBbPoints());
                pstmt.setInt(5, pelanggaran.getBaPoints());
                pstmt.setString(6, pelanggaran.getKegiatan());
                pstmt.setString(7, pelanggaran.getTanggal().toString());
                pstmt.setString(8, pelanggaran.getSatgas());
                pstmt.setString(9, pelanggaran.getKategori());
                pstmt.executeUpdate();
            }
        } else {
            throw new SQLException("Mahasiswa with NIM " + nim + " not found.");
        }
    }

    public List<Mahasiswa> getListMahasiswa() throws SQLException {
        loadAllMahasiswa(); // Ensure the list is updated from the database
        return new ArrayList<>(mhsList); // Return a copy to avoid external modification
    }

    public int getTotalBbPointsByNim(String nim) throws SQLException {
        String sql = "SELECT SUM(bbPoin) AS total_bb_points FROM rekappelanggaran WHERE nim = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nim);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_bb_points");
                } else {
                    return 0;
                }
            }
        }
    }

    public int getTotalBaPointsByNim(String nim) throws SQLException {
        String sql = "SELECT SUM(baPoin) AS total_ba_points FROM rekappelanggaran WHERE nim = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nim);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_ba_points");
                } else {
                    return 0;
                }
            }
        }
    }

    public void exportDataToCSV(String filePath) throws SQLException, IOException {
        String sql = "SELECT * FROM rekappelanggaran";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            
            writer.println("Nama,NIM,Pelanggaran,Poin,Kegiatan,Tanggal,Satgas");
            while (rs.next()) {
                String nama = rs.getString("nama");
                String nim = rs.getString("nim");
                String pelanggaran = rs.getString("pelanggaran");
                int bbPoin = rs.getInt("bbPoin");
                int baPoin = rs.getInt("baPoin");
                String kegiatan = rs.getString("kegiatan");
                String tanggal = rs.getString("tanggal");
                String satgas = rs.getString("satgas");
                
                writer.println(String.join(",", nama, nim, pelanggaran, String.valueOf(bbPoin), String.valueOf(baPoin), kegiatan, tanggal, satgas));
            }
        }
    }
    
    public void exportDataToCSVForNim(String filePath, String nim) throws SQLException, IOException {
        String query = "SELECT * FROM rekappelanggaran WHERE nim = ?";
        
        try (Connection conn = getConnection(); // Use getConnection() here
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, nim);
    
            ResultSet rs = pstmt.executeQuery();
    
            try (FileWriter csvWriter = new FileWriter(filePath)) {
                csvWriter.append("Nama,NIM,Pelanggaran,BbPoin,BaPoin,Kegiatan,Tanggal,Satgas,Kategori\n");
    
                while (rs.next()) {
                    csvWriter.append(rs.getString("nama")).append(",");
                    csvWriter.append(rs.getString("nim")).append(",");
                    csvWriter.append(rs.getString("pelanggaran")).append(",");
                    csvWriter.append(String.valueOf(rs.getInt("bbPoin"))).append(",");
                    csvWriter.append(String.valueOf(rs.getInt("baPoin"))).append(",");
                    csvWriter.append(rs.getString("kegiatan")).append(",");
                    csvWriter.append(rs.getString("tanggal")).append(",");
                    csvWriter.append(rs.getString("satgas")).append(",");
                    csvWriter.append(rs.getString("kategori")).append("\n");
                }
    
                csvWriter.flush();
            }
        }
    }    
    

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC"); // Ensure the SQLite driver is loaded
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("SQLite JDBC Driver not found.", e);
        }
        return DriverManager.getConnection("jdbc:" + DB_TYPE + ":" + DB_FILE);
    }

    public boolean authenticate(String username, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean authenticateMhs(String username, String password) throws SQLException {
        String sql = "SELECT * FROM userMhs WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    this.loggedInNim = username;
                    System.out.println("logged in nim: " + loggedInNim);
                    return true;
                }
                return false;
            }
        }
    }

    public String getLoggedInNim() {
        return loggedInNim;
    }

    public void clearLoggedInNim() {
        loggedInNim = null;
    }

    // New method to update user points
    public void updateUserPoints(String nim, int newPoints) throws SQLException {
        String sql = "UPDATE userMhs SET totalPoin = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newPoints);
            pstmt.setString(2, nim);
            pstmt.executeUpdate();
        }
    }

    // New method to get total points for a user
    public int getTotalPoints(String nim) throws SQLException {
        String sql = "SELECT totalPoin FROM userMhs WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nim);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("totalPoin");
                } else {
                    return 0;
                }
            }
        }
    }

}
