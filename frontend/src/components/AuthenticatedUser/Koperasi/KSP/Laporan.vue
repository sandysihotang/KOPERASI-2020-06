<template>
  <div class="q-pa-md">
    <div class="row q-mt-lg justify-center">
      <div class="col-md-4">
        <q-card flat bordered class="full-width">
          <q-card-section>
            <q-item v-ripple clickable @click="labadanpemasukan = true">
              <q-item-section avatar>
                <q-avatar color="primary" text-color="white">
                  <q-icon name="fa fa-file"/>
                </q-avatar>
              </q-item-section>

              <q-item-section class="text-black">
                <q-item-label></q-item-label>
                <q-item-label caption>
                  Laba Dan Pemasukan
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-card-section>
        </q-card>
      </div>
      <div class="col-md-4">
        <q-card flat bordered class="full-width">
          <q-card-section>
            <q-item v-ripple clickable @click="simpanan = true">
              <q-item-section avatar>
                <q-avatar color="primary" text-color="white">
                  <q-icon name="fa fa-coins"/>
                </q-avatar>
              </q-item-section>

              <q-item-section class="text-black">
                <q-item-label></q-item-label>
                <q-item-label caption>
                  Rekapitulasi Simpanan
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-card-section>
        </q-card>
      </div>
      <div class="col-md-4">
        <q-card flat bordered class="full-width">
          <q-card-section>
            <q-item v-ripple clickable @click="transaksiSimpanan = true">
              <q-item-section avatar>
                <q-avatar color="primary" text-color="white">
                  <q-icon name="fas fa-exchange-alt"/>
                </q-avatar>
              </q-item-section>

              <q-item-section class="text-black">
                <q-item-label></q-item-label>
                <q-item-label caption>
                  Transaksi Simpanan
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-card-section>
        </q-card>
      </div>
    </div>
    <div class="row q-mt-lg justify-center">
      <div class="col-md-4">
        <q-card flat bordered class="full-width">
          <q-card-section>
            <q-item v-ripple clickable @click="pinjaman = true">
              <q-item-section avatar>
                <q-avatar color="primary" text-color="white">
                  <q-icon name="fas fa-money-bill-alt"/>
                </q-avatar>
              </q-item-section>

              <q-item-section class="text-black">
                <q-item-label></q-item-label>
                <q-item-label caption>
                  Pinjaman
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-card-section>
        </q-card>
      </div>
      <div class="col-md-4">
        <q-card flat bordered class="full-width">
          <q-card-section>
            <q-item v-ripple clickable @click="anggota = true">
              <q-item-section avatar>
                <q-avatar color="primary" text-color="white">
                  <q-icon name="groups"/>
                </q-avatar>
              </q-item-section>

              <q-item-section class="text-black">
                <q-item-label></q-item-label>
                <q-item-label caption>
                  Anggota Koperasi
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-card-section>
        </q-card>
      </div>
      <div class="col-md-4">
        <q-card flat bordered class="full-width">
          <q-card-section>
            <q-item v-ripple clickable @click="produk = true">
              <q-item-section avatar>
                <q-avatar color="primary" text-color="white">
                  <q-icon name="fa fa-boxes"/>
                </q-avatar>
              </q-item-section>

              <q-item-section class="text-black">
                <q-item-label></q-item-label>
                <q-item-label caption>
                  Produk Koperasi
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-card-section>
        </q-card>
      </div>
    </div>
    <div class="row q-mt-lg justify-center">
      <div class="col-md-4">
        <q-card flat bordered class="full-width">
          <q-card-section>
            <q-item v-ripple clickable @click="pemasukan = true">
              <q-item-section avatar>
                <q-avatar color="primary" text-color="white">
                  <q-icon name="fas fa-arrow-down"/>
                </q-avatar>
              </q-item-section>

              <q-item-section class="text-black">
                <q-item-label></q-item-label>
                <q-item-label caption>
                  Pemasukan Produk
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-card-section>
        </q-card>
      </div>
      <div class="col-md-4">
        <q-card flat bordered class="full-width">
          <q-card-section>
            <q-item v-ripple clickable @click="penjualan = true">
              <q-item-section avatar>
                <q-avatar color="primary" text-color="white">
                  <q-icon name="fa fa-arrow-up"/>
                </q-avatar>
              </q-item-section>

              <q-item-section class="text-black">
                <q-item-label></q-item-label>
                <q-item-label caption>
                  Penjualan Produk
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-card-section>
        </q-card>
      </div>
      <div class="col-md-4">
      </div>
    </div>
    <q-dialog v-model="labadanpemasukan" full-width full-height transition-show="rotate"
              transition-hide="rotate">
      <laba-pemasukan/>
    </q-dialog>
    <q-dialog v-model="simpanan" full-width full-height transition-show="rotate"
              transition-hide="rotate">
      <Simpanan/>
    </q-dialog>
    <q-dialog v-model="transaksiSimpanan" full-width full-height transition-show="rotate"
              transition-hide="rotate">
      <transaksi-simpanan/>
    </q-dialog>
    <q-dialog v-model="pinjaman" full-width full-height transition-show="rotate"
              transition-hide="rotate">
      <pinjaman/>
    </q-dialog>
    <q-dialog v-model="anggota" full-width full-height transition-show="rotate"
              transition-hide="rotate">
      <anggota/>
    </q-dialog>
    <q-dialog v-model="produk" full-width full-height transition-show="rotate"
              transition-hide="rotate">
      <produk/>
    </q-dialog>
    <q-dialog v-model="pemasukan" full-width full-height transition-show="rotate"
              transition-hide="rotate">
      <pemasukan-produk/>
    </q-dialog>
    <q-dialog v-model="penjualan" full-width full-height transition-show="rotate"
              transition-hide="rotate">
      <penjualan-produk/>
    </q-dialog>
  </div>
</template>

<script>
  import LabaPemasukan from './Laporan/LabaDanPemasukan.vue'
  import Simpanan from './Laporan/Simpanan.vue'
  import TransaksiSimpanan from './Laporan/TransaksiSimpanan.vue'
  import Pinjaman from './Laporan/Pinjaman.vue'
  import Anggota from './Laporan/Anggota.vue'
  import Produk from './Laporan/Produk.vue'
  import PemasukanProduk from './Laporan/PemasukanProduk.vue'
  import PenjualanProduk from './Laporan/PenjualanProduk.vue'

  export default {
    components: {
      LabaPemasukan,
      Simpanan,
      TransaksiSimpanan,
      Pinjaman,
      Anggota,
      Produk,
      PemasukanProduk,
      PenjualanProduk
    },
    data() {
      return {
        penjualan: false,
        pemasukan: false,
        produk: false,
        anggota: false,
        pinjaman: false,
        labadanpemasukan: false,
        simpanan: false,
        transaksiSimpanan: false
      }
    }
  }
</script>

<style scoped>

</style>
