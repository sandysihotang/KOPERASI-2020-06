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
              <q-card square class="shadow-24" style="width:300px;height:510px;">
                <q-card-section class="bg-deep-purple-7">
                  <h4 class="text-h5 text-white q-my-md">Registration</h4>
                  <div class="absolute-bottom-right q-pr-md" style="transform: translateY(50%);">
                    <q-btn round icon="close" color="purple-4"/>
                  </div>
                </q-card-section>
                <q-card-section>
                  <q-form class="q-px-sm q-pt-xl q-pb-lg">
                    <q-input square clearable v-model="form.firstName" type="text"
                             label="First Name">
                      <template v-slot:prepend>
                        <q-icon name="fa fa-id-card"/>
                      </template>
                    </q-input>
                    <q-input square clearable v-model="form.lastName" type="text"
                             label="Last Name">
                      <template v-slot:prepend>
                        <q-icon name="fa fa-address-card"/>
                      </template>
                    </q-input>
                    <q-input square clearable v-model="form.username" type="username"
                             label="Username">
                      <template v-slot:prepend>
                        <q-icon name="person"/>
                      </template>
                    </q-input>
                    <q-input square clearable v-model="form.password" type="password"
                             label="Password">
                      <template v-slot:prepend>
                        <q-icon name="lock"/>
                      </template>
                    </q-input>
                  </q-form>
                </q-card-section>
                <q-card-actions class="q-px-lg">
                  <q-btn :loading="loading" @click="register" unelevated
                         size="lg"
                         color="purple-4" class="full-width text-white" label="Get Started">
                    <template v-slot:loading>
                      <q-spinner-hourglass class="on-left"/>
                      Loading...
                    </template>
                  </q-btn>
                </q-card-actions>
                <q-card-section class="text-center q-pa-sm">
                  <p class="text-grey-6">
                    Return to
                    <router-link to="/"> login</router-link>
                  </p>
                </q-card-section>
              </q-card>
            </div>x
          </div>
        </q-page>
      </q-page-container>
    </q-layout>
  </div>
</template>

<script>
export default {
  name: 'Register',
  data() {
    return {
      form: {
        username: '',
        password: '',
        firstName: '',
        lastName: '',
      },
      loading: false,
    };
  },
  methods: {
    register() {
      this.loading = true;
      this.$http.post('/register', this.form)
        .then((e) => {
          const { data } = e;
          if (data.errCode !== 200) {
            this.showAlert(data.err, 'error');
          } else {
            this.showAlert('Registrasi Account Berhasil', 'success');
            this.form.username = null;
            this.form.password = null;
            this.form.firstName = null;
            this.form.lastName = null;
          }
          this.loading = false;
        })
        // eslint-disable-next-line no-unused-vars
        .catch((e) => {
          this.showAlert('Ada masalah dengan jaringan, Refresh Page!', 'error');
          this.loading = false;
        });
    },
  },
  showAlert($title, $status) {
    this.$swal({
      position: 'center',
      type: $status,
      title: $title,
      showConfirmButton: false,
      timer: 1500,
    });
  },
};
</script>

<style>
</style>
