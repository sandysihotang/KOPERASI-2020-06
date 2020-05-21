<template>
  <div class="q-pa-md">
    <q-table
      :dense="$q.screen.lt.md"
      title="Laporan Terkirim Ke Dinas Koperasi"
      :data="data"
      :columns="columns"
      :filter="filter"
      row-key="id"
      selection="single"
      :selected.sync="selected"
    >
      <template v-slot:top-right>
        <div class="row">
          <div class="col">
            <q-btn color="primary" icon="fa fa-eye" :disable="selected.length === 0"
                   size="xs"
                   label="Lihat Laporan" @click="lihatLaporan = true"/>
          </div>
          <div class="col">
            <q-btn color="primary" icon="edit" :disable="selected.length === 0"
                   size="xs"
                   label="Edit Laporan" @click="editLaporan = true"/>
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
    <q-dialog v-model="lihatLaporan" full-width full-height persistent transition-show="scale"
              transition-hide="scale">
      <isi-laporan v-bind:ss="selected"/>
    </q-dialog>
    <q-dialog v-model="editLaporan" full-width full-height persistent transition-show="scale"
              transition-hide="scale">
      <edit-laporan v-bind:ss="selected" v-on:getData="getData"/>
    </q-dialog>
  </div>
</template>

<script>
  import moment from 'moment'
  import XLSX from 'xlsx'
  import IsiLaporan from './IsiLaporan.vue'
  import EditLaporan from './EditLaporan.vue'

  export default {
    components: {
      IsiLaporan,
      EditLaporan
    },
    data() {
      return {
        lihatLaporan: false,
        filter: '',
        editLaporan: false,
        selected: [],
        data: [],
        columns: [
          {
            name: 'status',
            label: 'Status',
            align: 'center',
            field: row => (row.status === 1 ? 'Menunggu Approval' : row.status === 2 ? 'Sedang di Review' : 'Laporan Diterima'),
            sortable: true,
          }, {
            name: 'tahun',
            label: 'Laporan Tahun',
            align: 'center',
            field: row => row.tahun_laporan,
            sortable: true,
          }, {
            name: 'dikirim',
            label: 'Tanggal Pengiriman',
            align: 'center',
            field: row => this.getDate(row.created_at),
            sortable: true,
          }, {
            name: 'file',
            label: 'File',
            align: 'center',
            field: row => row.original_name,
            sortable: true,
          }
        ],
      }
    },
    methods: {
      getDate(date) {
        moment.lang('id')
        return moment(date)
          .format('dddd, Do MMMM YYYY')
      },
      getData() {
        this.$http.get('/api/getlaporanterkirimkoperasi', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.data = res.data.datatable
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
      this.getData()
    }
  }
</script>

<style scoped>

</style>
