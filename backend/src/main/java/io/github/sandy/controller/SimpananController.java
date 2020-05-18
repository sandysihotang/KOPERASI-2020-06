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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return pengaturanSimpananRepository.existsByKoperasi((Integer) koperasi.get("id"));
    }

    @RequestMapping(value = "/api/getaturansimpanan", method = RequestMethod.GET)
    public List<Map<String, Object>> getAturanSimpanan(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return pengaturanSimpananRepository.getAllByKoperasi((Integer) koperasi.get("id"));
    }

    @RequestMapping(value = "/api/saveaturansimpanan", method = RequestMethod.POST)
    public void saveAturanSimpanan(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        simpananService.saveAturanSimpanan(requestbody, koperasiRepository.getOne((Integer) koperasi.get("id")));
    }

    @RequestMapping(value = "/api/aktivasisimpanan/{id}/{jenis_simpanan}", method = RequestMethod.POST)
    public ResponseEntity<Err> saveAktivasiSimpanan(@RequestBody Requestbody requestbody,
                                                    @PathVariable("id") Integer id,
                                                    HttpServletRequest request,
                                                    @PathVariable("jenis_simpanan") Integer jenis_simpanan) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> u = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) u.get("id"));
        User user = userRepository.getOne(id);
        if (aktivasiSimpananRepository.existsByKoperasiAndUserAndJenisSimpanan(koperasiRepository.getOne((Integer) koperasi.get("id")), user, jenis_simpanan)) {
            return new ResponseEntity<>(new Err(400, ""), HttpStatus.OK);
        }
        simpananService.saveActivasiSimpanan(requestbody, koperasi, user, jenis_simpanan);
        return new ResponseEntity<>(new Err(200, ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/getdatapengajusimpanan", method = RequestMethod.GET)
    public List<User> getDataPengajuSimpanan(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> u = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) u.get("id"));
        return userRepository.findAllForPengaju((Integer) koperasi.get("id"));
    }

    @RequestMapping(value = "/api/getdataanggotasimpananwajib", method = RequestMethod.GET)
    public List<Map<String, Object>> getDataAnggotaSimpananWajib(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> u = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) u.get("id"));
        return userRepository.findAllByJenisSimpanan((Integer) koperasi.get("id"), 2);
    }

    @RequestMapping(value = "/api/getdataanggotasimpanansukarela", method = RequestMethod.GET)
    public List<Map<String, Object>> getDataAnggotaSimpananSukarela(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> u = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) u.get("id"));
        return userRepository.findAllByJenisSimpanan((Integer) koperasi.get("id"), 3);
    }

    @RequestMapping(value = "/api/getanggotasimpananaktivasi", method = RequestMethod.GET)
    public List<Map<String, Object>> getAnggotaSimpananAktivasi(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return aktivasiSimpananRepository.findAllByKoperasi((Integer) koperasi.get("id"));
    }

    @RequestMapping(value = "/api/gettransaksisimpanan", method = RequestMethod.GET)
    public List<Map<String, Object>> getTransaksiSimpanan(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return transaksiSimpananRepository.findAllByKoperasi((Integer) koperasi.get("id"));
    }

    @RequestMapping(value = "/api/gettransaksisimpananuser", method = RequestMethod.GET)
    public List<Map<String, Object>> getTransaksiSimpananUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        return transaksiSimpananRepository.findAllByUser((Integer) user.get("id"));
    }

    @RequestMapping(value = "/api/getcolumnmemberfortransaksisimpanan/{id}", method = RequestMethod.GET)
    public String getColumnMemberForTransaksiSimpanan(@PathVariable("id") Integer userId) {
        Map<String, Object> user = userRepository.findFirstById(userId);
        Map<String, Object> koperasi = angotaKoperasiRepository.getByFirstByIdUser((Integer) user.get("id"));
        return (String) daftarAnggotaKoperasiRepository.findByKoperasiId((Integer) koperasi.get("id_koperasi")).get("pattern_field");
    }

    @RequestMapping(value = "/api/getdatamemberfortransaksisimpanan/{id}", method = RequestMethod.GET)
    public Set<Map<String, Object>> getDataForTransaksiSimpanan(@PathVariable("id") int id) {
        return angotaKoperasiRepository.getByUser(id);
    }

    @RequestMapping(value = "/api/getaturansimpanan/{jenis_simpanan}", method = RequestMethod.GET)
    public Map<String, Object> getMaxPengajuan(@PathVariable("jenis_simpanan") int jenisSimpanan, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return pengaturanSimpananRepository.findAturan((Integer) koperasi.get("id"), jenisSimpanan);
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
    public Map<String, Object> saveTransaksiSimpanan(@PathVariable("id") Integer id, @PathVariable("jenis_simpanan") Integer jenisSimpanan) {
//        Map<String, Object> user = userRepository.findFirstById(id);
        return aktivasiSimpananRepository.getFirstByUserAndJenisSimpanan(id, jenisSimpanan);
    }

    @RequestMapping(value = "/api/getsaldouser", method = RequestMethod.GET)
    public Long getSaldoUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> u = userRepository.getUserUsername(uname);
        return aktivasiSimpananRepository.getSaldo((Integer) u.get("id"));
    }

    @RequestMapping(value = "/api/gettransaksiterkini", method = RequestMethod.GET)
    public Set<Map<String, Object>> getTransaksiSimpananTerkini(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        return transaksiSimpananRepository.getTransaksiTerkini((Integer) user.get("id"));
    }

    @RequestMapping(value = "/api/getsimpananuser", method = RequestMethod.GET)
    public List<Map<String, Object>> getSimpananUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        return aktivasiSimpananRepository.getAllByUser((Integer) user.get("id"));
    }

}
