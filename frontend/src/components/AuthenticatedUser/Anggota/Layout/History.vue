<template>
  <div>
    <q-page-container>
      <div class="q-pa-md">
        <q-table
          :dense="$q.screen.lt.md"
          title="Daftar Transaksi Simpanan"
          :data="data"
          :columns="columns"
          row-key="id"
          :filter="filter">
          <template v-slot:top-right>
            <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
              <template v-slot:append>
                <q-icon name="search"/>
              </template>
            </q-input>
          </template>
        </q-table>
      </div>
    </q-page-container>
    <q-footer class="bg-white">
      <q-btn color="primary" class="full-width" icon="fa fa-arrow-left" to="/"/>
    </q-footer>
  </div>
</template>

<script>
  import moment from 'moment'

  export default {
    data() {
      return {
        filter: '',
        data: [],
        columns: [
          {
            name: 'notrx',
            label: 'No Transaksi',
            align: 'center',
            field: row => row.kode_transaksi,
            sortable: true,
          }, {
            name: 'namanasabah',
            label: 'Nama Nasabah',
            align: 'center',
            field: row => `${row.first_name} ${row.last_name}`,
            sortable: true,
          }, {
            name: 'produksimpanan',
            label: 'Produk Simpanan',
            align: 'center',
            field: (row) => {
              if (row.jenis_simpanan === 1) return 'Simpanan Pokok'
              if (row.jenis_simpanan === 2) return 'Simpanan Wajib'
              return 'Simpanan Sukarela'
            },
            sortable: true,
          }, {
            name: 'tipetransaksi',
            label: 'Tipe Transaksi',
            align: 'center',
            field: row => (row.jenis_transaksi === 1 ? 'Setor Dana' : 'Penarikan Dana'),
            sortable: true,
          }, {
            name: 'tanggaltransaksi',
            label: 'Tanggal Transaksi',
            align: 'center',
            field: (row) => {
              moment.lang('id')
              return moment(row.created_at)
                .format('dddd, Do MMMM YYYY')
            },
            sortable: true,
          }, {
            name: 'nominal',
            label: 'Nominal Transaksi',
            align: 'center',
            field: row => this.toIDR(parseInt(row.jumlah_transaksi)),
            sortable: true,
          }
        ],
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
      getData() {
        this.$http.get('/api/gettransaksisimpananuser', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.data = res.data
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
    }
  }
</script>

<style scoped>

</style>
