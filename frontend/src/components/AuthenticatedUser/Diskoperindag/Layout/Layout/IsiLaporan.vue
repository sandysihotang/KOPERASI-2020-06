<template>
  <div>
    <q-card>
      <q-card-section>
        <div class="text-h6">Isi Laporan</div>
      </q-card-section>
      <q-separator/>
      <q-card-section v-if="pd === true">
        <embed class="full-width full-height"
               :src="`data:application/pdf;base64,${this.nn}`"/>
      </q-card-section>
      <q-card-section v-else>
        <div class="desc" v-for="htm in nn" :key="htm">
          <div v-html="htm"></div>
        </div>
      </q-card-section>
      <q-card-section>
        <center>
          <q-btn color="red" label="Tutup" v-close-popup/>&nbsp;
        </center>
      </q-card-section>
    </q-card>
  </div>
</template>

<script>

  import XLSX from 'xlsx';
  import mammoth from 'mammoth';

  export default {
    props: ['ss'],
    data() {
      return {
        pd: false,
        nn: [],
      }
    },
    methods: {
      showLaporan() {
        this.$q.loading.show()
        this.$http.get(`/api/getlaporanterkirim/${this.ss[0].id}`, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.pd = false
            const { data } = res
            this.nn = []
            if (data.ext === 'xls' || data.ext === 'xlsx') {
              const wb = XLSX.read(data.file, { type: 'base64' });
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
      base64ToArrayBuffer(base64) {
        const string = window.atob(base64);
        const len = string.length;
        const bytes = new Uint8Array(len);
        for (let i = 0; i < len; i++) {
          bytes[i] = string.charCodeAt(i);
        }
        return bytes.buffer;
      },
    },
    created() {
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
