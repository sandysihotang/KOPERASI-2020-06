<template>
  <div class="q-pa-md">
    <q-table
      :dense="$q.screen.lt.md"
      title="Daftar Produk"
      :data="data"
      :columns="columns"
      :filter="filter">
      <template v-slot:top-right>
        <div class="row">
          <div class="col">
            <q-btn size="xs" color="green" label="Pemasukan Barang"
                   @click="formpenambahanproduk = true"
                   icon="table"/>
          </div>
          <div class="col">
            <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
              <template v-slot:append>
                <q-icon name="search"/>
              </template>
            </q-input>
          </div>
        </div>
      </template>
    </q-table>
    <q-dialog v-model="formpenambahanproduk" persistent transition-show="scale"
              transition-hide="scale" :full-width="bg" :full-height="bg">
      <form-penambahan-produk @change="changeBG"/>
    </q-dialog>
  </div>
</template>

<script>
  import FormPenambahanProduk from './Layout/FormPenambahanProduk.vue'

  export default {
    components: {
      FormPenambahanProduk
    },
    data() {
      return {
        bg: false,
        filter: '',
        data: [],
        columns: [
          {
            name: 'namabarang',
            label: 'Nama Barang',
            align: 'center',
            field: row => row.namaProduk,
            sortable: true,
          }, {
            name: 'kodebarang',
            label: 'Kode barang',
            align: 'center',
            field: row => row.kodeProduk,
            sortable: true,
          }, {
            name: 'kategoribarang',
            label: 'Kategori Barang',
            align: 'center',
            field: row => row.kategoriProduk.namaKategori,
            sortable: true,
          }, {
            name: 'hargabeli',
            label: 'Harga Beli',
            align: 'center',
            field: (row) => {
              const { harga } = row
              let hargaBeli = ''
              for (let i = 0; i < harga.length; i++) {
                if (harga[i].status === true) {
                  hargaBeli = this.toIDR(harga[i].hargaBeli)
                  break
                }
              }
              return hargaBeli
            },
            sortable: true,
          }, {
            name: 'hargajualanggota',
            label: 'Harga Jual Anggota',
            align: 'center',
            field: (row) => {
              const { harga } = row
              let jual = ''
              for (let i = 0; i < harga.length; i++) {
                if (harga[i].status === true) {
                  jual = this.toIDR(harga[i].hargaJualAnggota)
                  break
                }
              }
              return jual
            },
            sortable: true,
          }, {
            name: 'hargajualnonanggota',
            label: 'Harga Jual Non Anggota',
            align: 'center',
            field: (row) => {
              const { harga } = row
              let jual = ''
              for (let i = 0; i < harga.length; i++) {
                if (harga[i].status === true) {
                  jual = this.toIDR(harga[i].hargaJualNonAnggota)
                  break
                }
              }
              return jual
            },
            sortable: true,
          }, {
            name: 'jumlahbarang',
            label: 'Jumlah Barang',
            align: 'center',
            field: row => row.jumlahProduk,
            sortable: true,
          },
        ],
        formpenambahanproduk: false
      }
    },
    methods: {
      changeBG() {
        this.bg = true
      },
      getData() {
        this.$http.get('/api/getdataproduk', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.data = res.data
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
      this.getData()
    }
  }
</script>

<style scoped>

</style>
