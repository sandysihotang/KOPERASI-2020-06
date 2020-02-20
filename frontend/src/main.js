import Vue from 'vue';
import axios from 'axios';
import About from './views/Login.vue';
import router from './router';
import store from './store';
import './quasar';

Vue.prototype.$http = axios.create({
  // comment push deploy
  baseURL: 'http://localhost:8089/',
});
new Vue({
  router,
  store,
  render: h => h(About),
}).$mount('#app');
