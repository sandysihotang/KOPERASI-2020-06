export default function (Vue) {
  let data = {};
  // eslint-disable-next-line no-param-reassign
  Vue.auth = {
    setToken(token, expiration) {
      localStorage.setItem('token', token);
      localStorage.setItem('expiration', expiration);
    },
    getToken() {
      const token = localStorage.getItem('token');
      const expiration = localStorage.getItem('expiration');
      if (!token || !expiration) {
        return null;
      }
      // eslint-disable-next-line radix
      if (Date.now() > parseInt(expiration)) {
        localStorage.clear();
        return null;
      }
      return token;
    },
    setAuthenticatedUser(obj) {
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
