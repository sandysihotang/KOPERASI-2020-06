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
            <q-item v-ripple>
              <q-item-section avatar>
                <q-avatar color="primary" text-color="white">
                  <q-icon name="check"/>
                </q-avatar>
              </q-item-section>

              <q-item-section class="text-black">
                <q-item-label></q-item-label>
                <q-item-label caption>
                  Total Pinjaman Terbayar
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-card-section>
        </q-card>
      </div>
      <div class="col-md-4">
        <q-card flat bordered class="full-width">
          <q-card-section>
            <q-item v-ripple>
              <q-item-section avatar>
                <q-avatar color="primary" text-color="white">
                  <q-icon name="fa fa-ban"/>
                </q-avatar>
              </q-item-section>

              <q-item-section class="text-black">
                <q-item-label></q-item-label>
                <q-item-label caption>
                  Total Pinjaman Jatuh Tempo
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
            <q-item v-ripple>
              <q-item-section avatar>
                <q-avatar color="primary" text-color="white">
                  <q-icon name="inventory"/>
                </q-avatar>
              </q-item-section>

              <q-item-section class="text-black">
                <q-item-label></q-item-label>
                <q-item-label caption>
                  Total Produk
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-card-section>
        </q-card>
      </div>
      <div class="col-md-4">
        <q-card flat bordered class="full-width">
          <q-card-section>
            <q-item v-ripple>
              <q-item-section avatar>
                <q-avatar color="primary" text-color="white">
                  <q-icon name="fa fa-shopping-bag"/>
                </q-avatar>
              </q-item-section>

              <q-item-section class="text-black">
                <q-item-label></q-item-label>
                <q-item-label caption>
                  Produk Terjual
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-card-section>
        </q-card>
      </div>
      <div class="col-md-4">
        <q-card flat bordered class="full-width">
          <q-card-section>
            <q-item v-ripple>
              <q-item-section avatar>
                <q-avatar color="primary" text-color="white">
                  <q-icon name="fa fa-coins"/>
                </q-avatar>
              </q-item-section>

              <q-item-section class="text-black">
                <q-item-label></q-item-label>
                <q-item-label caption>
                  Total Penjualan
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-card-section>
        </q-card>
      </div>
    </div>
    <q-dialog v-model="labadanpemasukan" persistent full-width full-height transition-show="scale"
              transition-hide="scale">
      <laba-pemasukan/>
    </q-dialog>

  </div>
</template>

<script>
  import LabaPemasukan from './Laporan/LabaDanPemasukan.vue'

  export default {
    components: {
      LabaPemasukan
    },
    data() {
      return {
        labadanpemasukan: false
      }
    }
  }
</script>

<style scoped>

</style>
