// Pelanggaran.java
package model;

import java.time.LocalDate;

public class Pelanggaran {
    private String deskripsi;
    private String kategori;
    private int bbPoints;
    private int baPoints;
    private String points;
    private LocalDate tanggal;
    private String kegiatan;
    private String satgas;

    public Pelanggaran() {

    }
    public Pelanggaran(String deskripsi, String kategori, int bbPoints, int baPoints, LocalDate tanggal, String kegiatan, String satgas) {
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        this.bbPoints = bbPoints;
        this.baPoints = baPoints;
        this.tanggal = tanggal;
        this.kegiatan = kegiatan;
        this.satgas = satgas;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getKategori() {
        return kategori;
    }
    
    public int getBaPoints() {
        return baPoints;
    }

    public int getBbPoints() {
        return bbPoints;
    }

    public String getPoints() {
        points = bbPoints + " - " + baPoints;
        return points;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public String getKegiatan() {
        return kegiatan;
    }

    public String getSatgas() {
        return satgas;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setBbPoints(int bbPoints) {
        this.bbPoints = bbPoints;
    }

    public void setBaPoints(int baPoints) {
        this.baPoints = baPoints;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public void setKegiatan(String kegiatan) {
        this.kegiatan = kegiatan;
    }

    public void setSatgas(String satgas) {
        this.satgas = satgas;
    }
}
