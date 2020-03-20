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
    path: '/saas',
    name: 'Ck',
    component: () => import('../views/Dashboard.vue'),
    meta: {
      forAuth: true,
    },
  },
];

const router = new VueRouter({
  routes,
  // eslint-disable-next-line no-unused-vars
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
        next({
          path: '/saas',
        });
      } else {
        next();
      }
    } else if (to.matched.some(record => record.meta.forAuth)) {
      if (!Vue.auth.isAuthenticated()) {
        next({
          path: '/login',
        });
      } else {
        next();
      }
    } else {
      next();
    }
  },
);
export default router;
