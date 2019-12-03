import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import './quasar';
import Pusher from 'pusher-js';


Pusher.logToConsole = false;
const pusher = new Pusher('b62a6641110833445445', {
  cluster: 'ap1',
  forceTLS: true,
});

var channel = pusher.subscribe('my-channel');
channel.bind('my-event', e => {
  console.log(e);
});
Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app');
