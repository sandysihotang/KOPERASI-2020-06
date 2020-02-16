import Vue from 'vue';
import axios from 'axios';
import VueAxios from 'vue-axios';
import About from './views/Login.vue';
import router from './router';
import store from './store';
import './quasar';


const http = axios.create({
  // comment base url if your task deploy to prod
  baseURL: 'http://localhost:8089',
  headers: {
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json;charset=UTF-8',
  },
});

Vue.use(VueAxios, http);
new Vue({
  router,
  store,
  render: h => h(About),
}).$mount('#app');
