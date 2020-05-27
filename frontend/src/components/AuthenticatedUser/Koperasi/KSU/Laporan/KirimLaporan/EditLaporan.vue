<template>
  <div>
    <q-card>
      <q-card-section>
        <div class="text-h6">Edit Pengiriman Laporan</div>
      </q-card-section>
      <q-separator/>
      <q-card-section>
        <q-file filled bottom-slots type="file" v-model="model" label="Pilih File" counter
                @input="changeFile">
          <template v-slot:prepend>
            <q-icon name="cloud_upload" @click.stop/>
          </template>
          <template v-slot:append>
            <q-icon name="close" @click.stop="model = null" class="cursor-pointer"/>
          </template>
          <template v-slot:hint>
            File
          </template>
        </q-file>
        <br>
        <q-select outlined v-model="tahunKirim" :options="options" :dense="true" label="Tahun"
                  :options-dense="false">
          <template v-slot:prepend>
            <q-icon name="event"/>
          </template>
        </q-select>
        <br>
        <div class="row">
          <q-btn color="red" label="close" v-close-popup/>
          <q-btn color="primary" icon-right="send" label="Simpan" @click="simpan"/>
        </div>
      </q-card-section>
      <q-separator/>
      <q-card-section v-if="pd === true">
        <pdf :src="`data:application/octet-stream;base64,${this.nn}`"></pdf>
      </q-card-section>
      <q-card-section v-else>
        <div class="desc" v-for="htm in nn" :key="htm">
          <div v-html="htm"></div>
        </div>
      </q-card-section>
      <q-separator/>
    </q-card>
  </div>
</template>

<script>
  import XLSX from 'xlsx'
  import mammoth from 'mammoth'

  export default {
    components: {
      pdf: () => import('pdfvuer')
    },
    props: ['ss'],
    data() {
      return {
        nn: [],
        tahunKirim: this.ss.tahun_laporan,
        model: null,
        options: [],
        excel: null,
        pd: false
      }
    },
    methods: {
      simpan() {
        this.$q.loading.show()
        this.$http.put(`/api/simpaneditlaporan/${this.ss[0].id}`, this.useFormData(), {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.$q.notify({
              type: 'positive',
              message: 'Edit Laporan Berhasil Dilakukan'
            })
            this.$emit('getData')
          })
          .catch(() => {
            this.$q.notify({
              type: 'negative',
              message: `Terjadi kesalahan refresh (F5)`
            })
            this.$q.loading.hide()
          })
      },
      useFormData() {
        const formData = new FormData();
        if (this.excel) formData.append('files', this.excel);
        formData.append('tahun', this.tahunKirim);
        return formData;
      },
      changeFile(e) {
        this.excel = e
        this.pd = false
        if (e === undefined) {
          this.$q.notify({
            type: 'negative',
            message: `File Harus dengan Format xls / xlsx, Docx dan Pdf`
          })
          this.model = null
          return
        }
        this.$q.loading.show();
        if (e.type === 'application/vnd.openxmlformats-officedocument.wordprocessingml.document') {
          const reader = new FileReader();
          reader.onloadend = (event) => {
            const arrayBuffer = reader.result
            mammoth.convertToHtml({ arrayBuffer: arrayBuffer })
              .then((resultObject) => {
                this.nn.push(resultObject.value)
              })
            this.change = true
          }
          reader.readAsArrayBuffer(e);
        } else if (e.type === 'application/pdf') {
          const reader = new FileReader();
          reader.readAsArrayBuffer(e)
          reader.onload = (e) => {
            const table = Buffer.from(e.target.result)
              .toString("base64")
            this.nn = table
            this.pd = true
            this.change = true
          }
        } else if (e.type === 'application/vnd.ms-excel') {
          const reader = new FileReader();
          reader.readAsArrayBuffer(e)
          reader.onload = (e) => {
            const table = Buffer.from(e.target.result)
              .toString("base64")
            const wb = XLSX.read(table, { type: 'base64' });
            this.nn = []
            for (let i = 0; i < wb.SheetNames.length; i++) {
              const ws = wb.Sheets[wb.SheetNames[i]];
              this.nn.push(XLSX.utils.sheet_to_html(ws, {}))
            }
            this.change = true
          }
        } else {
          this.$q.notify({
            type: 'negative',
            message: `File Harus dengan Format xls / xlsx, Docx dan Pdf`
          })
          this.model = null
        }
        this.$q.loading.hide()
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
      showLaporan() {
        this.$q.loading.show()
        this.$http.get(`/api/getlaporanterkirim/${this.ss[0].id}`, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.pd = false
            const { data } = res
            if (data.ext === 'xls' || data.ext === 'xlsx') {
              const wb = XLSX.read(data.file, { type: 'base64' });
              this.nn = []
              for (let i = 0; i < wb.SheetNames.length; i++) {
                const ws = wb.Sheets[wb.SheetNames[i]];
                this.nn.push(XLSX.utils.sheet_to_html(ws, {}))
              }
            } else if (data.ext === 'pdf') {
              this.nn = data.file
              this.pd = true
            } else {
              mammoth.convertToHtml({ arrayBuffer: this.base64ToArrayBuffer(data.file) })
                .then((resultObject) => {
                  this.nn.push(resultObject.value)
                })
            }
            this.$q.loading.hide()
          })
          .catch((err) => {
            this.$q.notify({
              type: 'negative',
              message: `Terjadi kesalahan refresh (F5)`
            })
            this.$q.loading.hide()
          })
      },
      getOptions() {
        const date = new Date()
        const year = date.getFullYear() - 10;
        let t = year
        this.options = []
        while (t < year + 20) {
          this.options.push(t)
          t++
        }
        this.tahunKirim = this.ss[0].tahun_laporan
      }
    },
    created() {
      this.getOptions()
      this.showLaporan()
    }
  }
</script>

<style scoped>
  .desc >>> table {
    width: 100%;
    border-collapse: collapse;
    color: #211b19;
  }

  /* Zebra striping */
  .desc >>> tr:nth-of-type(odd) {
    background: #eee;
  }

  .desc >>> th {
    background: #333;
    color: white;
    font-weight: bold;
  }

  .desc >>> td, .desc >>> th {
    padding: 6px;
    border: 1px solid #ccc;
    text-align: left;
  }
</style>
