package io.github.sandy.service;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.config.AuthorizationServerConfiguration;
import io.github.sandy.model.*;
import io.github.sandy.repository.*;
import io.github.sandy.request.Requestbody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class KoperasiService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleUserRepository roleUserRepository;

    @Autowired
    DetailUserRepository detailUserRepository;

    @Autowired
    AuthorizationServerConfiguration auth;

    @Autowired
    KoperasiRepository koperasiRepository;

    @Autowired
    DaftarAnggotaKoperasiRepository daftarAnggotaKoperasiRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    AngotaKoperasiRepository angotaKoperasiRepository;

    @Autowired
    KoperasiPengaturanPinjamanRepository koperasiPengaturanPinjamanRepository;

    @Autowired
    PengaturanPinjamanRepository pengaturanPinjamanRepository;

    public Err createKoperasi(Requestbody requestbody, String uname) {
        User user = userRepository.findByUsername(uname).get();

        Koperasi koperasi = new Koperasi();

        koperasi.setAlamatKoperasi(requestbody.getAlamat());
        koperasi.setNamaKoperasi(requestbody.getName());
        koperasi.setJenisKoperasi(requestbody.getJenis().trim().equals("Koperasi Serba Usaha (KSU)") ? 2 : 1);
        koperasi.setNamaPendiri(requestbody.getPendiri());
        koperasi.setNoIzinKoperasi(requestbody.getIzin());
        koperasi.setTahunBerdiriKoperasi(new Date(requestbody.getDate()));
        koperasi.setEmail(requestbody.getEmail());
        koperasi.setUser(user);

        koperasiRepository.save(koperasi);

        User user1 = userRepository.getOne(user.getId());
        user1.setHaveKoperasi(2);
        userRepository.save(user1);

        return new Err(200, "Koperasi berhasil");
    }

    public void changeStateKoperasi(int id, boolean state) {
        Koperasi koperasi = koperasiRepository.findById(id).get();
        MailSender mailSender = new MailSender();
        mailSender.sendEmailSetStateKoperasi(javaMailSender, koperasi.getEmail(), (state ? "Koperasi Telah Diaktifkan" : "Maaf Koperasi dinonaktifkan"));

        User user = userRepository.getOne(koperasi.getUser().getId());
        user.setHaveKoperasi((!state ? 3 : 2));
        userRepository.save(user);

    }

    public void saveFormRegisterMember(String uname, String pattern) {
        User user = userRepository.findByUsername(uname).get();
        Koperasi koperasi = user.getKoperasi();


        FieldDaftarAnggota fieldDaftarAnggota = new FieldDaftarAnggota();
        fieldDaftarAnggota.setKoperasi(koperasi);
        fieldDaftarAnggota.setPatternField(pattern);

        daftarAnggotaKoperasiRepository.save(fieldDaftarAnggota);

        Koperasi changeStateForm = koperasiRepository.getOne(koperasi.getId());
        koperasi.setHaveFieldRegisterMember(true);
        koperasiRepository.save(changeStateForm);
    }

    public void saveUser(Requestbody requestbody, Koperasi koperasi) {
        User user = new User();
        user.setUsername(requestbody.getUsername());
        user.setPassword(auth.passwordEncoder.encode(requestbody.getPassword()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setEmail(requestbody.getEmail());
        userRepository.save(user);

        Role role = roleRepository.findFirstByName("ROLE_anggota");
        RoleUser roleUser = new RoleUser(role.getId(), user.getId());
        roleUserRepository.save(roleUser);

        UserDetail userDetail = new UserDetail();
        userDetail.setFirstName(requestbody.getFirstName());
        userDetail.setLastName(requestbody.getLastName());
        userDetail.setUser(user);
        userDetail.setAddress(requestbody.getAlamat());
        userDetail.setNoTelepon(requestbody.getTelepon());
        detailUserRepository.save(userDetail);

        AnggotaKoperasi anggotaKoperasi = new AnggotaKoperasi();
        anggotaKoperasi.setData(requestbody.getFieldData());
        anggotaKoperasi.setKoperasi(koperasi);
        anggotaKoperasi.setUser(user);

        angotaKoperasiRepository.save(anggotaKoperasi);
    }

    public void savePengaturanPeminjaman(Koperasi koperasi, Requestbody requestbody) {
        PengaturanPinjaman pengaturanPinjaman = new PengaturanPinjaman();
        pengaturanPinjaman.setBungaPinjaman(requestbody.getBungaPinjaman() / 100);
        pengaturanPinjaman.setAmbangBatasDenda(requestbody.getAmbangBatasDenda());
        pengaturanPinjaman.setMaxTenor(requestbody.getMaxTenor());
        pengaturanPinjaman.setMinTenor(requestbody.getMinTenor());
        pengaturanPinjaman.setPersentaseDenda(requestbody.getPersentaseDenda() / 100);

        pengaturanPinjamanRepository.save(pengaturanPinjaman);
        if (koperasiPengaturanPinjamanRepository.existsByKoperasiAndStatus(koperasi, true)) {
            KoperasiPengaturanPinjaman koperasiPengaturanPinjaman = koperasiPengaturanPinjamanRepository.getFirstByKoperasiAndStatus(koperasi, true).get();
            KoperasiPengaturanPinjaman update = koperasiPengaturanPinjamanRepository.getOne(koperasiPengaturanPinjaman.getId());
            update.setStatus(false);
            koperasiPengaturanPinjamanRepository.save(update);
        }
        KoperasiPengaturanPinjaman koperasiPengaturanPinjaman = new KoperasiPengaturanPinjaman();
        koperasiPengaturanPinjaman.setStatus(true);
        koperasiPengaturanPinjaman.setKoperasi(koperasi);
        koperasiPengaturanPinjaman.setPengaturanPinjaman(pengaturanPinjaman);
        koperasiPengaturanPinjamanRepository.save(koperasiPengaturanPinjaman);
    }
}
