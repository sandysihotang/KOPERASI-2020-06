<template>
  <div class="q-pa-md">
    <q-card>
      <q-card-section v-if="exist === true">
        <q-item clickable v-ripple v-for="lap in data" :key="lap.id" @click="download(lap.id)">
          <q-item-section avatar>
            <q-avatar color="primary" text-color="white">
              <q-icon name="check"/>
            </q-avatar>
          </q-item-section>

          <q-item-section class="text-black">
            <q-item-label class="text-caption">Laporan Tahunan Koperasi
            </q-item-label>
            <q-item-label caption lines="1">
              <q-chip class="glossy" color="primary" text-color="white">Laporan tahun {{
                lap.tahun_laporan }}
              </q-chip>
            </q-item-label>
          </q-item-section>
        </q-item>
      </q-card-section>
      <q-card-section v-else>
        <center>
          <div class="text-purple">
            <q-icon name="fa fa-smile" size="xl"/>
          </div>
          <div class="text-black">
            Koperasi belum memiliki laporan tahunan
          </div>
        </center>
      </q-card-section>
    </q-card>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        data: [],
        exist: false
      }
    },
    methods: {
      base64ToArrayBuffer(base64) {
        const string = window.atob(base64);
        const len = string.length;
        const bytes = new Uint8Array(len);
        for (let i = 0; i < len; i++) {
          bytes[i] = string.charCodeAt(i);
        }
        return bytes.buffer;
      },
      download(id) {
        this.$q.loading.show()
        this.$http.get(`/api/downloadlaporankeuangan/${id}`, {
          headers: this.$auth.getHeader(),
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
      getLaporan() {
        this.$http.get('/api/getlaporankoperasiforanggota', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            const { data } = res
            this.exist = data.exist
            if (this.exist) {
              this.data = data.datatable
            }
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      }
    },
    created() {
      this.$q.loading.show()
      this.getLaporan()
    }
  }
</script>

<style scoped>

</style>
