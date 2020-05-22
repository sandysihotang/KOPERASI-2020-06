<template>
  <div>
    <div class="q-pa-md">
      <div v-if="exist">
        <q-intersection
          v-for="a in data"
          :key="a.id"
        >
          <q-item clickable v-ripple>
            <q-item-section avatar>
              <q-avatar color="primary" text-color="white">
                <q-icon name="report"/>
              </q-avatar>
            </q-item-section>

            <q-item-section class="text-black">
              <q-item-label caption>Laporan anda tahun: {{ a.tahun_laporan }}</q-item-label>
              <q-item-label class="text-caption">Pesan:{{
                a.pesan }}
              </q-item-label>
              <q-item-label caption lines="1">
                <q-chip class="glossy" color="primary" text-color="white">{{
                  mom(a.tanggal_pengiriman) }}
                </q-chip>
              </q-item-label>
            </q-item-section>
          </q-item>
        </q-intersection>
      </div>
      <div v-else>
        <q-card class="full-width">
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
  </div>
</template>

<script>
  import moment from 'moment'

  export default {
    data() {
      return {
        exist: false,
        data: []
      }
    },
    methods: {
      mom(date) {
        moment.lang('id')
        return moment(date)
          .format('dddd, Do MMMM YYYY')
      },
      getData() {
        this.$http.get('/api/getnotifikasiexistanddata', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            const { data } = res
            this.exist = data.exist
            if (this.exist) {
              this.data = data.datatable
            }
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      }
    },
    created() {
      this.getData()
    }
  }
</script>

<style scoped>

</style>
