export default function (Vue) {
  let data = {};
  Vue.auth = {
    setToken(token, expiration) {
      localStorage.setItem('token', token);
      localStorage.setItem('expiration', expiration);
    },
    setUserRole(role) {
      localStorage.setItem('role', role);
    },
    getUserRole() {
      return localStorage.getItem('role');
    },
    getToken() {
      const token = localStorage.getItem('token');
      const expiration = localStorage.getItem('expiration');
      if (!token || !expiration) {
        return null;
      }
      if (Date.now() > parseInt(expiration)) {
        console.log(`${Date.now()} ${parseInt(expiration)}`);
        localStorage.clear();
        return null;
      }
      return token;
    },
    setAuthenticatedUser(obj) {
      console.log(obj);
      data = obj;
    },
    getAuthenticatedUser() {
      return data;
    },
    isAuthenticated() {
      return !!this.getToken();
    },
    getHeader() {
      const tokenData = window.localStorage.getItem('token');
      return {
        Accept: 'application/json',
        Authorization: `bearer ${tokenData}`,
      };
    },
  };
  Object.defineProperties(Vue.prototype, {
    $auth: {
      get: () => Vue.auth,
    },
  });
}
