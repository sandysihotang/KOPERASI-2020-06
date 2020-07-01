<template>
  <div class="q-pa-md">
    <div v-if="!adaAturanExist">
      <q-card
        class="my-card text-white"
        style="height: 100%; width:100%; background: radial-gradient(circle, #35a2ff 0%, #014a88 100%)"
      >
        <q-card-section>
          <div class="text-h6" align="center">Warning</div>
        </q-card-section>
        <q-card-section>
          <div class="row">
            <div class="col"/>
            <div class="col">
              <q-icon name="warning" class="text-red" style="font-size: 5rem;"/>
            </div>
            <div class="col"/>
          </div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <div align="justify">
            Silahkan isi pengaturan Simpanan
          </div>
        </q-card-section>
      </q-card>
    </div>
    <div>
      <q-table
        :dense="$q.screen.lt.md"
        title="Daftar Aktivasi Simpanan"
        :data="data"
        :columns="columns"
        :filter="filter">
        <template v-slot:top-right>
          <div class="row">
            <div class="col">
              <q-btn size="xs" color="green" label="Pengajuan Simpanan"
                     @click="pengajuanBaru = true"
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
        adaAturanExist: false,
        filter: '',
        pengajuanBaru: false,
        data: [],
        columns: [
          {
            name: 'status',
            label: 'Status',
            align: 'center',
            field: row => (row.aktif ? 'Aktif' : 'Tidak Aktif'),
            sortable: true,
          },{
            name: 'debitur',
            label: 'Debitur',
            align: 'center',
            field: row => row.debitur,
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
      },
      adaAturan() {
        this.$http.get('/api/getadaaturansimpanan', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.adaAturanExist = res.data.exist
            if (this.adaAturanExist) {
              this.getData()
            } else {
              this.$q.loading.hide()
            }
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      }
    },
    created() {
      this.$q.loading.show()
      this.adaAturan()
    }
  }
</script>

<style scoped>

</style>
