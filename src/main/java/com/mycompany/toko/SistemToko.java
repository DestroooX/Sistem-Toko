
package com.mycompany.toko;

import java.io.*;
import java.util.*;

public class SistemToko implements Serializable {

    private String namaToko;
    private HashMap<String, Pelanggan> daftarPelanggan;
    private HashMap<String, Produk> daftarProduk;
    private ArrayList<Transaksi> daftarTransaksi;

    public SistemToko(String namaToko) {
        this.namaToko = namaToko;
        daftarPelanggan = new HashMap<>();
        daftarProduk = new HashMap<>();
        daftarTransaksi = new ArrayList<>();
    }

    public void tambahPelanggan(String id, String nama) {
        Pelanggan pelanggan = new Pelanggan(id, nama);
        daftarPelanggan.put(id, pelanggan);
    }

    public void tambahProduk(String kode, String nama, int stok) {
        Produk produk;

        if (kode.equals("OBJ-A")) {
            produk = new ProdukElektronik(kode, nama, stok);
        } else if (kode.equals("OBJ-B")) {
            produk = new ProdukPakaian(kode, nama, stok);
        } else {
            produk = new ProdukMakanan(kode, nama, stok);
        }

        daftarProduk.put(kode, produk);
    }

    public void prosesTransaksi(String id, String kode, int jumlah)
            throws TransaksiException {

        Pelanggan pelanggan = daftarPelanggan.get(id);
        Produk produk = daftarProduk.get(kode);

        if (pelanggan == null) {
            throw new TransaksiException("Pelanggan " + id + " tidak ditemukan.");
        }

        if (produk == null) {
            throw new TransaksiException("Produk " + kode + " tidak ditemukan.");
        }

        if (jumlah <= 0) {
            throw new TransaksiException("Jumlah transaksi harus lebih dari 0.");
        }

        if (produk.getStok() < jumlah) {
            throw new TransaksiException("Stok " + produk.getNamaProduk()
                    + " tidak mencukupi.");
        }

        produk.kurangiStok(jumlah);

        Transaksi transaksi = new Transaksi(pelanggan, produk, jumlah);
        daftarTransaksi.add(transaksi);

        System.out.println("Transaksi berhasil: " + pelanggan.getNama()
                + " membeli " + produk.getNamaProduk()
                + " sebanyak " + jumlah);
    }

    public void jalankanTransaksiDariFile(String namaFile) throws IOException {

        File file = new File(namaFile);

        System.out.println("Lokasi file: " + file.getAbsolutePath());

        if (!file.exists()) {
            throw new IOException("File input_transaksi.txt tidak ditemukan.");
        }

        if (file.length() == 0) {
            throw new IOException("File transaksi kosong.");
        }

        StringBuilder isiFile = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String baris;

            while ((baris = br.readLine()) != null) {
                isiFile.append(baris);
            }
        }

        String[] data = isiFile.toString().split(",", -1);

        for (int i = 0; i < data.length; i += 3) {

            if (data[i].trim().isEmpty()) {
                continue;
            }

            try {
                if (i + 2 >= data.length) {
                    throw new IllegalArgumentException(
                            "Data transaksi tidak lengkap.");
                }

                String id = ambilNilai(data[i], "ID");
                String kode = ambilNilai(data[i + 1], "KD");
                int jumlah = Integer.parseInt(
                        ambilNilai(data[i + 2], "JML")
                );

                prosesTransaksi(id, kode, jumlah);

            } catch (TransaksiException | IllegalArgumentException e) {
                System.out.println("[TRANSAKSI GAGAL] " + e.getMessage());
            }
        }
    }

    private String ambilNilai(String data, String kunci) {

        String[] bagian = data.trim().split("=", 2);

        if (bagian.length != 2 || !bagian[0].trim().equals(kunci)) {
            throw new IllegalArgumentException(
                    "Format data " + kunci + " tidak valid.");
        }

        return bagian[1].trim();
    }

    public void simpanLaporanAkhir() {

        // Object Serialization
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("storage.db"))) {

            out.writeObject(this);
            System.out.println("\nData berhasil disimpan ke storage.db");

        } catch (IOException e) {
            System.out.println("Gagal menyimpan data: " + e.getMessage());
        }

        // File I/O Teks
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("laporan.txt"))) {

            bw.write("LAPORAN TRANSAKSI BESAR");
            bw.newLine();
            bw.write("Nama Toko: " + namaToko);
            bw.newLine();
            bw.write("==========================================");
            bw.newLine();

            bw.write("ID PELANGGAN | NAMA | PRODUK | QTY | KATEGORI");
            bw.newLine();

            for (Transaksi t : daftarTransaksi) {

                if (t.getJumlah() >= 5) {

                    bw.write(
                            t.getPelanggan().getIdPelanggan() + " | "
                            + t.getPelanggan().getNama() + " | "
                            + t.getProduk().getNamaProduk() + " | "
                            + t.getJumlah() + " | "
                            + t.getProduk().getKategori()
                    );

                    bw.newLine();
                }
            }

            bw.write("==========================================");
            bw.newLine();
            bw.write("Total transaksi berhasil: "
                    + daftarTransaksi.size());

            System.out.println("Laporan berhasil disimpan ke laporan.txt");

        } catch (IOException e) {
            System.out.println("Gagal membuat laporan: " + e.getMessage());
        }
    }
}
