<template>
  <div class="q-pa-md">
    <q-table
      :dense="$q.screen.lt.md"
      title="Daftar Aktivasi Simpanan"
      :data="data"
      :columns="columns"
      :filter="filter">
      <template v-slot:top-right>
        <div class="row">
          <div class="col">
            <q-btn size="xs" color="green" label="Pengajuan Simpanan" @click="pengajuanBaru = true"
                   icon="table"/>
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
    <q-dialog v-model="pengajuanBaru" persistent transition-show="scale"
              transition-hide="scale">
      <q-card style="width: 700px; max-width: 80vw">
        <form-pengajuan-simpanan @call="getData"/>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
  import moment from 'moment'
  import FormPengajuanSimpanan from './Layout/FormPengajuanSimpanan.vue'

  export default {
    components: {
      FormPengajuanSimpanan
    },
    data() {
      return {
        filter: '',
        pengajuanBaru: false,
        data: [],
        columns: [
          {
            name: 'nama',
            label: 'Nama Nasabah',
            align: 'center',
            field: row => `${row.first_name} ${row.last_name}`,
            sortable: true,
          }, {
            name: 'status',
            label: 'Status',
            align: 'center',
            field: row => (row.aktif ? 'Aktif' : 'Tidak Aktif'),
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
            name: 'jumlahsetoran',
            label: 'Jumlah Setoran',
            align: 'center',
            field: row => this.toIDR(parseInt(row.total_simpanan)),
            sortable: true,
          }, {
            name: 'tanggalmulai',
            label: 'Tanggal Mulai',
            align: 'center',
            field: (row) => {
              moment.lang('id')
              return moment(row.tanggal_mulai)
                .format('dddd, Do MMMM YYYY')
            },
            sortable: true,
          }, {
            name: 'tanggalaktivasi',
            label: 'Tanggal Aktivasi',
            align: 'center',
            field: (row) => {
              moment.lang('id')
              return moment(row.created_at)
                .format('dddd, Do MMMM YYYY')
            },
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
        this.$http.get('/api/getanggotasimpananaktivasi', {
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
