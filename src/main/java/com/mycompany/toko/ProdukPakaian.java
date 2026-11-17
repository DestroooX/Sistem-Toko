package com.mycompany.toko;
public class ProdukPakaian extends Produk {

    public ProdukPakaian(String kode, String nama, int stok) {
        super(kode, nama, stok);
    }

    @Override
    public String getKategori() {
        return "Pakaian";
    }
}
