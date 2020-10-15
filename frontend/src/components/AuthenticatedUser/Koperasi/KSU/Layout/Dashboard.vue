<template>
  <div class="q-pa-md">
    <div class="row justify-between">
      <div class="col-md-4">
        <div class="q-pa-md full-width">
          <q-card flat bordered class="full-width full-height">
            <q-card-section>
              <div class="text-caption">Laba, Pemasukan dan Pengeluaran {{ getDate() }}</div>
            </q-card-section>
            <q-separator/>
            <q-card-section>
              <pemasukan-simpanan/>
            </q-card-section>
          </q-card>
        </div>
      </div>
      <div class="col-md-4">
        <div class="q-pa-md full-width">
          <q-card flat bordered class="full-height full-width">
            <q-card-section>
              <div class="text-caption">Anggota</div>
            </q-card-section>

            <q-separator/>

            <q-card-section>
              <anggota/>
            </q-card-section>
          </q-card>
        </div>
      </div>
      <div class="col-md-4">
        <div class="q-pa-md full-width">
          <q-card flat bordered class="full-width full-height">
            <q-card-section>
              <div class="text-caption">Simpanan Anggota</div>
            </q-card-section>

            <q-separator/>

            <q-card-section>
              <simpanan/>
            </q-card-section>
          </q-card>
        </div>
      </div>
    </div>
    <div class="row q-mt-lg justify-between">
      <div class="col-md-4">
        <div class="q-pa-md full-width">
          <q-card flat bordered class="full-width">
            <q-card-section>
              <q-item v-ripple>
                <q-item-section avatar>
                  <q-avatar color="primary" text-color="white">
                    <q-icon name="settings"/>
                  </q-avatar>
                </q-item-section>

                <q-item-section class="text-black">
                  <q-item-label>{{ toIDR(parseInt(tersalur)) }}</q-item-label>
                  <q-item-label caption>
                    Pinjaman Tersalur
                  </q-item-label>
                </q-item-section>
              </q-item>
            </q-card-section>
          </q-card>
        </div>
      </div>
      <div class="col-md-4">
        <div class="q-pa-md full-width">
          <q-card flat bordered class="full-width">
            <q-card-section>
              <q-item v-ripple>
                <q-item-section avatar>
                  <q-avatar color="primary" text-color="white">
                    <q-icon name="check"/>
                  </q-avatar>
                </q-item-section>

                <q-item-section class="text-black">
                  <q-item-label>{{ toIDR(parseInt(terbayar)) }}</q-item-label>
                  <q-item-label caption>
                    Pinjaman Terbayar
                  </q-item-label>
                </q-item-section>
              </q-item>
            </q-card-section>
          </q-card>
        </div>
      </div>
      <div class="col-md-4">
        <div class="q-pa-md full-width">
          <q-card flat bordered class="full-width">
            <q-card-section>
              <q-item v-ripple>
                <q-item-section avatar>
                  <q-avatar color="primary" text-color="white">
                    <q-icon name="fa fa-ban"/>
                  </q-avatar>
                </q-item-section>

                <q-item-section class="text-black">
                  <q-item-label>{{ toIDR(parseInt(jatuhTempo)) }}</q-item-label>
                  <q-item-label caption>
                    Pinjaman Jatuh Tempo
                  </q-item-label>
                </q-item-section>
              </q-item>
            </q-card-section>
          </q-card>
        </div>
      </div>
    </div>
    <div class="row q-mt-lg justify-between" v-show="produk">
      <div class="col-md-4">
        <div class="q-pa-md full-width">
          <q-card flat bordered class="full-width">
            <q-card-section>
              <q-item v-ripple>
                <q-item-section avatar>
                  <q-avatar color="primary" text-color="white">
                    <q-icon name="inventory"/>
                  </q-avatar>
                </q-item-section>

                <q-item-section class="text-black">
                  <q-item-label>{{ totalProduk }}</q-item-label>
                  <q-item-label caption>
                    Total Produk
                  </q-item-label>
                </q-item-section>
              </q-item>
            </q-card-section>
          </q-card>
        </div>
      </div>
      <div class="col-md-4">
        <div class="q-pa-md full-width">
          <q-card flat bordered class="full-width">
            <q-card-section>
              <q-item v-ripple>
                <q-item-section avatar>
                  <q-avatar color="primary" text-color="white">
                    <q-icon name="fa fa-shopping-bag"/>
                  </q-avatar>
                </q-item-section>

                <q-item-section class="text-black">
                  <q-item-label>{{ totalProdukterjual }}</q-item-label>
                  <q-item-label caption>
                    Produk Terjual
                  </q-item-label>
                </q-item-section>
              </q-item>
            </q-card-section>
          </q-card>
        </div>
      </div>
      <div class="col-md-4">
        <div class="q-pa-md full-width">
          <q-card flat bordered class="full-width">
            <q-card-section>
              <q-item v-ripple>
                <q-item-section avatar>
                  <q-avatar color="primary" text-color="white">
                    <q-icon name="fa fa-coins"/>
                  </q-avatar>
                </q-item-section>

                <q-item-section class="text-black">
                  <q-item-label>{{ toIDR(parseInt(totalHargaProdukterjual)) }}</q-item-label>
                  <q-item-label caption>
                    Total Penjualan
                  </q-item-label>
                </q-item-section>
              </q-item>
            </q-card-section>
          </q-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import moment from 'moment'
  import Anggota from './Js/Anggota'
  import Simpanan from './Js/Simpanan';
  import PemasukanSimpanan from './Js/PemasukanPengeluaranLaba'

  export default {
    components: {
      Anggota,
      Simpanan,
      PemasukanSimpanan
    },
    data() {
      return {
        jatuhTempo: 0,
        tersalur: 0,
        terbayar: 0,
        produk: false,
        totalProduk: 0,
        totalProdukterjual: 0,
        totalHargaProdukterjual: 0,
      }
    },
    methods: {
      getDate() {
        moment.lang('id')
        return moment(new Date())
          .format('MMMM YYYY')
      },
      isProduk() {
        if (parseInt(localStorage.getItem('jenisKoperasi')) === 1) {
          this.produk = true
          this.$http.get('/api/produkkoperasi', {
            headers: this.$auth.getHeader()
          })
            .then((res) => {
              const { data } = res
              this.totalProduk = data.totalproduk
              this.totalProdukterjual = data.totalprodukterjual
              this.totalHargaProdukterjual = data.totaluangprodukterjual
            })
        }
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
      getDataTersalur() {
        this.$http.get('/api/getpinjamanfordashboard', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            const { data } = res
            this.tersalur = data.pinjamantersalur
            this.terbayar = data.pinjamanterbayar
            this.jatuhTempo = data.pinjamanJatuhTempo
          })
      }
    },
    created() {
      this.isProduk()
      this.getDataTersalur()
    }
  };
</script>

<style scoped>

</style>
