<template>
  <div class="q-pa-md">
    <div class="row">
      <q-card class="full-width">
        <q-card-section>
          <div class="text-h6">Kirim Laporan Tahunan</div>
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
          <q-btn color="primary" icon-right="send" label="Kirim Laporan" @click="kirim"/>
        </q-card-section>
      </q-card>
    </div>
    <br>
    <div class="row" v-if="change">
      <q-card class="full-width">
        <q-card-section v-if="pd === true">
          <embed class="full-width full-height" type="application/pdf" :src="`data:application/pdf;base64,${this.nn}#page=1`"/>
        </q-card-section>
        <q-card-section v-else>
          <div v-for="htm in nn" :key="htm">
            <div v-html="htm"></div>
          </div>
        </q-card-section>
      </q-card>
    </div>
  </div>
</template>

<script>
  import XLSX from 'xlsx';
  import mammoth from 'mammoth';

  export default {
    data() {
      return {
        pd: false,
        file: null,
        nn: [],
        change: false,
        model: null,
        excel: null,
        tahun: [],
        tahunKirim: null,
        options: []
      }
    },
    methods: {
      kirim() {
        if (this.tahunKirim === null || this.excel === null) {
          this.$q.notify({
            type: 'negative',
            message: `Isi Semua Field`
          })
          return
        }
        this.$q.loading.show()
        this.$http.post('/api/kirimlaporankoperasi', this.useFormData(), {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            const { data } = res
            if (data.errCode === 400) {
              this.$q.notify({
                type: 'negative',
                message: data.err
              })
            } else {
              this.$q.notify({
                type: 'positive',
                message: 'Pengajuan Laporan Berhasil dilakukan'
              })
            }
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
      useFormData() {
        const formData = new FormData();
        formData.append('files', this.excel);
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
                this.nn = []
                this.nn.push(resultObject.value)
              })
            this.change = true
            this.$q.loading.hide()
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
            this.$q.loading.hide()
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
            this.$q.loading.hide()
          }
        } else {
          this.$q.notify({
            type: 'negative',
            message: `File Harus dengan Format xls / xlsx, Docx dan Pdf`
          })
          this.model = null
          this.$q.loading.hide()
        }
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
      getData() {
        this.$http.get('/api/getonefile', {
          headers: this.$auth.getHeader(),
        })
          .then((res) => {
            const { data } = res
            const wb = XLSX.read(data.file, { type: 'base64' });
            this.nn = []
            for (let i = 0; i < wb.SheetNames.length; i++) {
              const ws = wb.Sheets[wb.SheetNames[i]];
              this.nn.push(XLSX.utils.sheet_to_html(ws, {}))
            }
          })
          .catch(() => {

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
      }
    },
    created() {
      this.getOptions()
    }
  }
</script>

<style>
  table {
    width: 100%;
    border-collapse: collapse;
  }

  /* Zebra striping */
  tr:nth-of-type(odd) {
    background: #eee;
  }

  th {
    background: #333;
    color: white;
    font-weight: bold;
  }

  td, th {
    padding: 6px;
    border: 1px solid #ccc;
    text-align: left;
  }
</style>
