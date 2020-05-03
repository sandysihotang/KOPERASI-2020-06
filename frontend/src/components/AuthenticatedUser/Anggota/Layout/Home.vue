<template>
  <div>
    <q-card>
      <center>
        {{nama}}
        <br>
        <h5>
          <q-badge color="primary" outline :label="`Total simpanan: ${toIDR(saldo)}`"/>
        </h5>
      </center>
    </q-card>
    <q-separator/>
    <q-card>
      <q-card-section>
        Tagihan
      </q-card-section>
      <q-separator/>
      <div v-if="!tagihan">
        <center>
          <div class="text-purple">
            <q-icon name="fa fa-smile" size="xl"/>
          </div>
          <div class="text-black">
            Yeay kamu tidak punya tagihan!
          </div>
        </center>
      </div>
      <div v-else>
        <q-intersection
          transition="flip-right"
          class="example-item"
        >
          <q-item clickable v-ripple>
            <q-item-section avatar>
              <q-avatar color="primary" text-color="white">
                <q-icon name="check"/>
              </q-avatar>
            </q-item-section>

            <q-item-section class="text-black">
              <q-item-label caption>Kode Pinjaman: #{{ kodePinjaman }}</q-item-label>
              <q-item-label class="text-caption">Pembayaran berikutnya:{{
                toIDR(jumlahPembayaranBerikutnya) }}
              </q-item-label>
              <q-item-label caption lines="1">
                <q-chip class="glossy" color="primary" text-color="white">{{
                  mom(tanggalPembayaranBerikutnya) }}
                </q-chip>
              </q-item-label>
            </q-item-section>
          </q-item>
        </q-intersection>
      </div>
    </q-card>
    <br>
    <q-separator/>
    <q-card>
      <q-card-section>
        Transaksi Terkini
      </q-card-section>
      <q-separator/>
      <q-card-section>
        <q-intersection
          v-for="a in transaksiTerkini"
          transition="flip-right"
          class="example-item"
          :key="a.id"
        >
          <q-item clickable v-ripple>
            <q-item-section avatar>
              <q-avatar color="primary" text-color="white">
                <q-icon name="check"/>
              </q-avatar>
            </q-item-section>

            <q-item-section class="text-black">
              <q-item-label caption>Kode Pinjaman: #{{ a.kodeTransaksi }}</q-item-label>
              <q-item-label class="text-caption">Total: {{ toIDR(a.jumlahTransaksi) }}
              </q-item-label>
              <q-item-label class="text-caption">Jenis: {{ (a.aktivasiSimpanan.jenisSimpanan === 1?
                'Simpanan Pokok': (a.aktivasiSimpanan.jenisSimpanan === 2 ? 'Simpanan Wajib' :
                'Simpanan Sukarela')) }}
              </q-item-label>
              <q-item-label caption lines="1">
                <q-chip class="glossy" color="primary" text-color="white">{{
                  mom(a.createdAt) }}
                </q-chip>
              </q-item-label>

              <q-item-label caption>Mode: {{ (a.jenisTransaksi === 1 ? 'Setor Dana' : 'Penarikan Dana') }}</q-item-label>
            </q-item-section>
          </q-item>
        </q-intersection>
      </q-card-section>
    </q-card>
  </div>
</template>
<script>
  import moment from 'moment'

  export default {
    data() {
      return {
        nama: '',
        tagihan: false,
        kodePinjaman: null,
        jumlahPembayaranBerikutnya: null,
        tanggalPembayaranBerikutnya: null,
        idPinjaman: null,
        saldo: null,
        transaksiTerkini: []
      }
    },
    methods: {
      mom(date) {
        moment.lang('id')
        return moment(date)
          .format('dddd, Do MMMM YYYY')
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
      getName() {
        this.$q.loading.show()
        this.$http.get('/api/getnameanggota', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.nama = res.data
            this.existTagihan()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      existTagihan() {
        this.$http.get('/api/existtagihan', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.tagihan = res.data
            if (res.data) {
              this.getData()
            }
            this.getSaldo()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      getSaldo() {
        this.$http.get('api/getsaldouser', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.saldo = res.data
            this.getTransaksi()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      getTransaksi() {
        this.$http.get('/api/gettransaksiterkini', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.transaksiTerkini = res.data
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      getData() {
        this.$http.get('/api/gettagihan', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.kodePinjaman = res.data.kodePinjaman
            this.idPinjaman = res.data.id
            this.getAngsuran()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      getAngsuran() {
        this.$http.get(`/api/getangsuranbasedidpinjaman/${this.idPinjaman}`, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.jumlahPembayaranBerikutnya = res.data.totalAngsuran
            this.tanggalPembayaranBerikutnya = res.data.tanggalJatuhTempo
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      }
    },
    created() {
      this.getName();
    }
  }
</script>
