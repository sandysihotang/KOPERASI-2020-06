import Vue from 'vue';
import swal from 'vue-sweetalert2';
import VueRouter from 'vue-router';
import Store from '../store/index';

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
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: {
      forAuth: true,
    },
  },
];

const router = new VueRouter({
  routes,
});
router.beforeEach(
  (to, from, next) => {
    if (to.matched.some(record => record.meta.forAuth)) {
      if (!Store.getters.isAuthenticated) {
        next({
          path: '/login',
        });
      } else next();
    } else if (to.matched.some(record => record.meta.forVisitor)) {
      if (Store.getters.isAuthenticated) {
        next({
          path: '/',
        });
      } else next();
    } else next();
  },
);
export default router;
