package com.mycompany.toko;
public class ProdukElektronik extends Produk {

    public ProdukElektronik(String kode, String nama, int stok) {
        super(kode, nama, stok);
    }

    @Override
    public String getKategori() {
        return "Elektronik";
    }
}
