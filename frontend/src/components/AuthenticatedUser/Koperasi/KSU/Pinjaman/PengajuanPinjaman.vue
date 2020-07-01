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
            Silahkan isi pengaturan pinjaman
          </div>
        </q-card-section>
      </q-card>
    </div>
    <div v-else>
      <q-table
        :dense="$q.screen.lt.md"
        title="Daftar Pengajuan Pinjaman"
        :data="data"
        :columns="columns"
        row-key="id"
        selection="single"
        :selected.sync="selected"
        :filter="filter">
        <template v-slot:top-right>
          <div class="row">
            <div class="col">
              <q-btn size="xs" color="green" label="Cetak Pengajuan" icon="print"
                     @click="cetakPengajuan"/>
            </div>
            <div class="col">
              <q-btn size="xs" color="green" label="Pengajuan Baru" icon="edit"
                     @click="pengajuanBaru = true"/>
            </div>
            <div class="col">
              <q-btn size="xs" color="green" label="Tabel Angsuran" icon="table"
                     @click="tabelAngsuran"/>
            </div>
            <div class="col">
              <q-btn size="xs" color="green" label="Persetujuan"
                     :disable="getDis()"
                     icon="check" @click="persetujuan"/>
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
      <q-dialog v-model="persetujuanView" persistent transition-show="scale"
                transition-hide="scale">
        <q-card style="width: 700px; max-width: 80vw;">
          <persetujuan-form :user="selected[0]" ref="saveFunction"/>
          <q-card-actions align="right" class="bg-white text-teal">
            <q-btn flat label="Close" v-close-popup/>
            <q-btn flat label="Simpan" v-close-popup @click="save"/>
          </q-card-actions>
        </q-card>
      </q-dialog>
      <q-dialog v-model="tableAngsuran" persistent transition-show="scale" transition-hide="scale">
        <q-card style="width: 700px; max-width: 80vw;">
          <table-angsuran :user="selected[0]"/>
          <q-card-actions align="right" class="bg-white text-teal">
            <q-btn flat label="Close" v-close-popup/>
          </q-card-actions>
        </q-card>
      </q-dialog>
      <q-dialog v-model="pengajuanBaru" persistent transition-show="scale" transition-hide="scale">
        <q-card style="width: 700px; max-width: 80vw;">
          <pengajuan-baru @get="getDataPinjaman"/>
          <q-card-actions align="right" class="bg-white text-teal">
            <q-btn flat label="Close" v-close-popup/>
          </q-card-actions>
        </q-card>
      </q-dialog>
    </div>

  </div>
</template>

<script>
  import moment from 'moment';
  import persetujuanForm from './Pengajuan/Persetujuan.vue';
  import tableAngsuran from './Pengajuan/TableAngsuran.vue';
  import PengajuanBaru from './Pengajuan/PengajuanBaru.vue';

  export default {
    components: {
      persetujuanForm,
      tableAngsuran,
      PengajuanBaru
    },
    data() {
      return {
        adaAturanExist: false,
        tableAngsuran: false,
        pengajuanBaru: false,
        status: ['Dibatalkan', 'Disetujui', 'Ditolak', 'Sedang Ditinjau', 'Menunggu Persetujuan', 'Close'],
        selected: [],
        data: [],
        columns: [
          {
            name: 'status',
            label: 'Status',
            align: 'center',
            field: row => this.status[row.status - 1],
            sortable: true,
          }, {
            name: 'noPinjaman',
            label: 'No Pinjaman',
            align: 'center',
            field: row => row.kode_pinjaman,
            sortable: true,
          }, {
            name: 'debitur',
            label: 'Debitur',
            align: 'center',
            field: row => row.debitur,
            sortable: true,
          }, {
            name: 'jumlahPinjaman',
            label: 'Jumlah Pinjaman',
            align: 'center',
            field: row => this.toIDR(parseInt(row.jumlah_pinjaman)),
            sortable: true,
          }, {
            name: 'jaminan',
            label: 'Jaminan',
            align: 'center',
            field: row => row.jaminan,
            sortable: true,
          },
          {
            name: 'tanggalPengajuan',
            label: 'Tanggal Pengajuan',
            align: 'center',
            field: (row) => {
              moment.lang('id')
              return moment(row.created_at)
                .format('dddd, Do MMMM YYYY');
            },
            sortable: true,
          },
          {
            name: 'termin',
            label: 'Termin (Bulan)',
            align: 'center',
            field: row => `${row.tenor} bulan`,
            sortable: true,
          },

        ],
        filter: '',
        persetujuanView: false
      }
    },
    methods: {
      getDis() {
        if (this.selected.length > 0) {
          return this.selected[0].status === 2 || this.selected[0].status === 6;
        }
        return true
      },
      cetakPengajuan() {
        if (!this.ck()) {
          this.$swal({
            position: 'center',
            type: 'error',
            title: 'Pilih item terlebih dahulu',
            showConfirmButton: false,
            timer: 1500,
          });
          return
        }
        this.$q.loading.show()
        this.$http.get(`/api/getdatapengajupdf/${this.selected[0].id}`, {
          headers: this.$auth.getHeader(),
          responseType: 'arraybuffer'
        })
          .then((res) => {
            this.downloadFile(res, 'pengajuan')
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      downloadFile(response, filename) {
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([response.data], { type: 'application/pdf' })

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
        link.download = `${filename}.pdf`
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
      save() {
        this.$refs.saveFunction.call()
        this.getDataPinjaman()
      },
      tabelAngsuran() {
        if (!this.ck()) {
          this.$swal({
            position: 'center',
            type: 'error',
            title: 'Pilih item terlebih dahulu',
            showConfirmButton: false,
            timer: 1500,
          });
          return;
        }
        this.tableAngsuran = true;
      },
      persetujuan() {
        if (!this.ck()) {
          this.$swal({
            position: 'center',
            type: 'error',
            title: 'Pilih item terlebih dahulu',
            showConfirmButton: false,
            timer: 1500,
          });
          return;
        }
        this.persetujuanView = true;
      },
      ck() {
        return this.selected.length > 0
      },
      getDataPinjaman() {
        this.$http.get('/api/getdatapengajuanpinjaman', {
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
      },
      adaAturan() {
        this.$http.get('/api/getadaaturanpinjaman', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.adaAturanExist = res.data.exist
            if (this.adaAturanExist) {
              this.getDataPinjaman()
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
  .my-card {
    width: 100%;
    height: 100%;
    max-height: 400px;
    max-width: 250px;
    margin-top: 40px;
    padding: 10px;
    position: absolute;
    top: 50%;
    left: 50%;
    margin-right: -50%;
    transform: translate(-50%, -50%)
  }
</style>
