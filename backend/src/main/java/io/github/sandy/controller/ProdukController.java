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

    @Autowired
    KoperasiRepository koperasiRepository;

    @Autowired
    ExportExcel exportExcel;

    @RequestMapping(value = "/api/simpanvendor", method = RequestMethod.POST)
    public Vendor saveVendor(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> u = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) u.get("id"));
        return produkService.saveVendor(koperasi, requestbody);
    }

    @RequestMapping(value = "/api/savekategori", method = RequestMethod.POST)
    public void saveKategori(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> u = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) u.get("id"));
        produkService.saveKategori(koperasi, requestbody.getKategori());
    }

    @RequestMapping(value = "/api/getproduk/{kodeProduk}", method = RequestMethod.GET)
    public HashMap<String, Object> getProdukByKode(@PathVariable("kodeProduk") String kodeProduk, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> u = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) u.get("id"));
        Boolean exist = produkRepository.getExistByKodeProdukAndKoperasi(kodeProduk, (Integer)koperasi.get("id"));
        HashMap<String, Object> found = new HashMap<>();
        found.put("exist", exist);
        if (exist) {
            found.put("data", produkRepository.getFirstForScan(kodeProduk, (Integer)koperasi.get("id")));
        }
        return found;
    }

    @RequestMapping(value = "/api/getoptions", method = RequestMethod.GET)
    public List<Map<String, Object>> getOptions(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return kategoriProdukRepository.getAllByKoperasi((Integer) koperasi.get("id"));
    }

    @RequestMapping(value = "/api/getprodukbaruvendor/{idVendor}", method = RequestMethod.GET)
    public List<Map<String, Object>> getOptions(@PathVariable("idVendor") Integer idVendor) {
        Vendor vendor = vendorRepository.getOne(idVendor);
        return produkBaruRepository.getPembelianProdukBaru(vendor.getId());
    }

    @RequestMapping(value = "/api/saveprodukbaru/{id}", method = RequestMethod.POST)
    public void saveProdukBaru(@RequestBody Requestbody requestbody, @PathVariable("id") Integer id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        produkService.saveProdukBaru(requestbody, koperasi, id);
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
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=pemasukanbarang.xlsx");
        ByteArrayInputStream stream = exportExcel.downloadBarangMasuk(vendorRepository.findById(idVendor).get());
        IOUtils.copy(stream, response.getOutputStream());
    }

    @RequestMapping(value = "/api/getdataproduk", method = RequestMethod.GET)
    public Set<Map<String, Object>> getDataProduk(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return produkRepository.getAllByKoperasi((Integer) koperasi.get("id"));
    }

    @RequestMapping(value = "/api/getdatavendorkoperasi", method = RequestMethod.GET)
    public List<Map<String, Object>> getDataProdukBaru(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return vendorRepository.getAllByKoperasiAndStatusOrderByTanggalMasukDesc((Integer) koperasi.get("id"), true);
    }

    @RequestMapping(value = "/api/getprodukbyvendor/{id}", method = RequestMethod.GET)
    public List<Map<String, Object>> getDataProdukBaruByVendor(@PathVariable("id") Integer idVendor) {
        Vendor vendor = vendorRepository.getOne(idVendor);
        return produkBaruRepository.getPembelianProdukBaru(vendor.getId());
    }

    @RequestMapping(value = "/api/getkodetransaksiproduk", method = RequestMethod.GET)
    public String getKodeTransaksiProduk(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return produkService.getKodeTransaksi((Integer) koperasi.get("id"));
    }

    @RequestMapping(value = "/api/getprodukbybarcode", method = RequestMethod.POST)
    public Map<String, Object> getKodeTransaksiProduk(HttpServletRequest request, @RequestBody Requestbody requestbody) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Boolean exist = produkRepository.getExistByKodeProdukAndKoperasi(requestbody.getBarCode(), (Integer) koperasi.get("id"));
        HashMap<String, Object> res = new HashMap<>();
        res.put("exist", exist);
        if (exist) {
            Map<String,Object> produk =  produkRepository.getFirstForScan(requestbody.getBarCode(), (Integer) koperasi.get("id"));
            Harga harga = hargaRepository.getFirstByIdProdukAndStatus((Integer) produk.get("id"), true);
            res.put("id", produk.get("id"));
            res.put("jumlah", produk.get("jumlah_produk"));
            res.put("harga", (requestbody.getAnggota() ? harga.getHargaJualAnggota() : harga.getHargaJualNonAnggota()));
        }
        return res;
    }

    @RequestMapping(value = "/api/tambahkerangjang", method = RequestMethod.POST)
    public void tambahKeranjang(HttpServletRequest request, @RequestBody Requestbody requestbody) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        produkService.tambahKeranjang(requestbody, koperasi);
    }

    @RequestMapping(value = "/api/savetransaksi", method = RequestMethod.POST)
    public void saveTransaksi(HttpServletRequest request, @RequestBody Requestbody requestbody) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        produkService.saveTransaksi((Integer) koperasi.get("id"), requestbody);
    }

    @RequestMapping(value = "/api/getprodukbeli", method = RequestMethod.GET)
    public List<Map<String, Object>> getPenjualanProduk(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return penjualanProdukRepository.findAllByKoperasiAndStatusPen((Integer) koperasi.get("id"), false);
    }

    @RequestMapping(value = "/api/deleteprod/{id}", method = RequestMethod.DELETE)
    public void deleteProd(@PathVariable("id") Integer id) {
        PenjualanProduk penjualanProduk = penjualanProdukRepository.getOne(id);
        penjualanProdukRepository.delete(penjualanProduk);
    }

    @RequestMapping(value = "/api/deleteallprod", method = RequestMethod.DELETE)
    public void deleteProd(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        penjualanProdukRepository.deleteAllByKoperasiAndStatus((Integer) koperasi.get("id"), false);
    }

    @RequestMapping(value = "/api/ubahprod/{id}", method = RequestMethod.PUT)
    public void ubahProd(@PathVariable("id") Integer id, @RequestBody Requestbody requestbody) {
        PenjualanProduk penjualanProduk = penjualanProdukRepository.getOne(id);
        penjualanProduk.setJumlahBeli(requestbody.getJumlahBeli());
        penjualanProdukRepository.save(penjualanProduk);
    }

    @RequestMapping(value = "/api/gettransaksiterkiniprod", method = RequestMethod.GET)
    public List<Map<String, Object>> getTransaksiTerkini(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return transaksiProdukRepository.getAllByKoperasi((Integer) koperasi.get("id"));
    }

    @RequestMapping(value = "/api/getdetailtransaksiproduk/{id}", method = RequestMethod.GET)
    public List<Map<String, Object>> getDetailTransaksiTerkini(@PathVariable("id") Integer id) {
        return penjualanProdukRepository.findAllByTransaksiProduk(id);
    }
}
