import Vue from 'vue';
import swal from 'vue-sweetalert2';
import VueRouter from 'vue-router';
import Login from '../views/Authentication/Login.vue';

Vue.use(VueRouter);
Vue.use(swal);
const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login,
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Authentication/Register.vue'),
  },
];

const router = new VueRouter({
  routes,
});

export default router;
