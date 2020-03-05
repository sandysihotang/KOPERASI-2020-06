import Vue from 'vue';
import Vuex from 'vuex';
// import createPersistedState from 'vuex-persistedstate';
// import * as Cookies from 'js-cookie';

Vue.use(Vuex);

export default new Vuex.Store({
  // plugins: [
  //   createPersistedState({
  //     getState: key => Cookies.getJSON(key),
  //     setState: (key, state) => Cookies.set(key, state, {
  //       expires: 30000,
  //       secure: true,
  //     }),
  //   }),
  // ],
  state: {
    header: {
      Accept: 'application/json',
      Authorization: `bearer ${localStorage.getItem('token')}`,
    },
    user: JSON.parse(localStorage.getItem('user')),
    isAuthentication: (localStorage.getItem('isAuth') === 'true'),
  },
  mutations: {
    // eslint-disable-next-line no-empty-pattern
    SET_AUTHENTICATED_USER({}, obj) {
      localStorage.setItem('user', JSON.stringify(obj));
    },
    // eslint-disable-next-line no-empty-pattern
    SET_IS_AUTHENTICATED({}, token) {
      localStorage.setItem('isAuth', (token != null));
    },
  },
  actions: {
    setUser({ commit }, obj) {
      commit('SET_AUTHENTICATED_USER', obj);
      commit('SET_IS_AUTHENTICATED', localStorage.getItem('token'));
    },
    // eslint-disable-next-line no-empty-pattern
    setToken({}, token) {
      localStorage.setItem('token', token.access_token);
      localStorage.setItem('expiration', token.expires_in + Date.now());
    },
  },
  modules: {},
  getters: {
    user: state => state.user,
    getToken: () => {
      const token = localStorage.getItem('token');
      const expiration = localStorage.getItem('expiration');
      if (!token || !expiration) {
        return null;
      }
      // eslint-disable-next-line radix
      const exp = parseInt(expiration);
      if (Date.now() > exp) {
        localStorage.clear();
        return null;
      }
      return token;
    },
    isAuthenticated: state => state.isAuthentication,
  },
});
