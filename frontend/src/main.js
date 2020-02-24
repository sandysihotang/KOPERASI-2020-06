import Vue from 'vue';
import axios from 'axios';
import App from './components/HelloWorld.vue';
import router from './router';
import store from './store';
import './quasar';

Vue.prototype.$http = axios.create({
  baseURL: 'http://localhost:8089/',
});
new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app');
