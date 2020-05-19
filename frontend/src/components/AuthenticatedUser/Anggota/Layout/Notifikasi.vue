<template>
  <div>
    <q-page-container>
      <div class="q-pa-md">
        <div v-if="exist">
          <q-intersection
            v-for="a in data"
            transition="flip-right"
            class="example-item"
            :key="a.id"
          >
            <q-item clickable v-ripple>
              <q-item-section avatar>
                <q-avatar color="primary" text-color="white">
                  <q-icon name="report"/>
                </q-avatar>
              </q-item-section>

              <q-item-section class="text-black">
                <q-item-label caption>{{ a.pesan }}</q-item-label>
              </q-item-section>
            </q-item>
          </q-intersection>
        </div>
        <div v-else>
          <q-card>
            <q-card-section>
              <center>
                <div class="text-purple">
                  <q-icon name="fa fa-smile" size="xl"/>
                </div>
                <div class="text-black">
                  Anda Tidak mempunyai Notifikasi
                </div>
              </center>
            </q-card-section>
          </q-card>
        </div>
      </div>
    </q-page-container>
    <q-footer class="bg-white">
      <q-btn color="primary" class="full-width" icon="fa fa-arrow-left" to="/"/>
    </q-footer>
  </div>
</template>

<script>
  import moment from 'moment'

  export default {
    data() {
      return {
        exist: false,
        data: null
      }
    },
    methods: {
      getData() {
        this.$http.get('/api/getnotifuser', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.exist = res.data.exist
            if (this.exist) {
              this.data = res.data.notif
            }
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      }
    },
    created() {
      this.$q.loading.show()
      this.getData()
    }
  }
</script>

<style scoped>

</style>
