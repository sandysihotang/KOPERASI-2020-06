package io.github.sandy.controller;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.model.*;
import io.github.sandy.repository.*;
import io.github.sandy.request.Requestbody;
import io.github.sandy.service.SimpananService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
public class SimpananController {

    @Autowired
    PengaturanSimpananRepository pengaturanSimpananRepository;

    @Autowired
    DaftarAnggotaKoperasiRepository daftarAnggotaKoperasiRepository;

    @Autowired
    KoperasiRepository koperasiRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SimpananService simpananService;

    @Autowired
    AktivasiSimpananRepository aktivasiSimpananRepository;

    @Autowired
    TransaksiSimpananRepository transaksiSimpananRepository;

    @Autowired
    AngotaKoperasiRepository angotaKoperasiRepository;

    @RequestMapping(value = "/api/existaturansimpanan", method = RequestMethod.GET)
    public boolean existAturanSimpanan(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();
        Koperasi koperasi = koperasiRepository.findFirstByUser(user);
        return pengaturanSimpananRepository.existsByKoperasi(koperasi);
    }

    @RequestMapping(value = "/api/getaturansimpanan", method = RequestMethod.GET)
    public List<PengaturanSimpanan> getAturanSimpanan(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();
        Koperasi koperasi = koperasiRepository.findFirstByUser(user);
        return pengaturanSimpananRepository.getAllByKoperasi(koperasi);
    }

    @RequestMapping(value = "/api/saveaturansimpanan", method = RequestMethod.POST)
    public void saveAturanSimpanan(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();
        Koperasi koperasi = koperasiRepository.findFirstByUser(user);
        simpananService.saveAturanSimpanan(requestbody, koperasi);
    }

    @RequestMapping(value = "/api/aktivasisimpanan/{id}/{jenis_simpanan}", method = RequestMethod.POST)
    public ResponseEntity<Err> saveAktivasiSimpanan(@RequestBody Requestbody requestbody,
                                                    @PathVariable("id") Integer id,
                                                    HttpServletRequest request,
                                                    @PathVariable("jenis_simpanan") Integer jenis_simpanan) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Koperasi koperasi = koperasiRepository.findFirstByUser(userRepository.findByUsername(uname).get());
        User user = userRepository.getOne(id);
        if (aktivasiSimpananRepository.existsByKoperasiAndUserAndJenisSimpanan(koperasi, user, jenis_simpanan)) {
            return new ResponseEntity<>(new Err(400, ""), HttpStatus.OK);
        }
        simpananService.saveActivasiSimpanan(requestbody, koperasi, user, jenis_simpanan);
        return new ResponseEntity<>(new Err(200, ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/getdatapengajusimpanan", method = RequestMethod.GET)
    public List<User> getDataPengajuSimpanan(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Koperasi koperasi = koperasiRepository.findFirstByUser(userRepository.findByUsername(uname).get());
        return userRepository.findAllForPengaju(koperasi.getId());
    }

    @RequestMapping(value = "/api/getdataanggotasimpananwajib", method = RequestMethod.GET)
    public List<User> getDataAnggotaSimpananWajib(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Koperasi koperasi = koperasiRepository.findFirstByUser(userRepository.findByUsername(uname).get());
        return userRepository.findAllByJenisSimpanan(koperasi.getId(), 2);
    }

    @RequestMapping(value = "/api/getdataanggotasimpanansukarela", method = RequestMethod.GET)
    public List<User> getDataAnggotaSimpananSukarela(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Koperasi koperasi = koperasiRepository.findFirstByUser(userRepository.findByUsername(uname).get());
        return userRepository.findAllByJenisSimpanan(koperasi.getId(), 3);
    }

    @RequestMapping(value = "/api/getanggotasimpananaktivasi", method = RequestMethod.GET)
    public List<AktivasiSimpanan> getAnggotaSimpananAktivasi(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Koperasi koperasi = koperasiRepository.findFirstByUser(userRepository.findByUsername(uname).get());
        return aktivasiSimpananRepository.findAllByKoperasi(koperasi);
    }

    @RequestMapping(value = "/api/gettransaksisimpanan", method = RequestMethod.GET)
    public List<TransaksiSimpanan> getTransaksiSimpanan(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Koperasi koperasi = koperasiRepository.findFirstByUser(userRepository.findByUsername(uname).get());
        return transaksiSimpananRepository.findAllByKoperasi(koperasi.getId());
    }

    @RequestMapping(value = "/api/gettransaksisimpananuser", method = RequestMethod.GET)
    public List<TransaksiSimpanan> getTransaksiSimpananUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();
        return transaksiSimpananRepository.findAllByUser(user.getId());
    }

    @RequestMapping(value = "/api/getcolumnmemberfortransaksisimpanan/{id}", method = RequestMethod.GET)
    public String getColumnMemberForTransaksiSimpanan(@PathVariable("id") Integer userId) {
        User user = userRepository.getOne(userId);
        AnggotaKoperasi anggotaKoperasi = angotaKoperasiRepository.getFirstByUser(user);
        return daftarAnggotaKoperasiRepository.findByKoperasiId(anggotaKoperasi.getKoperasi().getId()).get().getPatternField();
    }

    @RequestMapping(value = "/api/getdatamemberfortransaksisimpanan/{id}", method = RequestMethod.GET)
    public Set<AnggotaKoperasi> getDataForTransaksiSimpanan(@PathVariable("id") int id) {
        User user = userRepository.getOne(id);
        return angotaKoperasiRepository.getByUser(user);
    }

    @RequestMapping(value = "/api/getaturansimpanan/{jenis_simpanan}", method = RequestMethod.GET)
    public PengaturanSimpanan getMaxPengajuan(@PathVariable("jenis_simpanan") int jenisSimpanan, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();
        Koperasi koperasi = koperasiRepository.findFirstByUser(user);
        return pengaturanSimpananRepository.findAturan(koperasi.getId(), jenisSimpanan);
    }

    @RequestMapping(value = "/api/transaksisimpanan/{id}", method = RequestMethod.POST)
    public void saveTransaksiSimpanan(@PathVariable("id") Integer id, @RequestBody Requestbody requestbody) {
        simpananService.saveTransaksiSimpanan(id, requestbody);
    }

    @RequestMapping(value = "/api/transaksipenarikansimpanan/{id}", method = RequestMethod.POST)
    public void saveTransaksiPenarikanSimpanan(@PathVariable("id") Integer id, @RequestBody Requestbody requestbody) {
        simpananService.saveTransaksiPenarikanSimpanan(id, requestbody);
    }

    @RequestMapping(value = "/api/getsaldosimpanan/{id}/{jenis_simpanan}", method = RequestMethod.GET)
    public AktivasiSimpanan saveTransaksiSimpanan(@PathVariable("id") Integer id, @PathVariable("jenis_simpanan") Integer jenisSimpanan) {
        User user = userRepository.getOne(id);
        return aktivasiSimpananRepository.getFirstByUserAndJenisSimpanan(user, jenisSimpanan);
    }

    @RequestMapping(value = "/api/getsaldouser", method = RequestMethod.GET)
    public Long getSaldoUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();
        return aktivasiSimpananRepository.getSaldo(user.getId());
    }

    @RequestMapping(value = "/api/gettransaksiterkini", method = RequestMethod.GET)
    public Set<TransaksiSimpanan> getTransaksiSimpananTerkini(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();
        return transaksiSimpananRepository.getTransaksiTerkini(user.getId());
    }

    @RequestMapping(value = "/api/getsimpananuser", method = RequestMethod.GET)
    public List<AktivasiSimpanan> getSimpananUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();
        return aktivasiSimpananRepository.getAllByUser(user);
    }

}
