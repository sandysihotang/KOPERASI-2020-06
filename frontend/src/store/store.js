import Vuex from 'vuex';
import Vue from 'vue';

Vue.use(Vuex);
export default new Vuex.Store({
  state: {
    layoutNeeded: true,
    isLoginPage: false,
    mobileMode: false,
    menuCollapse: true,
    posts: [],
  },
  mutations: {
    setLayoutNeeded({ state }, value) {
      state.layoutNeeded = value;
    },
    setIsLoginPage({ state }, value) {
      state.isLoginPage = value;
    },
    setMobileMode({ state }, value) {
      state.mobileMode = value;
    },
    setMenuCollapse({ state }, value) {
      state.menuCollapse = value;
    },
    setPosts({ state }, posts) {
      state.posts = posts;
    },
  },
  getters: {
    getLayoutNeeded: state => state.layoutNeeded,
    getIsLoginPage: state => state.isLoginPage,
    getMobileMode: state => state.mobileMode,
    getMenuCollapse: state => state.menuCollapse,
    getPosts: state => state.posts,
  },
});
