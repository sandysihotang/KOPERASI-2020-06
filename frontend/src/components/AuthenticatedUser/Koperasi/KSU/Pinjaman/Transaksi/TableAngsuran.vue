<template>
  <div class="q-pa-md">
    <q-table
      class="full-width"
      :dense="$q.screen.lt.md"
      :data="dataAjuan"
      :columns="columnsAjuan"
      row-key="id"
      selection="single"
      :selected.sync="selectedAngsuran"
    />
    <q-space/>
    <q-card v-if="selectedAngsuran.length !== 0">
      <q-card-section>
        Detail Pembayaran
      </q-card-section>
      <q-card-section>
        <q-input
          disable
          filled
          :value="parseInt(selectedAngsuran[0].total_angsuran) * 100"
          label="Total Bayar"
          mask="Rp ###,###.##"
          fill-mask="0"
          reverse-fill-mask
          unmasked-value
          input-class="text-right"
        />
        <br/>
        <q-input filled v-model="date" mask="date" label="Tanggal Pembayaran" :rules="['date']">
          <template v-slot:append>
            <q-icon name="event" class="cursor-pointer">
              <q-popup-proxy ref="qDateProxy" transition-show="scale" transition-hide="scale">
                <q-date v-model="date" @input="() => $refs.qDateProxy.hide()"/>
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
        <q-btn color="primary" class="full-width" label="Simpan" @click="save"/>
      </q-card-section>
    </q-card>
  </div>
</template>

<script>
  import moment from 'moment'

  export default {
    props: ['user'],
    data() {
      return {
        dataAjuan: [],
        selectedAngsuran: [],
        columnsAjuan: [
          {
            name: 'termin',
            label: 'Termin Ke #',
            align: 'center',
            field: row => row.urutan_ke,
            sortable: true,
          },
          {
            name: 'status',
            label: 'Status',
            align: 'center',
            field: row => (row.status_bayar ? 'Sudah Dibayar' : 'Belum Dibayar'),
            sortable: true,
          },
          {
            name: 'angsuranPokok',
            label: 'Angsuran Pokok',
            align: 'center',
            field: row => this.toIDR(parseInt(row.angsuran_pokok)),
            sortable: true,
          },
          {
            name: 'bunga',
            label: 'Bunga',
            align: 'center',
            field: row => this.toIDR(parseInt(row.bunga)),
            sortable: true,
          },
          {
            name: 'totalAngsuran',
            label: 'Total Angsuran',
            align: 'center',
            field: row => this.toIDR(parseInt(row.total_angsuran)),
            sortable: true,
          },
          {
            name: 'tglJatuhTempo',
            label: 'Tanggal Jatuh Tempo',
            align: 'center',
            field: (row) => {
              moment.lang('id')
              return moment(row.tanggal_jatuh_tempo)
                .format('dddd, Do MMMM YYYY')
            },
            sortable: true,
          },
          {
            name: 'denda',
            label: 'Denda',
            align: 'center',
            field: row => this.toIDR(parseInt(row.denda === null ? 0 : row.denda)),
            sortable: true,
          },
          {
            name: 'totalTagihan',
            label: 'Total Tagihan',
            align: 'center',
            field: row => this.toIDR(parseInt(row.total_tagihan === null ? 0 : row.total_tagihan)),
            sortable: true,
          },
          {
            name: 'totalBayar',
            label: 'Total Bayar',
            align: 'center',
            field: row => this.toIDR(parseInt(row.total_bayar === null ? 0 : row.total_bayar)),
            sortable: true,
          }
        ],
        date: null
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
      loaddataAjuan() {
        this.$q.loading.show()
        this.$http.get(`/api/getdatapembayaran/${this.user.id}`, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.dataAjuan = res.data
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      save() {
        if (this.date === null) {
          this.$q.notify({
            type: 'negative',
            message: `Isi tanggal pembayaran`
          })
          return
        }
        const dateNow = new Date()
        const dateIn = new Date(this.date)
        const totDateNow = (dateNow.getFullYear() * 12 * 30) + (dateNow.getMonth() * 30) + dateNow.getDate()
        const totDateIn = (dateIn.getFullYear() * 12 * 30) + (dateIn.getMonth() * 30) + dateIn.getDate()
        if (totDateIn > totDateNow || totDateNow > totDateIn) {
          this.$q.notify({
            type: 'negative',
            message: `Tanggal pembayaran harus valid`
          })
          return;
        }
        this.$q.loading.show()
        this.$http.put(`/api/pembayaransukses/${this.selectedAngsuran[0].id}`, { date: this.date }, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.loaddataAjuan()
            this.$q.notify({
              type: 'positive',
              message: `Pembayaran Berhasil dilakukan`
            })
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      }
    },
    created() {
      this.loaddataAjuan()
    }
  }
</script>

<style scoped>

</style>
