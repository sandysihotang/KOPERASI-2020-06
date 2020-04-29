<template>
  <div>
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
            <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
              <template v-slot:append>
                <q-icon name="search"/>
              </template>
            </q-input>
          </div>
        </div>
      </template>
    </q-table>
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
        status: ['Cancel', 'Approved', 'Rejected', 'On Reviewed', 'Awaiting Approval','Close'],
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
            field: row => row.kodePinjaman,
            sortable: true,
          }, {
            name: 'jumlahPinjaman',
            label: 'Jumlah Pinjaman',
            align: 'center',
            field: row => this.toIDR(row.jumlahPinjaman),
            sortable: true,
          }, {
            name: 'namaDebitur',
            label: 'Nama Debitur',
            align: 'center',
            field: row => `${row.user.userDetail.firstName} ${row.user.userDetail.lastName}`,
            sortable: true,
          },
          {
            name: 'tanggalPengajuan',
            label: 'Tanggal Pengajuan',
            align: 'center',
            field: (row) => {
              moment.lang('id')
              return moment(row.createdAt)
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
        pembayaranView: false
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
      getDataPinjaman() {
        this.$q.loading.show()
        this.$http.get('/api/getdatapengajuanapprove', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.data = res.data
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
      }
    },
    created() {
      this.getDataPinjaman()
    }
  }
</script>

<style scoped>

</style>
