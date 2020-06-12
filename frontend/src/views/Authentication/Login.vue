<template>
  <div>
    <q-layout>
      <q-page-container>
        <q-page
          class="window-height window-width row justify-center items-center"
          style="background: radial-gradient(circle at 40% 91%, rgba(251, 251, 251,0.04) 0%, rgba(251, 251, 251,0.04) 50%,rgba(229, 229, 229,0.04) 50%, rgba(229, 229, 229,0.04) 100%),radial-gradient(circle at 66% 97%, rgba(36, 36, 36,0.04) 0%, rgba(36, 36, 36,0.04) 50%,rgba(46, 46, 46,0.04) 50%, rgba(46, 46, 46,0.04) 100%),radial-gradient(circle at 86% 7%, rgba(40, 40, 40,0.04) 0%, rgba(40, 40, 40,0.04) 50%,rgba(200, 200, 200,0.04) 50%, rgba(200, 200, 200,0.04) 100%),radial-gradient(circle at 15% 16%, rgba(99, 99, 99,0.04) 0%, rgba(99, 99, 99,0.04) 50%,rgba(45, 45, 45,0.04) 50%, rgba(45, 45, 45,0.04) 100%),radial-gradient(circle at 75% 99%, rgba(243, 243, 243,0.04) 0%, rgba(243, 243, 243,0.04) 50%,rgba(37, 37, 37,0.04) 50%, rgba(37, 37, 37,0.04) 100%),linear-gradient(90deg, rgb(34, 222, 237),rgb(135, 89, 215));"
        >
          <div class="column q-pa-lg">
            <div class="row">
              <q-form @submit="login">
                <q-card square class="shadow-24" style="width:300px;height:485px;">
                  <q-card-section class="bg-deep-purple-7">
                    <h4 class="text-h5 text-white q-my-md">TobaKo</h4>
                    <div class="absolute-bottom-right q-pr-md" style="transform: translateY(50%);">
                      <router-link to="/register">
                        <q-btn round icon="add" color="purple-4"/>
                      </router-link>
                    </div>
                  </q-card-section>
                  <q-card-section>
                    <div class="q-px-sm q-pt-xl">
                      <q-input square clearable v-model="form.username" type="text"
                               label="Username">
                        <template v-slot:prepend>
                          <q-icon name="person"/>
                        </template>
                      </q-input>
                      <q-input
                        square
                        clearable v-model="form.password" type="password" label="Password">
                        <template v-slot:prepend>
                          <q-icon name="lock"/>
                        </template>
                      </q-input>
                    </div>
                  </q-card-section>
                  <q-card-actions class="q-px-lg">
                    <q-btn :loading="loading" unelevated
                           size="lg"
                           color="purple-4" class="full-width text-white" label="Masuk" type="submit">
                      <template v-slot:loading>
                        <q-spinner-hourglass class="on-left"/>
                        Loading...
                      </template>
                    </q-btn>
                  </q-card-actions>
                  <q-card-section class="text-center q-pa-sm">
                    <p class="text-grey-6">
                      <router-link to="/lupapassword">Lupa password?</router-link>
                    </p>
                  </q-card-section>
                </q-card>
              </q-form>
            </div>
          </div>
        </q-page>
      </q-page-container>
    </q-layout>
  </div>
</template>
<script>
  export default {
    name: 'Login',
    data() {
      return {
        form: {
          username: null,
          password: null,
        },
        loading: false,
      };
    },
    methods: {
      login() {
        this.loading = true;
        const credential = this.checkCredential();
        if (credential) {
          const data = new FormData();
          data.set('grant_type', 'password');
          data.set('username', this.form.username);
          data.set('password', this.form.password);
          this.$http.post('/checkcredential', { username: this.form.username })
            .then((res) => {
              if (res.data.exist) {
                this.$http.post('oauth/token', data, {
                  auth: {
                    username: 'mobile',
                    password: 'pin',
                  },
                })
                  .then((e) => {
                    this.$auth.setToken(e.data.access_token, (e.data.expires_in * 1000) + Date.now());
                    this.$http.get('api/login/currentuser', {
                      headers: this.$auth.getHeader(),
                    })
                      .then((res) => {
                        this.$auth.setAuthenticatedUser(res.data.userDetail);
                        this.$auth.setUserRole(res.data.name);
                        this.$auth.setHaveKoperasi(res.data.haveKoperasi);
                        if (parseInt(localStorage.getItem('havekoperasi')) === 3) {
                          this.$http.get('/api/jeniskoperasi', {
                            headers: this.$auth.getHeader()
                          })
                            .then((res) => {
                              this.$auth.setJenisKoperasi(res.data);
                              window.location.href = '/';
                            })
                            .catch((err) => {
                              this.$swal({
                                position: 'center',
                                type: 'error',
                                title: 'Ada gangguan jaringan silahkan refresh (F5)',
                                showConfirmButton: false,
                                timer: 1500,
                              });
                            });
                        } else {
                          window.location.href = '/';
                        }
                      })
                      .catch((err) => {
                        this.$swal({
                          position: 'center',
                          type: 'error',
                          title: 'Ada gangguan jaringan silahkan refresh (F5)',
                          showConfirmButton: false,
                          timer: 1500,
                        });
                        this.loading = false;
                      });
                    this.loading = false;
                  })
                  .catch((error) => {
                    this.$swal({
                      position: 'center',
                      type: 'error',
                      title: 'Username atau Password yang anda masukkan salah, Refresh(F5)',
                      showConfirmButton: false,
                      timer: 1500,
                    });
                    this.loading = false;
                  });
              } else {
                this.$swal({
                  position: 'center',
                  type: 'error',
                  title: res.data.error,
                  showConfirmButton: false,
                  timer: 1500,
                });
                this.loading = false;
              }
            })
        } else {
          this.$swal({
            position: 'center',
            type: 'error',
            title: 'Silahkan isi Username dan Password',
            showConfirmButton: false,
            timer: 1500,
          });
          this.loading = false;
        }
      },
      checkCredential() {
        return !(!this.form.username || !this.form.password);
      },
    },
  };
</script>

<style>
  .q-card {
    width: 360px;
  }
</style>
