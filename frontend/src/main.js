import Vue from 'vue';
import axios from 'axios';
import App from './components/HelloWorld.vue';
import store from './store';
import router from './router';
import './quasar';

Vue.prototype.$http = axios.create({
  baseURL: 'http://localhost:8089/',
});
new Vue({
  store,
  router,
  render: h => h(App),
}).$mount('#app');
