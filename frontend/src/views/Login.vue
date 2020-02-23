<template>
  <div>
    <q-layout>
      <q-page-container>
        <q-page class="bg-light-green window-height window-width row justify-center items-center">
          <div class="column">
            <div class="row">
              <h5 class="text-h5 text-white q-my-md">Company & Co</h5>
            </div>
            <div class="row">
              <q-card square bordered class="q-pa-lg shadow-1">
                <q-card-section>
                  <q-form class="q-gutter-md">
                    <q-input square filled
                             clearable v-model="username" type="text" label="Username"/>
                    <q-input
                      square filled clearable
                      v-model="password" type="password" label="password"/>
                  </q-form>
                </q-card-section>
                <q-card-actions class="q-px-md">
                  <q-btn v-on:click="login"
                         unelevated color="light-green-7" size="lg" class="full-width"
                         label="Login"/>
                </q-card-actions>
                <q-card-section class="text-center q-pa-none">
                  <p class="text-grey-6">Not reigistered? Created an Account</p>
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
// import axios from 'axios';

export default {
  name: 'Login',
  data() {
    return {
      username: '',
      password: '',
    };
  },
  methods: {
    login() {
      const form = new FormData();
      form.set('grant_type', 'password');
      form.set('username', this.username);
      form.set('password', this.password);
      // this.$http.get('hello');
      this.$http.post('oauth/token', form, {
        auth: {
          username: 'mobile',
          password: 'pin',
        },
      })
        .then((e) => {
          console.log(e);
        });
    },
  },
};
</script>

<style>
  .q-card {
    width: 360px;
  }
</style>
