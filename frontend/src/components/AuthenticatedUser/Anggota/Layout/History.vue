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
            <q-input borderless dense debounce="300" v-model="filter" placeholder="Cari">
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
            label: 'Nasabah',
            align: 'center',
            field: row => `${row.debitur}`,
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
            const req = res.data
            const columns = JSON.parse(req.aturan.pattern_field)
            const { data } = req
            for (let i = 0; i < data.length; i++) {
              let str = `{`
              const obj = JSON.parse(data[i].data)
              for (let j = 0; j < obj.length; j++) {
                str = `${str} "${obj[j].uid}":`
                if (obj[j].value instanceof Object) {
                  const val = Object.values(obj[j].value)
                  for (let k = 0; k < val.length; k++) {
                    if (k === val.length - 1) {
                      if (j === obj.length - 1) {
                        str = `${str} ${val[k]}"`
                      } else {
                        str = `${str} ${val[k]}",`
                      }
                    } else if (k === 0) {
                      str = `${str} "${val[k]}`
                    } else {
                      str = `${str} ${val[k]}`
                    }
                  }
                } else if (j === obj.length - 1) {
                  str = `${str} "${obj[j].value}"`
                } else {
                  str = `${str} "${obj[j].value}",`
                }
              }
              str = `${str} }`
              const ll = JSON.parse(str)
              let strs = '';
              for (let i = 0; i < columns.length; i++) {
                if (i === columns.length - 1) {
                  strs = `${strs} ${columns[i].label}: ${ll[columns[i].cid]}`
                } else {
                  strs = `${strs} ${columns[i].label}: ${ll[columns[i].cid]} `
                }
              }
              data[i].debitur = strs;
            }
            this.data = data
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
