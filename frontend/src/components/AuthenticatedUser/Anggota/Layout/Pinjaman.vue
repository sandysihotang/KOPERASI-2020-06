<template>
  <div>
    <div v-if="!showPeminjaman">
      <q-card>
        <q-tabs
          v-model="tab"
          dense
          class="bg-grey-3 text-grey-7"
          active-color="primary"
          indicator-color="purple"
          align="justify"
        >
          <q-tab name="mails" label="Berjalan"/>
          <q-tab name="alarms" label="Selesai"/>
        </q-tabs>

        <q-tab-panels v-model="tab" animated class="bg-primary text-white">
          <q-tab-panel name="mails">
            <center class="text-white">
              <q-icon name="fa fa-search" size="xl"/>
              <h6>Kamu belum mengajukan pinjaman</h6>
              <q-btn unelevated rounded color="purple" label="Ajukan Peminjaman" size="xs"
                     @click="showPeminjaman = true"/>
            </center>
          </q-tab-panel>

          <q-tab-panel name="alarms">
            <center class="text-white">
              <q-icon name="fa fa-search" size="xl"/>
              <h6>Kamu belum mengajukan pinjaman</h6>
              <q-btn unelevated rounded color="purple" label="Ajukan Peminjaman" size="xs"
                     @click="showPeminjaman = true"/>
            </center>
          </q-tab-panel>
        </q-tab-panels>
      </q-card>
    </div>
    <div v-else>
      <q-card>
        <div class="q-pa-md full-width">
          <div class="q-gutter-md">
            <q-input
              filled
              v-model="price"
              label="Total pinjaman"
              mask="Rp #.##"
              fill-mask="0"
              reverse-fill-mask
              unmasked-value
              input-class="text-right"
            />
            <q-input
              filled
              v-model="tenor"
              label="Tenor (Bulan)"
              mask="## Bln"
              fill-mask="0"
              reverse-fill-mask
              unmasked-value
              input-class="text-right"
              @input="handle"
            />
          </div>
        </div>
      </q-card>
      <q-space/>
      <q-card v-if="showHandle">
        <q-list bordered separator>
          <q-item>
            <q-item-section>Angsuran Pokok</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>Rp.{{ price/100 / tenor}}</q-item-section>
          </q-item>
          <q-item>
            <q-item-section>Bunga(%)</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>{{persentase}} %</q-item-section>
          </q-item>
          <q-item>
            <q-item-section>Bunga Angsuran</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>Rp.{{ price/100 * persentase / 100 }}</q-item-section>
          </q-item>
          <q-item>
            <q-item-section>Total Angsuran</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>Rp.{{ (price/100 / tenor) + (price/100 * persentase / 100) }}</q-item-section>
          </q-item>
          <q-item>
            <q-item-section>Total Bunga/Jasa</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>Rp.Rp.{{ price / 100 * persentase / 100 * tenor }}</q-item-section>
          </q-item>
          <q-item>
            <q-item-section>Total Pinjaman</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>Rp.{{ (price/100) + (price/100 * persentase / 100 * tenor) }}</q-item-section>
          </q-item>
        </q-list>
      </q-card>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        tab: 'mails',
        showPeminjaman: false,
        price: null,
        tenor: null,
        showHandle: false,
        persentase: null
      }
    },
    methods: {
      handle(s) {
        this.showHandle = s.length !== 0
      },
      getSettings() {
        this.$q.loading.show();
        this.$http.get('/api/getpengaturanpinjamanreqpinjaman', {
          headers: this.$auth.getHeader(),
        })
          .then((res) => {
            this.persentase = res.data.bungaPinjaman
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
            this.$swal({
              position: 'center',
              type: 'error',
              title: 'Terjadi Kesalhan, refresh (F5)',
              showConfirmButton: false,
              timer: 1500,
            });
          })
      }
    },
    created() {
      this.getSettings()
    }
  }
</script>

<style scoped>

</style>
