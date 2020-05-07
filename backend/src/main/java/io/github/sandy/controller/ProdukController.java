package io.github.sandy.controller;

import io.github.sandy.additional.ExportExcel;
import io.github.sandy.model.*;
import io.github.sandy.repository.*;
import io.github.sandy.request.Requestbody;
import io.github.sandy.service.ProdukService;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@RestController
public class ProdukController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProdukService produkService;

    @Autowired
    ProdukRepository produkRepository;

    @Autowired
    KategoriProdukRepository kategoriProdukRepository;

    @Autowired
    VendorRepository vendorRepository;

    @Autowired
    ProdukBaruRepository produkBaruRepository;

    @Autowired
    HargaRepository hargaRepository;

    @Autowired
    PenjualanProdukRepository penjualanProdukRepository;

    @Autowired
    TransaksiProdukRepository transaksiProdukRepository;

    @RequestMapping(value = "/api/simpanvendor", method = RequestMethod.POST)
    public Vendor saveVendor(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        return produkService.saveVendor(user.getKoperasi(), requestbody);
    }

    @RequestMapping(value = "/api/savekategori", method = RequestMethod.POST)
    public void saveKategori(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        produkService.saveKategori(user.getKoperasi(), requestbody.getKategori());
    }

    @RequestMapping(value = "/api/getproduk/{kodeProduk}", method = RequestMethod.GET)
    public HashMap<String, Object> getProdukByKode(@PathVariable("kodeProduk") String kodeProduk, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();
        Boolean exist = produkRepository.existsByKodeProdukAndKoperasi(kodeProduk, user.getKoperasi());
        HashMap<String, Object> found = new HashMap<>();
        found.put("exist", exist);
        if (exist) {
            found.put("data", produkRepository.getFirstByKodeProdukAndKoperasi(kodeProduk, user.getKoperasi()));
        }
        return found;
    }

    @RequestMapping(value = "/api/getoptions", method = RequestMethod.GET)
    public List<KategoriProduk> getOptions(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        return kategoriProdukRepository.getAllByKoperasi(user.getKoperasi());
    }

    @RequestMapping(value = "/api/getprodukbaruvendor/{idVendor}", method = RequestMethod.GET)
    public List<ProdukBaru> getOptions(@PathVariable("idVendor") Integer idVendor) {
        Vendor vendor = vendorRepository.getOne(idVendor);
        return produkBaruRepository.getAllByVendor(vendor);
    }

    @RequestMapping(value = "/api/saveprodukbaru/{id}", method = RequestMethod.POST)
    public void saveProdukBaru(@RequestBody Requestbody requestbody, @PathVariable("id") Integer id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        produkService.saveProdukBaru(requestbody, user.getKoperasi(), id);
    }

    @RequestMapping(value = "/api/saveproduktambah/{idVendor}/{idProduk}", method = RequestMethod.POST)
    public void saveProdukTambah(@RequestBody Requestbody requestbody, @PathVariable("idVendor") Integer idVendor, @PathVariable("idProduk") Integer idProduk) {
        produkService.saveProdukTambah(requestbody, idProduk, idVendor);
    }

    @RequestMapping(value = "/api/ubahproduk/{id}", method = RequestMethod.POST)
    public void ubahProduk(@RequestBody Requestbody requestbody, @PathVariable("id") Integer idProduk) {
        ProdukBaru produkBaru = produkBaruRepository.getOne(idProduk);
        produkBaru.setHargaBeli(requestbody.getHargaBeli());
        produkBaru.setJumlahProduk(requestbody.getJumlahBarang());
        produkBaruRepository.save(produkBaru);
    }

    @RequestMapping(value = "/api/simpan/produk/{id}", method = RequestMethod.POST)
    public void simpanProduk(@PathVariable("id") Integer idProduk) {
        Vendor vendor = vendorRepository.findById(idProduk).get();
        produkService.simpanProduk(vendor);
    }

    @RequestMapping(value = "/api/deleteproduk/{id}", method = RequestMethod.DELETE)
    public void deleteProduk(@PathVariable("id") Integer idProduk) {
        ProdukBaru produkBaru = produkBaruRepository.getOne(idProduk);
        produkBaruRepository.delete(produkBaru);
    }

    @RequestMapping(value = "/api/download/{id}", method = RequestMethod.GET)
    public void download(@PathVariable("id") Integer idVendor, HttpServletResponse response) throws IOException {
        ExportExcel exportExcel = new ExportExcel();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=pemasukanbarang.xlsx");
        ByteArrayInputStream stream = exportExcel.downloadBarangMasuk(vendorRepository.findById(idVendor).get());
        IOUtils.copy(stream, response.getOutputStream());
    }

    @RequestMapping(value = "/api/getdataproduk", method = RequestMethod.GET)
    public Set<Produk> getDataProduk(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        return produkRepository.getAllByKoperasi(user.getKoperasi());
    }

    @RequestMapping(value = "/api/getdatavendorkoperasi", method = RequestMethod.GET)
    public List<Vendor> getDataProdukBaru(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        return vendorRepository.getAllByKoperasiAndStatusOrderByTanggalMasukDesc(user.getKoperasi(), true);
    }

    @RequestMapping(value = "/api/getprodukbyvendor/{id}", method = RequestMethod.GET)
    public List<ProdukBaru> getDataProdukBaruByVendor(@PathVariable("id") Integer idVendor) {
        Vendor vendor = vendorRepository.getOne(idVendor);
        return produkBaruRepository.getAllByVendor(vendor);
    }

    @RequestMapping(value = "/api/getkodetransaksiproduk", method = RequestMethod.GET)
    public String getKodeTransaksiProduk(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        return produkService.getKodeTransaksi(user.getKoperasi());
    }

    @RequestMapping(value = "/api/getprodukbybarcode", method = RequestMethod.POST)
    public Map<String, Object> getKodeTransaksiProduk(HttpServletRequest request, @RequestBody Requestbody requestbody) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        Boolean exist = produkRepository.existsByKodeProdukAndKoperasi(requestbody.getBarCode(), user.getKoperasi());
        HashMap<String, Object> res = new HashMap<>();
        res.put("exist", exist);
        if (exist) {
            Produk produk = produkRepository.getFirstByKodeProdukAndKoperasi(requestbody.getBarCode(), user.getKoperasi());
            Harga harga = hargaRepository.getFirstByProdukAndStatus(produk, true);
            res.put("id", produk.getId());
            res.put("jumlah", produk.getJumlahProduk());
            res.put("harga", (requestbody.getAnggota() ? harga.getHargaJualAnggota() : harga.getHargaJualNonAnggota()));
        }
        return res;
    }

    @RequestMapping(value = "/api/tambahkerangjang", method = RequestMethod.POST)
    public void tambahKeranjang(HttpServletRequest request, @RequestBody Requestbody requestbody) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        produkService.tambahKeranjang(requestbody, user.getKoperasi());
    }

    @RequestMapping(value = "/api/savetransaksi", method = RequestMethod.POST)
    public void saveTransaksi(HttpServletRequest request, @RequestBody Requestbody requestbody) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        produkService.saveTransaksi(user.getKoperasi(), requestbody);
    }

    @RequestMapping(value = "/api/getprodukbeli", method = RequestMethod.GET)
    public List<PenjualanProduk> getPenjualanProduk(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        return penjualanProdukRepository.findAllByKoperasiAndStatus(user.getKoperasi(), false);
    }

    @RequestMapping(value = "/api/deleteprod/{id}", method = RequestMethod.DELETE)
    public void deleteProd(@PathVariable("id") Integer id) {
        PenjualanProduk penjualanProduk = penjualanProdukRepository.getOne(id);
        penjualanProdukRepository.delete(penjualanProduk);
    }

    @RequestMapping(value = "/api/ubahprod/{id}", method = RequestMethod.PUT)
    public void ubahProd(@PathVariable("id") Integer id, @RequestBody Requestbody requestbody) {
        PenjualanProduk penjualanProduk = penjualanProdukRepository.getOne(id);
        penjualanProduk.setJumlahBeli(requestbody.getJumlahBeli());
        penjualanProdukRepository.save(penjualanProduk);
    }

    @RequestMapping(value = "/api/gettransaksiterkiniprod", method = RequestMethod.GET)
    public List<TransaksiProduk> getTransaksiTerkini(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        return transaksiProdukRepository.getAllByKoperasi(user.getKoperasi());
    }

    @RequestMapping(value = "/api/getdetailtransaksiproduk/{id}", method = RequestMethod.GET)
    public List<PenjualanProduk> getDetailTransaksiTerkini(@PathVariable("id") Integer id) {
        TransaksiProduk transaksiProduk = transaksiProdukRepository.getOne(id);
        return penjualanProdukRepository.findAllByTransaksiProduk(transaksiProduk);
    }
}
