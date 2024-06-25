// Mahasiswa.java
package model;

import java.util.ArrayList;
import java.util.List;

public class Mahasiswa {
    private String nama;
    private String nim;
    private List<Pelanggaran> pelanggaran;
    private int totalBbPoints;
    private int totalBaPoints;

    public Mahasiswa() {
        this.pelanggaran = new ArrayList<>();
    }

    public Mahasiswa(String nama, String nim) {
        this.nama = nama;
        this.nim = nim;
        this.pelanggaran = new ArrayList<>();
        this.totalBbPoints = 0;
        this.totalBaPoints = 0;
    }

    public String getNama() {
        return nama;
    }

    public String getNim() {
        return nim;
    }

    public void addPelanggaran(Pelanggaran pelanggaran) {
        this.pelanggaran.add(pelanggaran);
        this.totalBbPoints += pelanggaran.getBbPoints();
        this.totalBaPoints += pelanggaran.getBaPoints();
    }

    public int getTotalBbPoints() {
        return totalBbPoints;
    }

    public int getTotalBaPoints() {
        return totalBaPoints;
    }

    public List<Pelanggaran> getpelanggaran() {
        return pelanggaran;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

}

