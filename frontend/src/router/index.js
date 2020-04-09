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
    component: () => import('../components/AuthenticatedUser/Koperasi/App.vue'),
    meta: {
      forKoperasi: true,
    },
    children: [{
      path: '',
      component: () => import('../components/AuthenticatedUser/Koperasi/Layout/Dashboard.vue'),
    }, {
      path: '/anggotakoperasi',
      component: () => import('../components/AuthenticatedUser/Koperasi/Layout/AnggotaKoperasi.vue'),
    }],
  },
  {
    path: '/daftarkoperasi',
    name: 'daftarkoperasi',
    component: () => import('../components/AuthenticatedUser/Koperasi/Layout/DaftarKoperasi/DaftarKoperasi.vue'),
    meta: {
      forKoperasi: true,
    },
    children: [
      {
        path: '',
        component: () => import('../components/AuthenticatedUser/Koperasi/Layout/DaftarKoperasi/Layout/FormDaftar.vue'),
      },
    ],
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
          } else {
            next({
              path: '/dashboardkoperasi',
            });
          }
        } else if (Vue.auth.getUserRole() === 'ROLE_admin') {
          next({
            path: '/dashboardadmin',
          });
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
      } else if (Vue.auth.getUserRole() === 'ROLE_koperasi') {
        if (parseInt(Vue.auth.isHaveKoperasi()) === 0) {
          next({
            path: '/daftarkoperasi',
          });
        } else if (parseInt(Vue.auth.isHaveKoperasi()) === 2) {
          next({
            path: '/',
          });
        } else {
          next();
        }
      } else next();
    } else if (to.matched.some(record => record.meta.forAdmin)) {
      if (!Vue.auth.isAuthenticated()) {
        next({
          path: '/login',
        });
      } else if (Vue.auth.isAuthenticated() && Vue.auth.getUserRole() === 'ROLE_koperasi') {
        if (parseInt(Vue.auth.isHaveKoperasi()) === 0) {
          next({
            path: '/daftarkoperasi',
          });
        } else {
          next({
            path: '/dashboardkoperasi',
          });
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
