<template>
  <div>
    <q-card>
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
          <p class="text-h6">Penjualan Produk</p>
          <p>Koperasi {{ res.namaKoperasi }}</p>
          <p>{{ getDate(dateFrom) }} s/d {{ getDate(dateTo) }}</p>
          <table>
            <tr>
              <td><b>Jumlah Produk Terjual</b></td>
              <td>:</td>
              <td>{{ res.totProdTerjual }} Barang</td>
            </tr>
            <tr>
              <td><b>Total Produk Terjual</b></td>
              <td>:</td>
              <td>{{ toIDR(res.totTerjual) }}</td>
            </tr>
          </table>
        </center>
      </q-card-section>
      <q-separator/>
      <q-card-section v-if="show">
        <q-table
          :data="res.dataTable"
          :columns="columns"/>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn color="primary" icon="print" label="Export Excel" v-if="show" @click="download"/>
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
        dateFrom: null,
        dateTo: null,
        res: null,
        columns: [
          {
            name: 'trans',
            label: 'Kode Transaksi',
            align: 'center',
            field: row => row.kode_transaksi,
            sortable: true,
          }, {
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
            label: 'Jumlah Beli',
            align: 'center',
            field: row => row.jumlah_beli,
            sortable: true,
          }, {
            name: 'hargaJual',
            label: 'Harga Jual',
            align: 'center',
            field: row => this.toIDR(row.harga_jual),
            sortable: true,
          }, {
            name: 'tgl',
            label: 'Tanggal Transaksi',
            align: 'center',
            field: row => this.getDate(row.tanggal_transaksi),
            sortable: true,
          }, {
            name: 'sub',
            label: 'Sub Total',
            align: 'center',
            field: row => this.toIDR(row.harga_jual * row.jumlah_beli),
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
        if (this.dateFrom === null || this.dateTo === null) {
          this.$q.notify({
            type: 'negative',
            message: `Silahkan masukkan tanggal`
          })
          return
        }
        this.$q.loading.show()
        this.$http.post('/api/getlaporanpenjualanproduk/download', {
          dateFrom: this.dateFrom,
          dateTo: this.dateTo
        }, {
          headers: this.$auth.getHeader(),
          responseType: 'arraybuffer'
        })
          .then((res) => {
            this.downloadFile(res, 'PenjualanProduk')
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
      ,
      showLaporan() {
        if (this.dateFrom === null || this.dateTo === null) {
          this.$q.notify({
            type: 'negative',
            message: `Silahkan masukkan tanggal`
          })
          return
        }
        this.$q.loading.show()
        this.$http.post('/api/getlaporanpenjualanproduk', {
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
