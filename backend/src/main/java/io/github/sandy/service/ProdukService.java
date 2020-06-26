package io.github.sandy.service;

import io.github.sandy.model.*;
import io.github.sandy.repository.*;
import io.github.sandy.request.Requestbody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProdukService {
    @Autowired
    VendorRepository vendorRepository;

    @Autowired
    KategoriProdukRepository kategoriProdukRepository;

    @Autowired
    ProdukRepository produkRepository;

    @Autowired
    ProdukBaruRepository produkBaruRepository;

    @Autowired
    HargaRepository hargaRepository;

    @Autowired
    TransaksiProdukRepository transaksiProdukRepository;

    @Autowired
    PenjualanProdukRepository penjualanProdukRepository;

    @Autowired
    KoperasiRepository koperasiRepository;

    public Vendor saveVendor(Map<String, Object> koperasi, Requestbody requestbody) {
        Vendor vendor = new Vendor();
        vendor.setAlamatVendor(requestbody.getAlamat());
        vendor.setIdKoperasi((Integer) koperasi.get("id"));
        vendor.setStatus(false);
        vendor.setNamaVendor(requestbody.getName());
        vendor.setNoTelepon(requestbody.getTelepon());
        vendor.setTanggalMasuk(new Date());
        vendorRepository.save(vendor);
        return vendor;
    }

    public void saveKategori(Map<String, Object> koperasi, String kategori) {
        KategoriProduk kategoriProduk = new KategoriProduk();
        kategoriProduk.setIdKoperasi((Integer) koperasi.get("id"));
        kategoriProduk.setNamaKategori(kategori);
        kategoriProdukRepository.save(kategoriProduk);
    }

    public void saveProdukBaru(Requestbody requestbody, Map<String,Object> koperasi, Integer idVendor) {
        Produk produk = new Produk();
        produk.setJumlahProduk(0);
        produk.setIdKoperasi((Integer) koperasi.get("id"));
        produk.setNamaProduk(requestbody.getNamaProduk());
        produk.setKodeProduk(requestbody.getBarCode());
        KategoriProduk kategoriProduk = kategoriProdukRepository.getOne(requestbody.getKategoriProduk());
        produk.setKategoriProduk(kategoriProduk);
        produkRepository.save(produk);

        Harga harga = new Harga();
        harga.setIdProduk(produk.getId());
        harga.setHargaBeli(0);
        harga.setHargaJualNonAnggota(0);
        harga.setHargaJualAnggota(0);
        harga.setStatus(true);
        hargaRepository.save(harga);

        ProdukBaru produkBaru = new ProdukBaru();
        produkBaru.setHargaBeli(requestbody.getHargaBeli());
        produkBaru.setHargaJualAnggota(requestbody.getHargaJualAnggota());
        produkBaru.setHargaJualNonAnggota(requestbody.getHargaJualNonAnggota());
        produkBaru.setIdProduk(produk.getId());
        produkBaru.setJumlahProduk(requestbody.getJumlahBarang());
        produkBaru.setVendor(vendorRepository.getOne(idVendor));
        produkBaruRepository.save(produkBaru);
    }

    public void saveProdukTambah(Requestbody requestbody, Integer idProduk, Integer idVendor) {
        Produk produk = produkRepository.getOne(idProduk);
        Vendor vendor = vendorRepository.getOne(idVendor);

        ProdukBaru produkBaru = new ProdukBaru();
        produkBaru.setVendor(vendor);
        produkBaru.setIdProduk(produk.getId());
        produkBaru.setJumlahProduk(requestbody.getJumlahBarang());
        produkBaru.setHargaBeli(requestbody.getHargaBeli());
        produkBaru.setHargaJualAnggota(requestbody.getHargaJualAnggota());
        produkBaru.setHargaJualNonAnggota(requestbody.getHargaJualNonAnggota());

        produkBaruRepository.save(produkBaru);
    }

    public void simpanProduk(Vendor vendor) {
        Set<ProdukBaru> produkBaru = vendor.getProdukBaru();
        for (ProdukBaru produkBaru1 : produkBaru) {
            Produk produk = produkRepository.getOne(produkBaru1.getIdProduk());
            produk.setJumlahProduk(produk.getJumlahProduk() + produkBaru1.getJumlahProduk());
            produkRepository.save(produk);
            if (hargaRepository.existsByIdProdukAndStatus(produk.getId(), true)) {
                List<Harga> hargas = hargaRepository.getAllByIdProdukAndStatus(produk.getId(), true);
                for (Harga harga : hargas) {
                    Harga harga1 = hargaRepository.getOne(harga.getId());
                    harga1.setStatus(false);
                    hargaRepository.save(harga1);
                }
            }
            Harga harga = new Harga();
            harga.setStatus(true);
            harga.setHargaBeli(produkBaru1.getHargaBeli());
            harga.setHargaJualAnggota(produkBaru1.getHargaJualAnggota());
            harga.setHargaJualNonAnggota(produkBaru1.getHargaJualNonAnggota());
            harga.setIdProduk(produk.getId());
            hargaRepository.save(harga);
        }
        Vendor vendor1 = vendorRepository.getOne(vendor.getId());
        vendor1.setStatus(true);
        vendorRepository.save(vendor1);
    }


    public String getKodeTransaksi(Integer koperasi) {
        String kode = transaksiProdukRepository.getMaxKodePinjaman(koperasi);
        if (kode != null) {
            int noUrut = Integer.parseInt(kode.substring(1, 11));
            noUrut++;
            return String.format("R%010d", noUrut);
        }
        return "R0000000001";
    }

    public void tambahKeranjang(Requestbody requestbody, Map<String, Object> koperasi) {
        PenjualanProduk penjualanProduk = new PenjualanProduk();
        penjualanProduk.setKeanggotaan(requestbody.getAnggota());
        penjualanProduk.setJumlahBeli(requestbody.getJumlahBeli());
        penjualanProduk.setStatus(false);
        penjualanProduk.setIdKoperasi((Integer) koperasi.get("id"));
        Produk produk = produkRepository.getOne(requestbody.getId());
        penjualanProduk.setHarga(hargaRepository.getFirstByIdProdukAndStatus(produk.getId(), true));
        penjualanProdukRepository.save(penjualanProduk);
    }

    public void saveTransaksi(Integer koperasi, Requestbody requestbody) {
        TransaksiProduk transaksiProduk = new TransaksiProduk();
        transaksiProduk.setTanggalTransaksi(new Date());
        transaksiProduk.setKodeTransaksi(getKodeTransaksi(koperasi));
        transaksiProduk.setKeanggotaan(requestbody.getAnggota());
        transaksiProduk.setUangMasuk(requestbody.getUangBeli());
        transaksiProduk.setIdKoperasi(koperasi);

        transaksiProdukRepository.save(transaksiProduk);

        List<PenjualanProduk> penjualanProduks = penjualanProdukRepository.findAllByIdKoperasiAndStatus(koperasi, false);
        for (PenjualanProduk penjualanProduk : penjualanProduks) {
            PenjualanProduk penjualanProduk1 = penjualanProdukRepository.getOne(penjualanProduk.getId());
            penjualanProduk1.setStatus(true);
            penjualanProduk1.setTransaksiProduk(transaksiProduk);
            penjualanProdukRepository.save(penjualanProduk1);
            Produk produk = produkRepository.getOne(penjualanProduk.getHarga().getIdProduk());
            produk.setJumlahProduk(produk.getJumlahProduk() - penjualanProduk.getJumlahBeli());
            produkRepository.save(produk);
        }
    }
}
