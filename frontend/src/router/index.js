import Vue from 'vue';
import swal from 'vue-sweetalert2';
import VueRouter from 'vue-router';

Vue.use(VueRouter);
Vue.use(swal);
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Authentication/Login.vue'),
    meta: {
      forVisitor: true,
    },
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Authentication/Register.vue'),
    meta: {
      forVisitor: true,
    },
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Dashboard.vue'),
    meta: {
      forVisitor: true,
    },
  },
  {
    path: '/dashboardkoperasi',
    name: 'dashboardkoperasi',
    component: () => import('../components/AuthenticatedUser/Koperasi/KSU/App.vue'),
    meta: {
      forKoperasi: true,
    },
    children: [
      {
        path: '',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Layout/Dashboard.vue'),
      },
      {
        path: '/anggotakoperasi',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Layout/AnggotaKoperasi.vue'),
        meta: {
          roolTo: true,
        },
      },
      {
        path: '/pengaturanpendaftarananggota',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Layout/PengaturanFieldDaftarKoperasi.vue'),
        meta: {
          roolTo: true,
        },
      },
      {
        path: '/daftaranggota',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Layout/DaftarAnggotaKoperasi.vue'),
        meta: {
          roolTo: true,
        },
      },
      {
        path: '/pengaturanpinjaman',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Pinjaman/PengaturanPinjaman.vue'),
        meta: {
          roolTo: true,
        },
      },
      {
        path: '/pengaturansimpanan',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Simpanan/PengaturanSimpanan.vue'),
        meta: {
          roolTo: true,
        },
      }, {
        path: '/transaksisimpanan',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Simpanan/TransaksiSimpanan.vue'),
        meta: {
          roolTo: true,
        },
      }, {
        path: '/aktivasisimpanan',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Simpanan/AktivasiSimpanan.vue'),
        meta: {
          roolTo: true,
        },
      },
      {
        path: '/pengajuanpinjaman',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Pinjaman/PengajuanPinjaman.vue'),
        meta: {
          roolTo: true,
        },
      },
      {
        path: '/transaksipinjaman',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Pinjaman/TransaksiPinjaman.vue'),
        meta: {
          roolTo: true,
        },
      }
    ],
  },
  {
    name: 'dashboardkoperasiKSU',
    path: '/dashboardkoperasiksu',
    component: () => import('../components/AuthenticatedUser/Koperasi/KSP/App.vue'),
    meta: {
      forKoperasiKSU: true
    },
    children: [
      {
        path: '',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Layout/Dashboard.vue'),
      },
      {
        path: '/anggotakoperasiksu',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Layout/AnggotaKoperasi.vue'),
        meta: {
          roolToKSU: true,
        },
      },
      {
        path: '/pengaturanpendaftarananggotaksu',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Layout/PengaturanFieldDaftarKoperasi.vue'),
        meta: {
          roolToKSU: true,
        },
      },
      {
        path: '/daftaranggotaksu',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Layout/DaftarAnggotaKoperasi.vue'),
        meta: {
          roolToKSU: true,
        },
      },
      {
        path: '/pengaturanpinjamanksu',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Pinjaman/PengaturanPinjaman.vue'),
        meta: {
          roolToKSU: true,
        },
      },
      {
        path: '/pengaturansimpananksu',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Simpanan/PengaturanSimpanan.vue'),
        meta: {
          roolToKSU: true,
        },
      }, {
        path: '/transaksisimpananksu',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Simpanan/TransaksiSimpanan.vue'),
        meta: {
          roolToKSU: true,
        },
      }, {
        path: '/aktivasisimpananksu',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Simpanan/AktivasiSimpanan.vue'),
        meta: {
          roolToKSU: true,
        },
      },
      {
        path: '/pengajuanpinjamanksu',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Pinjaman/PengajuanPinjaman.vue'),
        meta: {
          roolToKSU: true,
        },
      },
      {
        path: '/transaksipinjamanksu',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Pinjaman/TransaksiPinjaman.vue'),
        meta: {
          roolToKSU: true,
        },
      },
      {
        path: '/daftarproduk',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSP/Produk/DaftarProduk.vue'),
        meta: {
          roolToKSU: true,
        },
      },
      {
        path: '/daftarprodukmasuk',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSP/Produk/PembelianProduk.vue'),
        meta: {
          roolToKSU: true,
        },
      },
      {
        path: '/pengkasiran',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSP/Produk/Pengkasiran.vue'),
        meta: {
          roolToKSU: true,
        },
      },
      {
        path: '/produkterjual',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSP/Produk/ProdukTerjual.vue'),
        meta: {
          roolToKSU: true,
        },
      },
    ],
  },
  {
    path: '/daftarkoperasi',
    name: 'daftarkoperasi',
    component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Layout/DaftarKoperasi/DaftarKoperasi.vue'),
    meta: {
      forKoperasi: true,
    },
    children: [
      {
        path: '',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Layout/DaftarKoperasi/Layout/FormDaftar.vue'),
      },
      {
        path: '/pendingactivation',
        component: () => import('../components/AuthenticatedUser/Koperasi/KSU/Layout/DaftarKoperasi/Layout/PendingActivation.vue'),
      },
    ],
  },
  {
    path: '/dashboardanggotakoperasi',
    name: 'AnggotaKoperasi',
    component: () => import('../components/AuthenticatedUser/Anggota/App.vue'),
    meta: {
      forAnggota: true,
    },
    children: [
      {
        path: '',
        component: () => import('../components/AuthenticatedUser/Anggota/Layout/Dashboard.vue'),
      }, {
        path: '/history',
        component: () => import('../components/AuthenticatedUser/Anggota/Layout/History.vue'),
      },
    ]
  },
  {
    path: '*',
    name: 'notfound',
    component: () => import('../views/NotFound.vue'),
  },
  {
    path: '/dashboardadmin',
    name: 'dashboaradmin',
    component: () => import('../components/AuthenticatedUser/Diskoperindag/App.vue'),
    meta: {
      forAdmin: true,
    },
    children: [
      {
        path: '',
        component: () => import('../components/AuthenticatedUser/Diskoperindag/Layout/AccKoperasiPending.vue'),
      },
      {
        path: '/semuakoperasi',
        component: () => import('../components/AuthenticatedUser/Diskoperindag/Layout/DaftarKoperasi.vue'),
      },
    ],
  },
];

const router = new VueRouter({
  routes,
  scrollBehavior(to, from, next) {
    if (to.hash) {
      return { selector: to.hash };
    }
    return {
      x: 0,
      y: 0,
    };
  },
});
router.beforeEach(
  (to, from, next) => {
    if (to.matched.some(record => record.meta.forVisitor)) {
      if (Vue.auth.isAuthenticated()) {
        if (Vue.auth.getUserRole() === 'ROLE_koperasi') {
          if (parseInt(Vue.auth.isHaveKoperasi()) === 0) {
            next({
              path: '/daftarkoperasi',
            });
          } else if (parseInt(Vue.auth.isHaveKoperasi()) === 2) {
            next({
              path: '/pendingactivation',
            });
          } else if (parseInt(Vue.auth.isHaveKoperasi()) === 3) {
            if (parseInt(localStorage.getItem('jenisKoperasi')) === 2) {
              next({
                path: '/dashboardkoperasi',
              });
            } else {
              next({
                path: '/dashboardkoperasiksu',
              });
            }
          } else {
            next();
          }
        } else if (Vue.auth.getUserRole() === 'ROLE_admin') {
          next({
            path: '/dashboardadmin',
          });
        } else if (Vue.auth.getUserRole() === 'ROLE_anggota') {
          next({
            path: '/dashboardanggotakoperasi',
          });
        } else {
          next();
        }
      } else {
        next();
      }
    } else if (to.matched.some(record => record.meta.forKoperasi)) {
      if (!Vue.auth.isAuthenticated()) {
        next({
          path: '/login',
        });
      } else if (Vue.auth.isAuthenticated() && Vue.auth.getUserRole() === 'ROLE_admin') {
        next({
          path: '/dashboardadmin',
        });
      } else if (Vue.auth.isAuthenticated() && Vue.auth.getUserRole() === 'ROLE_anggota') {
        next({
          path: '/dashboardanggotakoperasi',
        });
      } else if (Vue.auth.getUserRole() === 'ROLE_koperasi') {
        if (parseInt(Vue.auth.isHaveKoperasi()) === 0 && to.path !== '/daftarkoperasi') {
          next({
            path: '/daftarkoperasi',
          });
        } else if (parseInt(Vue.auth.isHaveKoperasi()) === 2 && to.path !== '/pendingactivation') {
          next({
            path: '/pendingactivation',
          });
        } else if (parseInt(Vue.auth.isHaveKoperasi()) === 3) {
          if (parseInt(localStorage.getItem('jenisKoperasi')) === 1) {
            next({
              path: '/dashboardkoperasiksu',
            });
          } else if (to.path !== '/dashboardkoperasi' && from.path !== '/dashboardkoperasi' && !to.matched.some(record => record.meta.roolTo)) {
            next({
              path: '/dashboardkoperasi',
            });
          } else {
            next();
          }
        } else {
          next();
        }
      } else {
        next();
      }
    } else if (to.matched.some(record => record.meta.forKoperasiKSU)) {
      if (!Vue.auth.isAuthenticated()) {
        next({
          path: '/login',
        });
      } else if (Vue.auth.isAuthenticated() && Vue.auth.getUserRole() === 'ROLE_admin') {
        next({
          path: '/dashboardadmin',
        });
      } else if (Vue.auth.isAuthenticated() && Vue.auth.getUserRole() === 'ROLE_anggota') {
        next({
          path: '/dashboardanggotakoperasi',
        });
      } else if (Vue.auth.getUserRole() === 'ROLE_koperasi') {
        if (parseInt(Vue.auth.isHaveKoperasi()) === 0 && to.path !== '/daftarkoperasi') {
          next({
            path: '/daftarkoperasi',
          });
        } else if (parseInt(Vue.auth.isHaveKoperasi()) === 2 && to.path !== '/pendingactivation') {
          next({
            path: '/pendingactivation',
          });
        } else if (parseInt(Vue.auth.isHaveKoperasi()) === 3) {
          if (parseInt(localStorage.getItem('jenisKoperasi')) === 2) {
            next({
              path: '/dashboardkoperasi',
            });
          } else if (to.path !== '/dashboardkoperasiksu' && from.path !== '/dashboardkoperasiksu' && !to.matched.some(record => record.meta.roolToKSU)) {
            next({
              path: '/dashboardkoperasiksu',
            });
          } else {
            next();
          }
        } else {
          next();
        }
      } else {
        next();
      }
    } else if (to.matched.some(record => record.meta.forAdmin)) {
      if (!Vue.auth.isAuthenticated()) {
        next({
          path: '/login',
        });
      } else if (Vue.auth.isAuthenticated() && Vue.auth.getUserRole() === 'ROLE_anggota') {
        next({
          path: '/dashboardanggotakoperasi',
        });
      } else if (Vue.auth.isAuthenticated() && Vue.auth.getUserRole() === 'ROLE_koperasi') {
        if (parseInt(Vue.auth.isHaveKoperasi()) === 0) {
          next({
            path: '/daftarkoperasi',
          });
        } else if (parseInt(Vue.auth.isHaveKoperasi()) === 2) {
          next({
            path: '/pendingactivation',
          });
        } else if (parseInt(Vue.auth.isHaveKoperasi()) === 3) {
          if (parseInt(localStorage.getItem('jenisKoperasi')) === 2) {
            next({
              path: '/dashboardkoperasi',
            });
          } else {
            next({
              path: '/dashboardkoperasiksu',
            });
          }
        } else {
          next();
        }
      } else {
        next();
      }
    } else if (to.matched.some(record => record.meta.forAnggota)) {
      if (!Vue.auth.isAuthenticated()) {
        next({
          path: '/login',
        });
      } else if (Vue.auth.isAuthenticated() && Vue.auth.getUserRole() === 'ROLE_koperasi') {
        if (parseInt(Vue.auth.isHaveKoperasi()) === 0) {
          next({
            path: '/daftarkoperasi',
          });
        } else if (parseInt(Vue.auth.isHaveKoperasi()) === 2) {
          next({
            path: '/pendingactivation',
          });
        } else if (parseInt(Vue.auth.isHaveKoperasi()) === 3) {
          if (parseInt(localStorage.getItem('jenisKoperasi')) === 2) {
            next({
              path: '/dashboardkoperasi',
            });
          } else {
            next({
              path: '/dashboardkoperasiksu',
            });
          }
        } else if (Vue.auth.isAuthenticated() && Vue.auth.getUserRole() === 'ROLE_admin') {
          next({
            path: '/dashboardadmin',
          });
        } else {
          next();
        }
      } else {
        next();
      }
    } else {
      next();
    }
  },
);
export default router;
