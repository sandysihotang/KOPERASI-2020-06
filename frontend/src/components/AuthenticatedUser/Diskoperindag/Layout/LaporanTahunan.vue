<template>
  <div class="q-pa-md">
    <q-table
      :dense="$q.screen.lt.md"
      title="Daftar Laporan Tahunan"
      :data="data"
      :columns="columns"
      :filter="filter"
      row-key="id"
      selection="single"
      :selected.sync="selected"
    >
      <template v-slot:top-right>

        <q-btn color="primary" icon="fa fa-eye" :disable="selected.length === 0"
               size="xs"
               label="Lihat Laporan" @click="lihatLaporan = true"/>
        <q-btn color="primary" icon="send"
               :disable="selected.length === 0 || selected[0].status === 3"
               size="xs"
               label="Kirim Pesan" @click="pes = true"/>
        <q-btn color="primary" icon="edit"
               :disable="selected.length === 0 || selected[0].status === 3"
               size="xs"
               label="Ubah Status" @click="changes"/>
        <q-btn color="primary" icon="fa fa-download" :disable="selected.length === 0"
               size="xs"
               label="Unduh Laporan" @click="download"/>
        <q-input borderless dense debounce="300" style="margin-left: 15px" v-model="filter"
                 placeholder="Cari">
          <template v-slot:append>
            <q-icon name="search"/>
          </template>
        </q-input>
      </template>
    </q-table>
    <q-dialog v-model="lihatLaporan" full-width full-height persistent transition-show="scale"
              transition-hide="scale">
      <isi-laporan v-bind:ss="selected"/>
    </q-dialog>
    <q-dialog v-model="change" persistent transition-show="scale"
              transition-hide="scale">
      <q-card>
        <q-card-section>
          <div class="text-h6"> Ubah Status Laporan</div>
        </q-card-section>
        <q-separator/>
        <q-card-section>
          <q-select outlined v-model="status" :options="options" :dense="true" label="Tahun"
                    :options-dense="false">
            <template v-slot:prepend>
              <q-icon name="event"/>
            </template>
          </q-select>
        </q-card-section>
        <q-card-section>
          <center>
            <q-btn color="red" label="Tutup" v-close-popup/>&nbsp;&nbsp;
            <q-btn color="primary" label="Ubah Status" @click="ubahStatus"/>
          </center>
        </q-card-section>
      </q-card>
    </q-dialog>
    <q-dialog v-model="pes" persistent transition-show="scale"
              transition-hide="scale">
      <q-card>
        <q-card-section>
          <div class="text-h6">Kirim Pesan Untuk Koperasi</div>
        </q-card-section>
        <q-separator/>
        <q-card-section>
          <div class="q-pa-md" style="max-width: 300px">
            <q-input
              v-model="text"
              filled
              label="Pesan"
              type="textarea"
            />
          </div>
          <br>
          <center>
            <q-btn color="red" label="Tutup" v-close-popup/>&nbsp;
            <q-btn color="primary" label="Kirim Pesan" @click="kirimPesan" v-close-popup/>
          </center>
        </q-card-section>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
  import moment from 'moment'
  import IsiLaporan from './Layout/IsiLaporan.vue'

  export default {
    components: {
      IsiLaporan
    },
    data() {
      return {
        text: null,
        pes: false,
        status: null,
        options: ['Request', 'In Review', 'Approve'],
        change: false,
        lihatLaporan: false,
        selected: [],
        data: [],
        columns: [
          {
            name: 'status',
            label: 'status',
            align: 'center',
            field: row => (row.status === 1 ? 'Request' : row.status === 2 ? 'In Riview' : 'Approve'),
            sortable: true,
          }, {
            name: 'koperasi',
            label: 'Koperasi',
            align: 'center',
            field: row => row.nama_koperasi,
            sortable: true,
          }, {
            name: 'alamat',
            label: 'Alamat Koperasi',
            align: 'center',
            field: row => row.alamat_koperasi,
            sortable: true,
          }, {
            name: 'file',
            label: 'File',
            align: 'center',
            field: row => row.original_name,
            sortable: true,
          }, {
            name: 'laporan',
            label: 'Tahun Laporan',
            align: 'center',
            field: row => row.tahun_laporan,
            sortable: true,
          }, {
            name: 'pengajuan',
            label: 'Tanggal Pengiriman',
            align: 'center',
            field: row => this.getDate(row.created_at),
            sortable: true,
          },
        ],
        filter: ''
      }
    },
    methods: {
      kirimPesan() {
        this.$q.loading.show()
        this.$http.post(`/api/kirimpesanlaporan/${this.selected[0].id}`, {
          text: this.text
        }, {
          headers: this.$auth.getHeader()
        })
          .then(() => {
            this.$q.notify({
              type: 'positive',
              message: `Berhasil mengirim pesan`
            })
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.notify({
              type: 'negative',
              message: `Gagal mengirim pesan, refresh (F5)`
            })
            this.$q.loading.hide()
          })
      },
      ubahStatus() {
        this.$q.loading.show()
        this.$http.put(`/api/changestatuslaporan/${this.selected[0].id}`, {
          statusLaporan: this.status
        }, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.change = false
            this.$q.notify({
              type: 'positive',
              message: `Berhasil mengubah Status`
            })
            this.getData()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      changes() {
        this.status = (this.selected[0].status === 1 ? 'Request' : (this.selected[0].status === 2 ? 'In Review' : 'Approve'))
        this.change = true
      },
      base64ToArrayBuffer(base64) {
        const string = window.atob(base64);
        const len = string.length;
        const bytes = new Uint8Array(len);
        for (let i = 0; i < len; i++) {
          bytes[i] = string.charCodeAt(i);
        }
        return bytes.buffer;
      },
      download() {
        this.$q.loading.show()
        this.$http.get(`/api/downloadlaporankeuangan/${this.selected[0].id}`, {
          headers: this.$auth.getHeader(),
          // responseType: 'arraybuffer'
        })
          .then((res) => {
            this.downloadFile(this.base64ToArrayBuffer(res.data.file), 'laporankeuangan', res.data.ext)
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.notify({
              type: 'negative',
              message: `Terjadi kesalahan refresh (F5)`
            })
            this.$q.loading.hide()
          })
      },
      downloadFile(response, filename, ext) {
        if (ext === 'xls' || ext === 'xlsx') {
          const newBlob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })

          if (window.navigator && window.navigator.msSaveOrOpenBlob) {
            window.navigator.msSaveOrOpenBlob(newBlob)
            return
          }
          const data = window.URL.createObjectURL(newBlob)
          const link = document.createElement('a')
          link.href = data
          link.download = `${filename}.xlsx`
          link.click()
          setTimeout(() => {
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
        } else if (ext === 'pdf') {
          const newBlob = new Blob([response], { type: 'application/pdf' })
          if (window.navigator && window.navigator.msSaveOrOpenBlob) {
            window.navigator.msSaveOrOpenBlob(newBlob)
            return
          }
          const data = window.URL.createObjectURL(newBlob)
          const link = document.createElement('a')
          link.href = data
          link.download = `${filename}.pdf`
          link.click()
          setTimeout(() => {
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
        } else {
          const newBlob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' })

          if (window.navigator && window.navigator.msSaveOrOpenBlob) {
            window.navigator.msSaveOrOpenBlob(newBlob)
            return
          }
          const data = window.URL.createObjectURL(newBlob)
          const link = document.createElement('a')
          link.href = data
          link.download = `${filename}.docx`
          link.click()
          setTimeout(() => {
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
        }
      },
      getDate(date) {
        moment.lang('id')
        return moment(date)
          .format('dddd, Do MMMM YYYY')
      },
      getData() {
        this.$http.get('/api/getlaporankoperasikabtoba', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            const { data } = res
            this.data = data.datatable
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
