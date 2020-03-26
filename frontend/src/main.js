import Vue from 'vue';
import axios from 'axios';
import App from './components/HelloWorld.vue';
import Auth from './store/index';
import router from './router';
import './quasar';

Vue.use(Auth);
Vue.prototype.$http = axios.create({
  baseURL: 'http://localhost:8089/',
});
new Vue({
  router,
  store: () => import('./store/store'),
  render: h => h(App),
}).$mount('#app');
