<template>
  <div>
    <q-card  v-if="show">
      <q-card-section>
        <center>
          <p class="text-h6">Rekapitulasi Simpanan</p>
          <p>Koperasi {{ res.namaKoperasi }}</p>
        </center>
      </q-card-section>
      <q-separator/>
      <q-card-section>
        <table class="q-table">
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td><b>Simpanan Pokok</b></td>
            <td>{{ toIDR(res.pokok) }}</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td><b>Simpanan Wajib</b></td>
            <td>{{ toIDR(res.wajib) }}</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td><b>Simpanan Sukarela</b></td>
            <td>{{ toIDR(res.sukarela) }}</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td><b>Total Simpanan</b></td>
            <td><b>{{
              toIDR(res.sukarela+res.wajib+res.pokok)
              }}</b></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
        </table>
      </q-card-section>
      <q-card-section>
        <q-table
          :columns="columns"
          :data="res.dataTable"/>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn color="primary" icon="print" label="Export Excel" @click="download"/>
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
        res: null,
        columns: [
          {
            name: 'nama',
            label: 'Nama Nasabah',
            align: 'center',
            field: row => row.name,
            sortable: true,
          }, {
            name: 'pokok',
            label: 'Simpanan Pokok',
            align: 'center',
            field: row => this.toIDR(row.pokok),
            sortable: true,
          }, {
            name: 'wajib',
            label: 'Simpanan Wajib',
            align: 'center',
            field: row => this.toIDR(row.wajib),
            sortable: true,
          }, {
            name: 'sukarela',
            label: 'Simpanan Sukarela',
            align: 'center',
            field: row => this.toIDR(row.sukarela),
            sortable: true,
          }, {
            name: 'total',
            label: 'Total Simpanan',
            align: 'center',
            field: row => this.toIDR(row.sukarela + row.wajib + row.pokok),
            sortable: true,
          },
        ]
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
      downloadFile(response, filename) {
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([response.data], { type: 'application/excel' })

        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        if (window.navigator && window.navigator.msSaveOrOpenBlob) {
          window.navigator.msSaveOrOpenBlob(newBlob)
          return
        }

        // For other browsers:
        // Create a link pointing to the ObjectURL containing the blob.
        const data = window.URL.createObjectURL(newBlob)
        const link = document.createElement('a')
        link.href = data
        link.download = `${filename}.xlsx`
        link.click()
        setTimeout(() => {
          // For Firefox it is necessary to delay revoking the ObjectURL
          window.URL.revokeObjectURL(data)
        }, 100)
        this.$swal({
          position: 'center',
          type: 'success',
          width: 300,
          title: 'Berhasil Mengexport Data',
          showConfirmButton: false,
          timer: 1500
        })
      },
      download() {
        this.$q.loading.show()
        this.$http.get('/api/getlaporansimpanan/download', {
          headers: this.$auth.getHeader(),
          responseType: 'arraybuffer'
        })
          .then((res) => {
            this.downloadFile(res, 'RekapitulasiSimpanan')
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
        this.$http.get('/api/getlaporanusersimpanan', {
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
    },
    created() {
      this.$q.loading.show()
      this.showLaporan()
    }
  }
</script>

<style scoped>

</style>
