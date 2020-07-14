<template>
  <div class="q-pa-md">
    <q-card>
      <q-card-section>
        <q-table
          :dense="$q.screen.lt.md"
          title="Daftar Barang yang Dibeli"
          :data="data"
          :columns="columns"/>
      </q-card-section>
      <q-card-section>
        <center>
          <q-btn label="Tutup" color="red" v-close-popup/>&nbsp;
        </center>
      </q-card-section>
    </q-card>
  </div>
</template>

<script>
  export default {
    props: ['vendor'],
    data() {
      return {
        data: [],
        columns: [
          {
            name: 'namaproduk',
            label: 'Nama Barang',
            align: 'center',
            field: row => row.nama_produk,
            sortable: true,
          }, {
            name: 'hargabeli',
            label: 'Harga Beli',
            align: 'center',
            field: row => this.toIDR(parseInt(row.harga_beli)),
            sortable: true,
          }, {
            name: 'hargajualanggota',
            label: 'Harga Jual Anggota',
            align: 'center',
            field: row => this.toIDR(parseInt(row.harga_jual_anggota)),
            sortable: true,
          }, {
            name: 'hargajualnonanggota',
            label: 'Harga Jual Non Anggota',
            align: 'center',
            field: row => this.toIDR(parseInt(row.harga_jual_non_anggota)),
            sortable: true,
          }, {
            name: 'jumlahproduk',
            label: 'Jumlah Barang',
            align: 'center',
            field: row => row.jumlah_produk,
            sortable: true,
          },
        ],
      }
    },
    methods: {
      getData() {
        this.$http.get(`/api/getprodukbyvendor/${this.vendor.id}`, { headers: this.$auth.getHeader() })
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
