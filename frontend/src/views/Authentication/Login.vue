<template>
  <div>
    <q-layout>
      <q-page-container>
        <q-page
          class="window-height window-width row justify-center items-center"
          style="background: linear-gradient(#8274C5, #5A4A9F);"
        >
          <div class="column q-pa-lg">
            <div class="row">
              <q-card square class="shadow-24" style="width:300px;height:485px;">
                <q-card-section class="bg-deep-purple-7">
                  <h4 class="text-h5 text-white q-my-md">Koperasi</h4>
                  <div class="absolute-bottom-right q-pr-md" style="transform: translateY(50%);">
                    <router-link to="/register">
                      <q-btn round icon="add" color="purple-4"/>
                    </router-link>
                  </div>
                </q-card-section>
                <q-card-section>
                  <q-form class="q-px-sm q-pt-xl">
                    <q-input square clearable v-model="form.username" type="text" label="Username">
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
                  </q-form>
                </q-card-section>
                <q-card-actions class="q-px-lg">
                  <q-btn :loading="loading" @click="login"  unelevated
                         size="lg"
                         color="purple-4" class="full-width text-white" label="Sign In">
                    <template v-slot:loading>
                      <q-spinner-hourglass class="on-left"/>
                      Loading...
                    </template>
                  </q-btn>
                </q-card-actions>
                <q-card-section class="text-center q-pa-sm">
                  <p class="text-grey-6">Forgot your password?</p>
                </q-card-section>
              </q-card>
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
                this.$auth.setAuthenticatedUser(res.data.userAuthentication.principal.userDetail);
                this.$auth.setUserRole(res.data.userAuthentication.principal.roles[0].name);
                this.$auth.setHaveKoperasi(res.data.userAuthentication.principal.haveKoperasi);
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
                this.loading = false;
              });
            this.loading = false;
          })
          .catch((error) => {
            if (error.response.data.error_description === 'User is disabled') {
              this.$swal({
                position: 'center',
                type: 'error',
                title: 'Silahkan Tunggu Konfirmasi dari Diskoperindag daerah anda',
                showConfirmButton: false,
                timer: 1500,
              });
            }
            this.loading = false;
          });
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
