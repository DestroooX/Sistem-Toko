package com.mycompany.toko;
public class ProdukMakanan extends Produk {

    public ProdukMakanan(String kode, String nama, int stok) {
        super(kode, nama, stok);
    }

    @Override
    public String getKategori() {
        return "Makanan";
    }
}
