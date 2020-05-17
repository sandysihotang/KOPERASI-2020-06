<template>
  <div class="q-pa-md">
    <q-table
      :dense="$q.screen.lt.md"
      title="Daftar Vendor"
      :data="data"
      :columns="columns"
      :filter="filter"
      row-key="id"
      selection="single"
      :selected.sync="selected">
      <template v-slot:top-right>
        <div class="row">
          <div class="col">
            <q-btn size="xs" color="green" label="Lihat Barang Masuk" @click="ven = true"
                   :disable="selected.length === 0"
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

    <q-dialog v-model="ven" persistent transition-show="scale"
              transition-hide="scale">
      <daftar-produk-vendor :vendor="selected[0]"/>
    </q-dialog>
  </div>
</template>

<script>
  import moment from 'moment'
  import DaftarProdukVendor from './Layout/DaftarProdukVendor.vue'

  export default {
    components: {
      DaftarProdukVendor
    },
    data() {
      return {
        ven: false,
        selected: [],
        filter: '',
        data: [],
        columns: [
          {
            name: 'namavendor',
            label: 'Nama Vendor',
            align: 'center',
            field: row => row.nama_vendor,
            sortable: true,
          }, {
            name: 'alamat',
            label: 'Alamat Vendor',
            align: 'center',
            field: row => row.alamat_vendor,
            sortable: true,
          }, {
            name: 'notelepom',
            label: 'No Telepon Vendor',
            align: 'center',
            field: row => row.no_telepon,
            sortable: true,
          }, {
            name: 'tanggalmasuk',
            label: 'Tanggal Masuk Barang',
            align: 'center',
            field: (row) => {
              moment.lang('id')
              return moment(row.tanggal_masuk)
                .format('dddd, Do MMMM YYYY')
            },
            sortable: true,
          },
        ],
      }
    },
    methods: {
      getData() {
        this.$http.get('/api/getdatavendorkoperasi', {
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
