<template>
  <div>
    <q-card class="full-width full-height">
      <q-card-section class="full-width">
        <div class="row">
          <div class="col">
            <div class="row">
              <div class="col">
                <q-input filled v-model="dateFrom" mask="date" label="Tanggal Dari"
                         :rules="['date']" style="max-width: 90%;">
                  <template v-slot:append>
                    <q-icon name="event" class="cursor-pointer">
                      <q-popup-proxy ref="qDateProxy" transition-show="scale"
                                     transition-hide="scale">
                        <q-date v-model="dateFrom" @input="() => $refs.qDateProxy.hide()"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
              </div>
              <div class="col">
                <q-input filled v-model="dateTo" mask="date" label="Tanggal Sampai"
                         :rules="['date']" style="max-width: 90%;">
                  <template v-slot:append>
                    <q-icon name="event" class="cursor-pointer">
                      <q-popup-proxy ref="qDateProxy" transition-show="scale"
                                     transition-hide="scale">
                        <q-date v-model="dateTo" @input="() => $refs.qDateProxy.hide()"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
              </div>
              <div class="col">
                <q-btn color="primary" label="Tampilkan" icon="fa fa-file" size="xm"
                       @click="showLaporan"/>
              </div>
            </div>
          </div>
          <div class="col"></div>
        </div>
      </q-card-section>
      <q-separator/>
      <q-card-section v-if="show">
        <center>
          <p class="text-h6">Laba Dan Pemasukan</p>
          <p>Koperasi {{ res.namaKoperasi }}</p>
          <p>{{ getDate(dateFrom) }} s/d {{ getDate(dateTo) }}</p>
        </center>
      </q-card-section>
      <q-separator/>
      <q-card-section v-if="show">
        <table class="q-table">
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td><b>Realisasi Jasa</b></td>
            <td>{{ toIDR(res.realisasiJasa) }}</td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td><b>Tunggakan</b></td>
            <td>{{ toIDR(res.tunggakan) }}</td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td><b>Simpanan Pokok</b></td>
            <td>{{ toIDR(res.simpananPokok) }}</td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td><b>Simpanan Wajib</b></td>
            <td>{{ toIDR(res.simpananWajib) }}</td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td><b>Simpanan Sukarela</b></td>
            <td>{{ toIDR(res.simpananSukarela) }}</td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td><b>Total Pemasukan</b></td>
            <td><b>{{
              toIDR(res.simpananSukarela+res.simpananWajib+res.simpananPokok+res.tunggakan+res.realisasiJasa)
              }}</b></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
        </table>
      </q-card-section>
      <q-card-actions align="right" class="bg-white absolute-bottom-right text-teal">
        <q-btn color="primary" icon="print" label="Export Excel" v-if="show" @click="download"/>
        <q-btn label="Close" color="green" v-close-popup/>
      </q-card-actions>
    </q-card>
  </div>
</template>

<script>
  import moment from 'moment'

  export default {
    data() {
      return {
        show: false,
        dateFrom: null,
        dateTo: null,
        res: null
      }
    },
    methods: {
      toIDR(num) {
        const nums = `${num}`
        let ans = ''
        let coma = 0
        for (let i = nums.length - 1; i >= 0; i--) {
          ans = `${ans}${nums[i]}`
          if (coma === 2 && i !== 0) {
            ans = `${ans},`
            coma = 0;
          } else {
            coma++;
          }
        }
        let res = 'Rp '
        for (let i = ans.length - 1; i >= 0; i--) {
          res = `${res}${ans[i]}`
        }
        return res;
      },
      getDate(date) {
        moment.lang('id')
        return moment(date)
          .format('dddd, Do MMMM YYYY')
      },
      download() {
        if (this.dateFrom === null || this.dateTo === null) {
          this.$q.notify({
            type: 'negative',
            message: `Silahkan masukkan tanggal`
          })
          return
        }
        this.$q.loading.show()
        this.$http.post('/api/getlaporanpemasukandanlaba/download', {
          dateFrom: this.dateFrom,
          dateTo: this.dateTo
        }, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.notify({
              type: 'negative',
              message: `Terjadi kesalahan refresh (F5)`
            })
            this.$q.loading.hide()
          })
      },
      showLaporan() {
        if (this.dateFrom === null || this.dateTo === null) {
          this.$q.notify({
            type: 'negative',
            message: `Silahkan masukkan tanggal`
          })
          return
        }
        this.$q.loading.show()
        this.$http.post('/api/getlaporanpemasukandanlaba', {
          dateFrom: this.dateFrom,
          dateTo: this.dateTo
        }, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.res = res.data
            this.show = true
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.notify({
              type: 'negative',
              message: `Terjadi kesalahan refresh (F5)`
            })
            this.$q.loading.hide()
          })
      }
    }
  }
</script>

<style scoped>

</style>
