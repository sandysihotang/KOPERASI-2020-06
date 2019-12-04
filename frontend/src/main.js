import Vue from 'vue';
import Pusher from 'pusher-js';
import axios from 'axios';
import VueAxios from 'vue-axios';
import App from './App.vue';
import router from './router';
import store from './store';
import './quasar';


const http = axios.create({
  // comment base url if your task deploy to prod
  // baseURL: 'http://localhost:8089',
});

Pusher.logToConsole = false;
const pusher = new Pusher('b62a6641110833445445', {
  cluster: 'ap1',
  forceTLS: true,
});

const channel = pusher.subscribe('my-channel');

// eslint-disable-next-line no-unused-vars
channel.bind('my-event', (e) => {
  // console.log(e);
});
Vue.config.productionTip = false;


Vue.use(VueAxios, http);
new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app');
