<template>
  <div>
    <q-card>
      <q-card-section>
        <div class="text-h6">Isi Laporan</div>
      </q-card-section>
      <q-separator/>
      <q-card-section>
        <div class="desc" v-for="htm in nn" :key="htm">
          <div v-html="htm"></div>
        </div>
      </q-card-section>
      <q-card-section>
        <center>
          <q-btn color="red" label="Close" v-close-popup/>&nbsp;
        </center>
      </q-card-section>
    </q-card>
  </div>
</template>

<script>

  import XLSX from 'xlsx';

  export default {
    props: ['ss'],
    data() {
      return {
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
            const { data } = res
            const wb = XLSX.read(data.file, { type: 'base64' });
            this.nn = []
            for (let i = 0; i < wb.SheetNames.length; i++) {
              const ws = wb.Sheets[wb.SheetNames[i]];
              this.nn.push(XLSX.utils.sheet_to_html(ws, {}))
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
      }
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
