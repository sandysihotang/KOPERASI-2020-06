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
                        title="Akun"
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
                                 color="purple-4" class="full-width text-white" label="Daftar Pemilik Koperasi">
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
            this.showAlert('Registrasi Akun Berhasil', 'success');
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
            this.showAlert(error.response.data.err, 'error');
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
