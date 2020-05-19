<template>
  <div class="q-pa-md">
    <q-card class="full-height full-width">
      <div v-if="!vendor">
        <q-card-section>Data Vendor</q-card-section>
        <q-separator/>
        <q-card-section>
          <q-input
            square clearable
            filled
            v-model="namaVendor"
            label="Nama Vendor"
          />
          <br>
          <q-input
            square clearable
            filled
            v-model="noTelepon"
            label="No Telepon Vendor"
          />
          <br>
          <q-input
            square clearable
            filled
            v-model="alamat"
            label="Alamat Vendor"
          />
        </q-card-section>
        <q-separator/>
        <q-card-section>
          <center>
            <q-btn color="red" label="tutup" v-close-popup/>&nbsp;
            <q-btn color="primary" label="Simpan Vendor" @click="simpanVendor"/>
          </center>
        </q-card-section>
      </div>
      <div v-else>
        <q-card-section>
          <q-table
            :dense="$q.screen.lt.md"
            title="Daftar Produk"
            :data="data"
            :columns="columns"
            row-key="id"
            selection="single"
            :selected.sync="selected"
          >
            <template v-slot:top-right>
              <q-btn size="xs" color="green" label="Export"
                     :disable="data.length === 0"
                     icon="fa fa-download" @click="download"/>&nbsp;
              <q-btn size="xs" color="green" label="Ubah produk"
                     :disable="selected.length === 0"
                     icon="update" @click="update"/>&nbsp;
              <q-btn size="xs" color="green" label="Hapus Produk"
                     :disable="selected.length === 0"
                     @click="deleteProduk"
                     icon="fa fa-minus"/>&nbsp;
              <q-btn size="xs" color="green" label="Tambah Barang" @click="p = true"
                     icon="fa fa-plus"/>&nbsp;
              <q-btn size="xs" color="green" label="Tambah Kategori" @click="kat = true"
                     icon="table"/>
            </template>
          </q-table>
        </q-card-section>
        <q-card-section>
          <div class="q-pa-md">
            <div class="row">
              <div class="col-md-4"></div>
              <div class="col-md-4 offset-md-4">
                <q-card>
                  <q-card-section class="bg-primary text-white text-h6">
                    Total: {{ toIDR(parseInt(total)) }}
                  </q-card-section>
                </q-card>
              </div>
            </div>
            <div class="row">
              <div class="col-md-4"></div>
              <div class="col-md-4 offset-md-4">
                <q-card>
                  <q-card-section>
                    <center>
                      <q-btn label="Close" color="red" v-close-popup/>&nbsp;
                      <q-btn label="Simpan" @click="simpan" color="primary"
                             :disable="data.length === 0" v-close-popup/>
                    </center>
                  </q-card-section>
                </q-card>
              </div>
            </div>
          </div>
        </q-card-section>
      </div>
      <q-dialog v-model="p" persistent transition-show="scale"
                transition-hide="scale">
        <q-card style="width: 700px; max-width: 80vw" v-if="barcode === false">
          <q-card-section>
            Form Penambahan Barang
          </q-card-section>
          <q-card-section>
            <q-form @submit.prevent="getProduk">
              <q-input
                square clearable
                filled
                v-model="kodeProduk"
                label="BarCode"
              />
              <br>
              <center>
                <q-btn label="Cari Barang" type="submit" color="primary"/>
              </center>
            </q-form>
          </q-card-section>
        </q-card>
        <q-card style="width: 700px; max-width: 80vw" v-else>
          <div v-if="produkExist">
            <q-card-section>
              Nama Produk: {{ restProduk.namaProduk }}
            </q-card-section>
            <q-separator/>
            <q-card-section>
              <q-form @submit.prevent="tambahProduk">
                <q-input
                  square clearable
                  filled
                  v-model="hargaBeli"
                  label="Harga Beli"
                />
                <br>
                <q-input
                  square clearable
                  filled
                  v-model="hargaJualAnggota"
                  label="Harga Jual Anggota"
                />
                <br>
                <q-input
                  square clearable
                  filled
                  v-model="hargaJualNonAnggota"
                  label="Harga Jual Non Anggota"
                />
                <br>
                <q-input
                  square clearable
                  filled
                  v-model="jumlahBarang"
                  label="Jumlah Barang Masuk"
                />
                <br>
                <br>
                <center>
                  <q-btn label="Close" color="red" @click="barcode = true" v-close-popup/>
                  &nbsp;
                  <q-btn label="Simpan" color="primary" type="submit" v-close-popup/>
                </center>
              </q-form>
            </q-card-section>
          </div>
          <div v-else>
            <q-card-section>
              Produk Baru
            </q-card-section>
            <q-separator/>
            <q-card-section>
              <q-form @submit.prevent="simpanProdukBaru">
                <q-input
                  square clearable
                  filled
                  label="Nama Barang"
                  v-model="namaProduk"
                />
                <br>
                <q-select
                  label="Kategori"
                  filled
                  v-model="model"
                  :options="options"
                  stack-label
                />
                <br>
                <q-input
                  square clearable
                  filled
                  label="Harga Beli"
                  v-model="hargaBeli"
                />
                <br>
                <q-input
                  square clearable
                  filled
                  label="Harga Jual Anggota"
                  v-model="hargaJualAnggota"
                />
                <br>
                <q-input
                  square clearable
                  filled
                  label="Harga Jual Non Anggota"
                  v-model="hargaJualNonAnggota"
                />
                <br>
                <q-input
                  square clearable
                  filled
                  label="Jumlah Barang Masuk"
                  v-model="jumlahBarang"
                />
                <br>
                <center>
                  <q-btn label="Close" color="red" v-close-popup @click="barcode = true"/>
                  &nbsp;
                  <q-btn label="Simpan" color="primary" type="submit" v-close-popup/>
                </center>
              </q-form>
            </q-card-section>
          </div>
        </q-card>
      </q-dialog>
      <q-dialog v-model="ubah" persistent transition-show="scale"
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
                v-model="hargaBeli"
                label="Harga Beli"
              />
              <br>
              <q-input
                square clearable
                filled
                v-model="jumlahBarang"
                label="Jumlah Barang Masuk"
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
      <q-dialog v-model="kat" persistent transition-show="scale"
                transition-hide="scale">
        <q-card>
          <q-card-section>
            Kategori Baru
          </q-card-section>
          <q-card-section>
            <q-form @submit.prevent="saveKategori">
              <q-input
                square clearable
                filled
                v-model="kategoriProd"
                label="Kategori"
              />
              <br>
              <center>
                <q-btn label="Close" color="red" v-close-popup/>&nbsp;
                <q-btn label="Simpan" color="primary" type="submit"/>
              </center>
            </q-form>
          </q-card-section>
        </q-card>
      </q-dialog>
    </q-card>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        total: 0,
        selected: [],
        ubah: false,
        namaProduk: null,
        hargaBeli: null,
        hargaJualAnggota: null,
        hargaJualNonAnggota: null,
        jumlahBarang: null,
        kategoriProd: null,
        kat: false,
        kodeProduk: null,
        p: false,
        namaVendor: null,
        noTelepon: null,
        alamat: null,
        vendor: false,
        id: null,
        bg: false,
        data: [],
        columns: [
          {
            name: 'barcode',
            label: 'Kode Barang',
            align: 'center',
            field: row => row.produk.kodeProduk,
            sortable: true,
          },
          {
            name: 'nama',
            label: 'Nama Barang',
            align: 'center',
            field: row => row.produk.namaProduk,
            sortable: true,
          },
          {
            name: 'kategori',
            label: 'Kategori Barang',
            align: 'center',
            field: row => row.produk.kategoriProduk.namaKategori,
            sortable: true,
          },
          {
            name: 'hargabeli',
            label: 'Harga Beli',
            align: 'center',
            field: row => this.toIDR(parseInt(row.hargaBeli)),
            sortable: true,
          }, {
            name: 'jumlahbarang',
            label: 'Jumlah Barang',
            align: 'center',
            field: row => row.jumlahProduk,
            sortable: true,
          }, {
            name: 'subtotal',
            label: 'Sub total',
            align: 'center',
            field: row => this.toIDR(parseInt(row.jumlahProduk * row.hargaBeli)),
            sortable: true,
          },
        ],
        produkExist: false,
        barcode: false,
        restProduk: [],
        model: '',
        options: []
      }
    },
    methods: {
      simpan() {
        this.$q.loading.show()
        this.$http.post(`/api/simpan/produk/${this.id}`, {}, { headers: this.$auth.getHeader() })
          .then(() => {
            this.$q.notify({
              type: 'positive',
              message: `Berhasil Menyimpan Barang`
            })
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
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
        this.$http.get(`/api/download/${this.id}`, {
          headers: this.$auth.getHeader(),
          responseType: 'arraybuffer'
        })
          .then((res) => {
            this.downloadFile(res, 'pemasukanbarang')
          })
      },
      getData() {
        this.$http.get(`/api/getprodukbaruvendor/${this.id}`, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.data = res.data;
            for (let i = 0; i < this.data.length; i++) {
              this.total += (this.data[i].jumlahProduk * this.data[i].hargaBeli)
            }
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      saveKategori() {
        if (this.kategoriProd === null) {
          this.$q.notify({
            type: 'negative',
            message: `Isi Semua field`
          })
          return
        }
        this.$q.loading.show()
        this.$http.post('/api/savekategori',
          {
            kategori: this.kategoriProd
          }, {
            headers: this.$auth.getHeader()
          })
          .then((res) => {
            this.kategoriProd = null
            this.getOptions()
            this.$q.notify({
              type: 'positive',
              message: `Berhasil menyimpan kategori`
            })
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      ubahProduk() {
        this.$q.loading.show()
        this.$http.post(`/api/ubahproduk/${this.selected[0].id}`, {
          hargaBeli: this.hargaBeli,
          jumlahBarang: this.jumlahBarang
        }, {
          headers: this.$auth.getHeader()
        })
          .then(() => {
            this.getData()
            this.$q.notify({
              type: 'positive',
              message: `Berhasil mengubah data`
            })
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      deleteProduk() {
        this.$q.loading.show()
        this.$http.delete(`/api/deleteproduk/${this.selected[0].id}`, {
          headers: this.$auth.getHeader()
        })
          .then(() => {
            this.getData()
            this.$q.notify({
              type: 'positive',
              message: `Berhasil menghapus data`
            })
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      update() {
        const obj = this.selected[0]
        this.hargaBeli = obj.hargaBeli
        this.jumlahBarang = obj.jumlahProduk
        this.ubah = true;
      },
      tambahProduk() {
        if (this.hargaBeli === null || this.hargaJualAnggota === null || this.hargaJualNonAnggota === null || this.jumlahBarang === null) {
          this.$q.notify({
            type: 'negative',
            message: `Isi Semua field`
          })
          return
        }
        this.$q.loading.show()
        this.$http.post(`/api/saveproduktambah/${this.id}/${this.restProduk.id}`, {
          hargaBeli: this.hargaBeli,
          hargaJualAnggota: this.hargaJualAnggota,
          hargaJualNonAnggota: this.hargaJualNonAnggota,
          jumlahBarang: this.jumlahBarang
        }, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.hargaBeli = null
            this.hargaJualAnggota = null
            this.hargaJualNonAnggota = null
            this.jumlahBarang = null
            this.barcode = false
            this.getData()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      simpanProdukBaru() {
        if (this.namaProduk === null || this.hargaBeli === null || this.hargaJualAnggota === null || this.hargaJualNonAnggota === null || this.jumlahBarang === null || this.model === null) {
          this.$q.notify({
            type: 'negative',
            message: `Isi Semua field`
          })
          return
        }
        this.$q.loading.show()
        this.$http.post(`/api/saveprodukbaru/${this.id}`, {
          namaProduk: this.namaProduk,
          hargaBeli: this.hargaBeli,
          hargaJualAnggota: this.hargaJualAnggota,
          hargaJualNonAnggota: this.hargaJualNonAnggota,
          kategoriProduk: this.model.value,
          barCode: this.kodeProduk,
          jumlahBarang: this.jumlahBarang
        }, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.namaProduk = null
            this.hargaBeli = null
            this.hargaJualAnggota = null
            this.hargaJualNonAnggota = null
            this.model = null
            this.kodeProduk = null
            this.jumlahBarang = null
            this.barcode = false
            this.getData()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      getProduk() {
        if (this.kodeProduk === null) {
          this.$q.notify({
            type: 'negative',
            message: `Isi Semua field`
          })
          return
        }
        this.$q.loading.show()
        this.$http.get(`/api/getproduk/${this.kodeProduk}`, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            const { data } = res
            this.produkExist = data.exist
            if (data.exist === true) {
              this.restProduk = data.data
            }
            this.barcode = true
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      simpanVendor() {
        if (this.namaVendor === null || this.alamat === null || this.noTelepon === null) {
          this.$q.notify({
            type: 'negative',
            message: `Isi Semua field`
          })
          return
        }
        this.$q.loading.show()
        this.$http.post('/api/simpanvendor', {
          name: this.namaVendor,
          alamat: this.alamat,
          telepon: this.noTelepon
        }, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.vendor = true;
            this.id = res.data.id
            this.$emit('change')
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
            this.$q.notify({
              type: 'negative',
              message: `Terjadi kesalahan, Refresh (F5)`
            })
          })
      },
      getOptions() {
        this.$http.get('/api/getoptions', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            const { data } = res
            this.options = []
            for (let i = 0; i < data.length; i++) {
              this.options.push({
                label: data[i].nama_kategori,
                value: data[i].id
              })
            }
            this.$q.loading.hide()
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
      }
    },
    created() {
      this.$q.loading.show()
      this.getOptions()
    }
  }
</script>

<style scoped>

</style>
