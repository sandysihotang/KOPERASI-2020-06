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
              <q-card square class="shadow-24" style="width:600px;height:540px;">
                <q-card-section class="bg-deep-purple-6">
                  <div class="row justify-between">
                    <div class="col-4">
                      <span class="text-h6 text-white q-my-md">Registration</span>
                    </div>
                    <div class="col-4"></div>
                    <div class="col-4">
                      <router-link class="text-h6 text-white" to="/login">Login</router-link>
                    </div>
                  </div>
                </q-card-section>
                <q-card-section>
                  <q-form class="q-px-sm q-pb-lg">
                    <q-stepper
                      v-model="step"
                      ref="stepper"
                      color="primary"
                      animated
                    >
                      <q-step
                        :name="1"
                        title="Account"
                        icon="settings"
                        :done="step > 1"
                      >
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
                      </q-step>

                      <q-step
                        :name="2"
                        title="Data Diri"
                        icon="create_new_folder"
                        :done="step > 2"
                      >
                        <q-input square clearable v-model="form.email" type="email"
                                 label="Email">
                          <template v-slot:prepend>
                            <q-icon name="email"/>
                          </template>
                        </q-input>
                        <q-input v-model="form.alamat" type="text" label="Alamat Lengkap">
                          <template v-slot:prepend>
                            <q-icon name="place"/>
                          </template>
                        </q-input>
                        <q-input square clearable v-model="form.telepon" type="text"
                                 label="No Telepon">
                          <template v-slot:prepend>
                            <q-icon name="phone"/>
                          </template>
                        </q-input>
                      </q-step>

                      <template v-slot:navigation>
                        <q-stepper-navigation>
                          <q-btn v-if="step===2" :loading="loading" @click="register" unelevated
                                 size="lg"
                                 color="purple-4" class="full-width text-white" label="Get Started">
                            <template v-slot:loading>
                              <q-spinner-hourglass class="on-left"/>
                              Loading...
                            </template>
                          </q-btn>
                          <q-btn v-else @click="$refs.stepper.next()" unelevated
                                 size="lg"
                                 color="purple-4" class="full-width text-white" label="Continue">
                          </q-btn>
                          <q-btn v-if="step > 1" flat color="primary"
                                 @click="$refs.stepper.previous()" label="Back" class="q-ml-sm"/>
                        </q-stepper-navigation>
                      </template>
                    </q-stepper>
                  </q-form>
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
  name: 'Register',
  data() {
    return {
      step: 1,
      form: {
        username: '',
        password: '',
        firstName: '',
        lastName: '',
        email: '',
        telepon: '',
        alamat: '',
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
          this.showAlert('Registrasi Account Berhasil', 'success');
          this.form.username = null;
          this.form.password = null;
          this.form.firstName = null;
          this.form.lastName = null;
          this.form.email = null;
          this.form.alamat = null;
          this.form.telepon = null;

          this.loading = false;
        })
        .catch((error) => {
          this.showAlert(error.response.data.error_description, 'error');
          this.loading = false;
        });
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
  },
};
</script>

<style>
</style>
