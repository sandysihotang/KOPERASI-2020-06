<template>
  <div>
    <q-card>
      <q-card-section v-if="show">
        <center>
          <p class="text-h6">Produk Koperasi</p>
          <p>Koperasi {{ res.namaKoperasi }}</p>
        </center>
      </q-card-section>
      <q-separator/>
      <q-card-section v-if="show">
        <table class="q-table">
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td><b>Jumlah Produk</b></td>
            <td>{{ res.jumlahProduk }}</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
        </table>
      </q-card-section>
      <q-card-section v-if="show">
        <q-table
          :columns="columns"
          :data="res.dataTable"/>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn color="primary" v-if="show" icon="print" label="Export Excel" @click="download"/>
        <q-btn label="Tutup" color="green" v-close-popup/>
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
        res: null,
        columns: [
          {
            name: 'nama',
            label: 'Nama Produk',
            align: 'center',
            field: row => row.nama_produk,
            sortable: true,
          }, {
            name: 'kode',
            label: 'Kode Produk',
            align: 'center',
            field: row => row.kode_produk,
            sortable: true,
          }, {
            name: 'kate',
            label: 'Kategori Produk',
            align: 'center',
            field: row => row.nama_kategori,
            sortable: true,
          }, {
            name: 'jum',
            label: 'Jumlah Produk',
            align: 'center',
            field: row => row.jumlah_produk,
            sortable: true,
          }, {
            name: 'bel',
            label: 'Harga Beli',
            align: 'center',
            field: row => this.toIDR(row.harga_beli),
            sortable: true,
          }, {
            name: 'jualang',
            label: 'Harga Jual Anggota',
            align: 'center',
            field: row => this.toIDR(row.harga_jual_anggota),
            sortable: true,
          }, {
            name: 'jualnonang',
            label: 'Harga Jual Non Anggota',
            align: 'center',
            field: row => this.toIDR(row.harga_jual_non_anggota),
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
        this.$http.get('/api/getlaporanproduk/download', {
          headers: this.$auth.getHeader(),
          responseType: 'arraybuffer'
        })
          .then((res) => {
            this.downloadFile(res, 'ProdukKoperasi')
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
        this.$http.get('/api/getlaporanproduk', {
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
