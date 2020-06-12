<template>
  <div class="q-pa-md">
    <q-table
      :dense="$q.screen.lt.md"
      title="Daftar Transaksi Produk Terkini"
      :data="data"
      :columns="columns"
      :filter="filter"
      row-key="id"
      selection="single"
      :selected.sync="selected">
      <template v-slot:top-right>
        <q-btn size="xs" color="green" label="Detail Transaksi" icon="print"
               @click="detailTransaksi" :disable="selected.length === 0"/>&nbsp;
        <q-input borderless dense debounce="300" v-model="filter" placeholder="Cari">
          <template v-slot:append>
            <q-icon name="search"/>
          </template>
        </q-input>
      </template>
    </q-table>
    <q-dialog v-model="detail" persistent transition-show="scale" transition-hide="scale">
      <q-card style="width: 700px; max-width: 80vw;">
        <q-card-section>
          <q-table
            :dense="$q.screen.lt.md"
            title="Daftar Transaksi Produk Terkini"
            :data="datadetail"
            :columns="columnsdetail"/>
        </q-card-section>
        <q-card-actions align="right" class="bg-white text-teal">
          <q-btn flat label="Close" v-close-popup/>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
  import moment from 'moment'

  export default {
    data() {
      return {
        detail: false,
        selected: [],
        filter: '',
        data: [],
        columns: [
          {
            name: 'kodetran',
            label: 'Kode Transaksi',
            align: 'center',
            field: row => row.kode_transaksi,
            sortable: true,
          },
          {
            name: 'tgltransaksi',
            label: 'Tanggal Transaksi',
            align: 'center',
            field: (row) => {
              moment.lang('id')
              return moment(row.tanggal_transaksi)
                .format('dddd, Do MMMM YYYY');
            },
            sortable: true,
          },
          {
            name: 'pembeli',
            label: 'Pembeli',
            align: 'center',
            field: row => (row.keanggotaan === false ? 'Bukan Anggota' : 'Anggota'),
            sortable: true,
          },
          {
            name: 'totalbeli',
            label: 'Total Beli',
            align: 'center',
            field: row => this.toIDR(parseInt(row.uang_masuk)),
            sortable: true,
          }
        ],
        datadetail: [],
        columnsdetail: [
          {
            name: 'namaproduk',
            label: 'Nama Produk',
            align: 'center',
            field: row => row.nama_produk,
            sortable: true,
          }, {
            name: 'kodeproduk',
            label: 'Kode Produk',
            align: 'center',
            field: row => row.kode_produk,
            sortable: true,
          }, {
            name: 'jumlah',
            label: 'Jumlah',
            align: 'center',
            field: row => row.jumlah_beli,
            sortable: true,
          }, {
            name: 'harga',
            label: 'Harga',
            align: 'center',
            field: row => this.toIDR(parseInt((row.keanggotaan ? row.harga_jual_anggota : row.harga_jual_non_anggota))),
            sortable: true,
          }, {
            name: 'total',
            label: 'Total',
            align: 'center',
            field: row => this.toIDR(parseInt(row.jumlah_beli * (row.keanggotaan ? row.harga_jual_anggota : row.harga_jual_non_anggota))),
            sortable: true,
          },
        ]
      }
    },
    methods: {
      detailTransaksi() {
        this.$q.loading.show()
        this.$http.get(`/api/getdetailtransaksiproduk/${this.selected[0].id}`, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.datadetail = res.data
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
            this.$swal({
              position: 'center',
              type: 'error',
              title: 'Terjadi kesalahan, Refresh (F5)',
              showConfirmButton: false,
              timer: 1500,
            });
          })
        this.detail = true
      },
      getData() {
        this.$http.get('/api/gettransaksiterkiniprod', { headers: this.$auth.getHeader() })
          .then((res) => {
            this.data = res.data
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
            this.$swal({
              position: 'center',
              type: 'error',
              title: 'Terjadi kesalahan, Refresh (F5)',
              showConfirmButton: false,
              timer: 1500,
            });
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
