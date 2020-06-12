<template>
  <div class="q-pa-md">
    <q-table
      :dense="$q.screen.lt.md"
      title="Daftar Transaksi Simpanan"
      :data="data"
      :columns="columns"
      row-key="id"
      :filter="filter">
      <template v-slot:top-right>
        <div class="row">
          <div class="col">
            <q-btn-dropdown size="xs" color="green" label="Setor Dana" icon="fa fa-arrow-up">
              <q-list>
                <q-item clickable v-close-popup @click="setorDanaWajib = true">
                  <q-item-section>
                    <q-item-label>Simpanan Wajib</q-item-label>
                  </q-item-section>
                </q-item>
                <q-item clickable v-close-popup @click="setorDanaSukarela = true">
                  <q-item-section>
                    <q-item-label>Simpanan Sukarela</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </q-btn-dropdown>
          </div>
          <div class="col">
            <q-btn-dropdown size="xs" color="green" label="Penarikan Dana" icon="fa fa-arrow-down">
              <q-list>
                <q-item clickable v-close-popup @click="penarikanDanaWajib = true">
                  <q-item-section>
                    <q-item-label>Simpanan Wajib</q-item-label>
                  </q-item-section>
                </q-item>
                <q-item clickable v-close-popup @click="penarikanDanaSukarela = true">
                  <q-item-section>
                    <q-item-label>Simpanan Sukarela</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </q-btn-dropdown>
          </div>
          <div class="col">
            <q-input borderless dense debounce="300" v-model="filter" placeholder="Cari">
              <template v-slot:append>
                <q-icon name="search"/>
              </template>
            </q-input>
          </div>
        </div>
      </template>
    </q-table>
    <q-dialog v-model="setorDanaWajib" persistent transition-show="scale"
              transition-hide="scale">
      <q-card style="width: 700px; max-width: 80vw">
        <form-setor-dana-wajib @refresh="getData"/>
      </q-card>
    </q-dialog>
    <q-dialog v-model="setorDanaSukarela" persistent transition-show="scale"
              transition-hide="scale">
      <q-card style="width: 700px; max-width: 80vw">
        <form-setor-dana-sukarela @refresh="getData"/>
      </q-card>
    </q-dialog>
    <q-dialog v-model="penarikanDanaSukarela" persistent transition-show="scale"
              transition-hide="scale">
      <q-card style="width: 700px; max-width: 80vw">
        <form-penarikan-dana-sukarela @refresh="getData"/>
      </q-card>
    </q-dialog>
    <q-dialog v-model="penarikanDanaWajib" persistent transition-show="scale"
              transition-hide="scale">
      <q-card style="width: 700px; max-width: 80vw">
        <form-penarikan-dana-wajib @refresh="getData"/>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
  import moment from 'moment'
  import FormSetorDanaWajib from './Layout/FormSetorDanaWajib.vue'
  import FormSetorDanaSukarela from './Layout/FormSetorDanaSukarela.vue'
  import FormPenarikanDanaSukarela from './Layout/FormPenarikanDanaSukarela.vue'
  import FormPenarikanDanaWajib from './Layout/FormPenarikanDanaWajib.vue'

  export default {
    components: {
      FormSetorDanaWajib,
      FormSetorDanaSukarela,
      FormPenarikanDanaSukarela,
      FormPenarikanDanaWajib
    },
    data() {
      return {
        penarikanDanaWajib: false,
        penarikanDanaSukarela: false,
        setorDanaSukarela: false,
        setorDanaWajib: false,
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
        this.$http.get('/api/gettransaksisimpanan', {
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
