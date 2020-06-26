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
              <q-card square class="shadow-24" style="width:300px;height:485px;">
                <q-card-section class="bg-deep-purple-7">
                  <h4 class="text-h5 text-white q-my-md">TobaKo</h4>
                  <div class="absolute-bottom-right q-pr-md" style="transform: translateY(50%);">
                    <router-link to="/login">
                      <q-btn round icon="fa fa-arrow-left" color="purple-4"/>
                    </router-link>
                  </div>
                </q-card-section>
                <q-card-section>
                  <q-form class="q-px-sm q-pt-xl">
                    <q-input v-model="username" square clearable type="text" label="Username"
                             hint="Masukkan username, link reset password akan dikirimkan ke email anda">
                      <template v-slot:prepend>
                        <q-icon name="person"/>
                      </template>
                    </q-input>
                  </q-form>
                </q-card-section>
                <q-card-actions class="q-px-lg">
                  <q-btn :loading="loading" @click="caripassword" unelevated
                         size="lg"
                         color="purple-4" class="full-width text-white" label="Reset Password">
                    <template v-slot:loading>
                      <q-spinner-hourglass class="on-left"/>
                      Loading...
                    </template>
                  </q-btn>
                </q-card-actions>
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
    data() {
      return {
        loading: false,
        username: null
      }
    },
    methods: {
      caripassword() {
        if (this.username === null) {
          this.$q.notify({
            type: 'negative',
            message: 'Isi username anda'
          })
          return
        }
        this.$q.loading.show()
        this.$http.post('/resetpassword', {
          username: this.username
        })
          .then((res) => {
            const { data } = res
            if (data.exist === false) {
              this.$q.notify({
                type: 'negative',
                message: data.error
              })
            } else {
              this.$q.notify({
                type: 'positive',
                message: 'Link reset password telah dikirimkan ke email anda'
              })
            }
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      }
    }
  }
</script>

<style scoped>

</style>
