package io.github.sandy.service;

import io.github.sandy.model.*;
import io.github.sandy.repository.*;
import io.github.sandy.request.Requestbody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

    public Vendor saveVendor(Koperasi koperasi, Requestbody requestbody) {
        Vendor vendor = new Vendor();
        vendor.setAlamatVendor(requestbody.getAlamat());
        vendor.setKoperasi(koperasi);
        vendor.setStatus(false);
        vendor.setNamaVendor(requestbody.getName());
        vendor.setNoTelepon(requestbody.getTelepon());
        vendor.setTanggalMasuk(new Date());
        vendorRepository.save(vendor);
        return vendor;
    }

    public void saveKategori(Koperasi koperasi, String kategori) {
        KategoriProduk kategoriProduk = new KategoriProduk();
        kategoriProduk.setKoperasi(koperasi);
        kategoriProduk.setNamaKategori(kategori);
        kategoriProdukRepository.save(kategoriProduk);
    }

    public void saveProdukBaru(Requestbody requestbody, Koperasi koperasi, Integer idVendor) {
        Produk produk = new Produk();
        produk.setJumlahProduk(0);
        produk.setKoperasi(koperasi);
        produk.setNamaProduk(requestbody.getNamaProduk());
        produk.setKodeProduk(requestbody.getBarCode());
        KategoriProduk kategoriProduk = kategoriProdukRepository.getOne(requestbody.getKategoriProduk());
        produk.setKategoriProduk(kategoriProduk);
        produkRepository.save(produk);

        Harga harga = new Harga();
        harga.setProduk(produk);
        harga.setHargaBeli(0);
        harga.setHargaJualNonAnggota(0);
        harga.setHargaJualAnggota(0);
        harga.setStatus(true);
        hargaRepository.save(harga);

        ProdukBaru produkBaru = new ProdukBaru();
        produkBaru.setHargaBeli(requestbody.getHargaBeli());
        produkBaru.setHargaJualAnggota(requestbody.getHargaJualAnggota());
        produkBaru.setHargaJualNonAnggota(requestbody.getHargaJualNonAnggota());
        produkBaru.setProduk(produk);
        produkBaru.setJumlahProduk(requestbody.getJumlahBarang());
        produkBaru.setVendor(vendorRepository.getOne(idVendor));
        produkBaruRepository.save(produkBaru);
    }

    public void saveProdukTambah(Requestbody requestbody, Integer idProduk, Integer idVendor) {
        Produk produk = produkRepository.getOne(idProduk);
        Vendor vendor = vendorRepository.getOne(idVendor);

        ProdukBaru produkBaru = new ProdukBaru();
        produkBaru.setVendor(vendor);
        produkBaru.setProduk(produk);
        produkBaru.setJumlahProduk(requestbody.getJumlahBarang());
        produkBaru.setHargaBeli(requestbody.getHargaBeli());
        produkBaru.setHargaJualAnggota(requestbody.getHargaJualAnggota());
        produkBaru.setHargaJualNonAnggota(requestbody.getHargaJualNonAnggota());

        produkBaruRepository.save(produkBaru);
    }

    public void simpanProduk(Vendor vendor) {
        Set<ProdukBaru> produkBaru = vendor.getProdukBaru();
        for (ProdukBaru produkBaru1 : produkBaru) {
            Produk produk = produkRepository.getOne(produkBaru1.getProduk().getId());
            produk.setJumlahProduk(produk.getJumlahProduk() + produkBaru1.getJumlahProduk());
            produkRepository.save(produk);
            if (hargaRepository.existsByProdukAndStatus(produk, true)) {
                List<Harga> hargas = hargaRepository.getAllByProdukAndStatus(produk, true);
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
            harga.setProduk(produk);
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

    public void tambahKeranjang(Requestbody requestbody, Koperasi koperasi) {
        PenjualanProduk penjualanProduk = new PenjualanProduk();
        penjualanProduk.setKeanggotaan(requestbody.getAnggota());
        penjualanProduk.setJumlahBeli(requestbody.getJumlahBeli());
        penjualanProduk.setStatus(false);
        penjualanProduk.setKoperasi(koperasi);
        Produk produk = produkRepository.getOne(requestbody.getId());
        penjualanProduk.setHarga(hargaRepository.getFirstByProdukAndStatus(produk, true));
        penjualanProdukRepository.save(penjualanProduk);
    }

    public void saveTransaksi(Integer koperasi, Requestbody requestbody) {
        TransaksiProduk transaksiProduk = new TransaksiProduk();
        transaksiProduk.setTanggalTransaksi(new Date());
        transaksiProduk.setKodeTransaksi(getKodeTransaksi(koperasi));
        transaksiProduk.setKeanggotaan(requestbody.getAnggota());
        transaksiProduk.setUangMasuk(requestbody.getUangBeli());
        Koperasi koperasi1 = koperasiRepository.getOne(koperasi);
        transaksiProduk.setKoperasi(koperasi1);

        transaksiProdukRepository.save(transaksiProduk);

        List<PenjualanProduk> penjualanProduks = penjualanProdukRepository.findAllByKoperasiAndStatus(koperasi1, false);
        for (PenjualanProduk penjualanProduk : penjualanProduks) {
            PenjualanProduk penjualanProduk1 = penjualanProdukRepository.getOne(penjualanProduk.getId());
            penjualanProduk1.setStatus(true);
            penjualanProduk1.setTransaksiProduk(transaksiProduk);
            penjualanProdukRepository.save(penjualanProduk1);
            Produk produk = produkRepository.getOne(penjualanProduk.getHarga().getProduk().getId());
            produk.setJumlahProduk(produk.getJumlahProduk() - penjualanProduk.getJumlahBeli());
            produkRepository.save(produk);
        }
    }
}
