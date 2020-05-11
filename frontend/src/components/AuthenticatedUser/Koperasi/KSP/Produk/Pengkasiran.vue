<template>
  <div class="q-pa-md">
    <q-card class="full-height full-height">
      <q-card-section class="text-h6">Menu Transaksi</q-card-section>
      <q-separator/>
      <q-card-section>
        <div class="row">
          <div class="col">
            <div class="row">
              <div class="col">
                <q-input
                  filled
                  disable
                  style="max-height: 50%"
                  v-model="kodeTransaksi"
                  label="Kode Transaksi"
                  input-class="text-right"
                />
                <br>
                <q-input
                  filled
                  disable
                  style="max-height: 50%"
                  :value="tanggal"
                  label="Tanggal Transaksi"
                  input-class="text-right"
                />
                <br>
                <q-input
                  filled
                  disable
                  style="max-height: 50%"
                  :value="pukul"
                  label="Waktu"
                  input-class="text-right"
                />
                <br>
                <q-checkbox v-model="anggota" label="Untuk Anggota" :disable="anggota === true"
                            color="teal"/>

              </div>
              <div class="col"></div>
            </div>
          </div>
          <div class="col">
            <div class="row">
              <q-form @submit.prevent="searchBarang" class="full-width">
                <q-input
                  filled
                  class="full-width"
                  v-model="barcode"
                  label="Kode Barang"
                  input-class="text-right"
                />
              </q-form>
            </div>
            <br>
            <div class="row">
              <div class="col">
                <q-input
                  filled
                  disable
                  v-model="jumlahBarang"
                  label="Jumlah Barang"
                  input-class="text-right"
                />
              </div>
              <div class="col"></div>
              <div class="col">
                <q-input
                  filled
                  disable
                  v-model="hargaBarang"
                  label="Harga Barang"
                  input-class="text-right"
                />
              </div>
            </div>
            <br>
            <div class="row">
              <q-input
                filled
                class="full-width"
                v-model="jumlahBeli"
                label="Jumlah Beli"
                input-class="text-right"
              />
            </div>
            <br>
            <div class="row">
              <q-btn color="primary" label="Tambah Keranjang"
                     :disable="jumlahBeli>jumlahBarang || jumlahBeli ===0 "
                     @click="tambahKeranjang"/>
            </div>
          </div>
        </div>
      </q-card-section>
      <q-separator/>
      <q-card-section>
        <div class="row">
          <div class="col-md-8">
            <q-table
              :dense="$q.screen.lt.md"
              title="Daftar Barang Beli"
              :data="data"
              :columns="columns"
              row-key="id"
              selection="single"
              :selected.sync="selected">
              <template v-slot:top-right>
                <q-btn size="xs" color="red" label="Hapus"
                       :disable="selected.length === 0"
                       icon="fa fa-window-close" @click="hapus"/>&nbsp;
                <q-btn size="xs" color="green" label="Ubah stok"
                       :disable="selected.length === 0"
                       icon="update" @click="update = true"/>
              </template>
            </q-table>
          </div>
          <div class="col-md-1"></div>
          <div class="col-md-3">
            <q-input
              filled
              disable
              class="full-width"
              v-model="totalBeli"
              label="Total"
              input-class="text-right"
            />
            <br>
            <q-input
              filled
              class="full-width"
              v-model="uangBeli"
              label="Jumlah Beli"
              input-class="text-right"
            />
            <br>
            <q-btn :disable="tot>uangBeli" color="primary" label="Simpan" @click="saveTransaksi"/>
          </div>
        </div>
      </q-card-section>
    </q-card>
    <q-dialog v-model="update" persistent transition-show="scale"
              transition-hide="scale">
      <q-card style="width: 700px; max-width: 80vw">
        <q-card-section>
          Ubah
        </q-card-section>
        <q-separator/>
        <q-card-section>
          <q-form @submit.prevent="ubahProduk">
            <q-input
              square clearable
              filled
              v-model="jumlahBeli"
              label="Jumlah Barang"
            />
            <br>
            <br>
            <center>
              <q-btn label="Close" color="red" v-close-popup/>
              &nbsp;
              <q-btn label="Simpan" color="primary" type="submit" v-close-popup/>
            </center>
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
  import moment from 'moment'

  export default {
    data() {
      return {
        totalBeli: 0,
        uangBeli: 0,
        id: null,
        selected: [],
        kodeTransaksi: null,
        tanggal: null,
        pukul: null,
        jumlahBarang: 0,
        hargaBarang: 0,
        jumlahBeli: 0,
        data: [],
        columns: [
          {
            name: 'barcode',
            label: 'Kode Barang',
            align: 'center',
            field: row => row.harga.produk.kodeProduk,
            sortable: true,
          },
          {
            name: 'nama',
            label: 'Nama Barang',
            align: 'center',
            field: row => row.harga.produk.namaProduk,
            sortable: true,
          },
          {
            name: 'harga',
            label: 'Harga Barang',
            align: 'center',
            field: row => (this.anggota ? this.toIDR(row.harga.hargaJualAnggota) : this.toIDR(row.harga.hargaJualNonAnggota)),
            sortable: true,
          },
          {
            name: 'jumlahBeli',
            label: 'Jumlah Beli',
            align: 'center',
            field: row => row.jumlahBeli,
            sortable: true,
          },
          {
            name: 'subTotal',
            label: 'SubTotal',
            align: 'center',
            field: row => this.toIDR(row.jumlahBeli * (this.anggota ? row.harga.hargaJualAnggota : row.harga.hargaJualNonAnggota)),
            sortable: true,
          }
        ],
        anggota: false,
        barcode: null,
        tot: 0,
        update: false
      }
    },
    methods: {
      ubahProduk() {
        this.$q.loading.show()
        this.$http.put(`/api/ubahprod/${this.selected[0].id}`, { jumlahBeli: this.jumlahBeli }, { headers: this.$auth.getHeader() })
          .then((res) => {
            this.getDataProduk()
            this.getData()
            this.$q.notify({
              type: 'positive',
              message: `Berhasil mengubah dari keranjang`
            })
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      hapus() {
        this.$q.loading.show()
        this.$http.delete(`/api/deleteprod/${this.selected[0].id}`, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.getDataProduk()
            this.getData()
            this.$q.notify({
              type: 'positive',
              message: `Berhasil menghapus dari keranjang`
            })
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      saveTransaksi() {
        this.$q.loading.show()
        this.$http.post('/api/savetransaksi', {
          uangBeli: this.uangBeli,
          anggota: this.anggota
        }, {
          headers: this.$auth.getHeader()
        })
          .then(() => {
            this.getDataProduk()
            this.getData()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      getDataProduk() {
        this.$http.get('/api/getprodukbeli', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.data = res.data
            this.tot = 0
            for (let i = 0; i < this.data.length; i++) {
              this.tot += this.data[i].jumlahBeli * (this.anggota ? this.data[i].harga.hargaJualAnggota : this.data[i].harga.hargaJualNonAnggota)
            }
            this.totalBeli = this.toIDR(this.tot)
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      tambahKeranjang() {
        this.$q.loading.show()
        this.$http.post('/api/tambahkerangjang', {
          id: this.id,
          jumlahBeli: this.jumlahBeli,
          anggota: this.anggota
        }, {
          headers: this.$auth.getHeader()
        })
          .then(() => {
            this.jumlahBeli = 0
            this.id = null
            this.barcode = null
            this.jumlahBarang = 0
            this.hargaBarang = 0
            this.getDataProduk()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
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
      searchBarang() {
        this.$q.loading.show()
        this.$http.post('/api/getprodukbybarcode', {
          barCode: this.barcode,
          anggota: this.anggota
        }, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            const { data } = res
            if (data.exist === false) {
              this.$q.notify({
                type: 'negative',
                message: `Barang Tidak Ditemukan`
              })
              this.id = null
              this.jumlahBarang = 0
              this.hargaBarang = 0
            } else {
              this.id = data.id
              this.jumlahBarang = data.jumlah
              this.hargaBarang = this.toIDR(data.harga)
            }
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      getData() {
        this.$http.get('/api/getkodetransaksiproduk', { headers: this.$auth.getHeader() })
          .then((res) => {
            this.kodeTransaksi = res.data
            moment.lang('id')
            this.tanggal = moment(new Date())
              .format('dddd, Do MMMM YYYY')
            this.pukul = moment(new Date())
              .format('hh:mm')
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
      this.getDataProduk()
    }
  }
</script>

<style scoped>

</style>
