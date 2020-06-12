<template>
  <div class="q-pa-md">
    <div class="row">
      <div class="col">
        <div class="q-pa-md">
          <q-card>
            <q-card-section>Pinjaman Jatuh Tempo</q-card-section>
            <q-card-section class="bg-primary text-white text-h6 text-center">
              {{ toIDR(jatuhTempo) }}
            </q-card-section>
          </q-card>
        </div>
      </div>
      <div class="col">
        <div class="q-pa-md">
          <q-card>
            <q-card-section>Pinjaman Belum Dibayar</q-card-section>
            <q-card-section class="bg-primary text-white text-h6 text-center">
              {{ toIDR(belumBayar) }}
            </q-card-section>
          </q-card>
        </div>
      </div>
      <div class="col">
        <div class="q-pa-md">
          <q-card>
            <q-card-section>Pinjaman Terbayar</q-card-section>
            <q-card-section class="bg-primary text-white text-h6 text-center">
              {{ toIDR(terbayar) }}
            </q-card-section>
          </q-card>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="q-pa-md full-width">
        <q-table
          :dense="$q.screen.lt.md"
          title="Daftar Transaksi Pinjaman"
          :data="data"
          :columns="columns"
          row-key="id"
          selection="single"
          :selected.sync="selected"
          :filter="filter">
          <template v-slot:top-right>
            <div class="row">
              <div class="col">
                <q-btn size="xs" color="green" label="Detail Peminjaman" icon="table"
                       @click="pembayaran"/>
              </div>
              <div class="col">
                <q-btn size="xs" color="green" label="Pembayaran"
                       icon="check" @click="tabelAngsuran"/>
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
      </div>
    </div>
    <q-dialog v-model="pembayaranView" persistent transition-show="scale" transition-hide="scale">
      <q-card style="width: 700px; max-width: 80vw;">
        <persetujuan-form :user="selected[0]"/>
        <q-card-actions align="right" class="bg-white text-teal">
          <q-btn flat label="Close" v-close-popup/>
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
  </div>
</template>

<script>
  import moment from 'moment';
  import persetujuanForm from './Transaksi/Pembayaran.vue';
  import tableAngsuran from './Transaksi/TableAngsuran.vue';

  export default {
    components: {
      persetujuanForm,
      tableAngsuran
    },
    data() {
      return {
        tableAngsuran: false,
        status: ['Cancel', 'Approved', 'Rejected', 'On Reviewed', 'Awaiting Approval', 'Close'],
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
            name: 'jumlahPinjaman',
            label: 'Jumlah Pinjaman',
            align: 'center',
            field: row => this.toIDR(parseInt(row.jumlah_pinjaman)),
            sortable: true,
          }, {
            name: 'namaDebitur',
            label: 'Nama Debitur',
            align: 'center',
            field: row => `${row.first_name} ${row.last_name}`,
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
        pembayaranView: false,
        jatuhTempo: 0,
        belumBayar: 0,
        terbayar: 0
      }
    },
    methods: {
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
      pembayaran() {
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
        this.pembayaranView = true;
      },
      ck() {
        return this.selected.length > 0
      },
      getDataTotal() {
        this.$http.get('/api/getpinjamanfortransasaksi', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            const { data } = res
            this.jatuhTempo = data.pinjamanJatuhTempo
            this.belumBayar = data.pinjamanbelumbayar
            this.terbayar = data.pinjamanterbayar
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
      getDataPinjaman() {
        this.$q.loading.show()
        this.$http.get('/api/getdatapengajuanapprove', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.data = res.data
            this.getDataTotal()
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
      }
    },
    created() {
      this.getDataPinjaman()
    }
  }
</script>

<style scoped>

</style>
