package com.mycompany.toko;
import java.io.Serializable;

public class Produk implements Serializable {
    private String kodeProduk;
    private String namaProduk;
    private int stok;

    public Produk(String kodeProduk, String namaProduk, int stok) {
        this.kodeProduk = kodeProduk;
        this.namaProduk = namaProduk;
        this.stok = stok;
    }

    public String getKodeProduk() {
        return kodeProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public int getStok() {
        return stok;
    }

    public void kurangiStok(int jumlah) {
        stok -= jumlah;
    }

    public String getKategori() {
        return "Produk Umum";
    }
}
