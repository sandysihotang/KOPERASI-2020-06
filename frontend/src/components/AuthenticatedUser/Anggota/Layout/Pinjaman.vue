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
            <div v-show="!adaPinjaman">
              <center class="text-white">
                <q-icon name="fa fa-search" size="xl"/>
                <h6>Kamu belum mengajukan pinjaman</h6>
                <q-btn unelevated rounded color="purple" label="Ajukan Peminjaman" size="xs"
                       @click="showPeminjaman = true"/>
              </center>
            </div>
            <div v-show="adaPinjaman">
              <div class="q-pa-md flex justify-center">
                <div class="full-width bg-white">
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
                        <q-item-label caption>Kode Pinjaman: {{ dataPinjaman.kodePinjaman }}</q-item-label>
                        <q-item-label class="text-caption">Total pengajuan:{{
                          toIDR(dataPinjaman.jumlahPinjaman) }}
                        </q-item-label>
                        <q-item-label caption lines="1">
                          <q-chip class="glossy" color="primary" text-color="white">{{
                            status[dataPinjaman.status-1] }}
                          </q-chip>
                        </q-item-label>
                      </q-item-section>
                    </q-item>
                  </q-intersection>
                </div>
              </div>
            </div>
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
              mask="Rp #,###,###,###.##"
              fill-mask="0"
              reverse-fill-mask
              unmasked-value
              input-class="text-right"
            />
            <q-input
              filled
              v-model="jaminan"
              label="Jaminan"
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
            <q-item-section>{{ toIDR((price / 100) / tenor)}}</q-item-section>
          </q-item>
          <q-item>
            <q-item-section>Bunga(%)</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>{{persentase}} %</q-item-section>
          </q-item>
          <q-item>
            <q-item-section>Bunga Angsuran</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>{{ toIDR((price/100) * persentase / 100) }}</q-item-section>
          </q-item>
          <q-item>
            <q-item-section>Total Angsuran</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>{{ toIDR(((price/100) / tenor) + ((price/100) * persentase / 100)) }}
            </q-item-section>
          </q-item>
          <q-item>
            <q-item-section>Total Bunga/Jasa</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>{{ toIDR((price / 100) * persentase / 100 * tenor) }}</q-item-section>
          </q-item>
          <q-item>
            <q-item-section>Total Pinjaman</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>{{ toIDR((price/100) + ((price/100) * persentase / 100 * tenor)) }}
            </q-item-section>
          </q-item>
          <q-btn color="primary" class="full-width" @click="ajukanPinjaman">Ajukan Pinjaman</q-btn>
        </q-list>
      </q-card>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        status: ['', '', '', '', 'Dalam Persetujuan'],
        dataPinjaman: [],
        id: null,
        tab: 'mails',
        showPeminjaman: false,
        price: null,
        tenor: null,
        showHandle: false,
        persentase: null,
        jaminan: null,
        adaPinjaman: false,
      }
    },
    methods: {
      handle(s) {
        this.showHandle = s.length !== 0
      },
      ajukanPinjaman() {
        if (parseInt(this.price) === 0 && parseInt(this.tenor) && this.jaminan.length === 0) {
          this.$swal({
            position: 'center',
            type: 'error',
            title: 'Silahkan isi semua field Peminjaman!',
            showConfirmButton: false,
            timer: 1500,
          });
          return
        }
        this.$q.loading.show()
        this.$http.post('/api/requestpeminjaman', {
          id: this.id,
          jaminan: this.jaminan,
          tenor: this.tenor,
          price: this.price
        }, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.showPeminjaman = false;
            this.getAdaPinjaman()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      getAdaPinjaman() {
        this.$q.loading.show()
        this.$http.get('/api/getadapinjaman', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.adaPinjaman = res.data
            if (this.adaPinjaman) this.getDataProcessing()
            else this.getSettings()
          })
          .catch(() => {
            this.$swal({
              position: 'center',
              type: 'error',
              title: 'Ada kesalahan pada sistem, refresh (F5)',
              showConfirmButton: false,
              timer: 1500,
            });
            this.$q.loading.hide()
          })
      },
      getDataProcessing() {
        this.$http.get('/api/getdatapinjaman', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.dataPinjaman = res.data
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$swal({
              position: 'center',
              type: 'error',
              title: 'Ada kesalahan pada sistem, refresh (F5)',
              showConfirmButton: false,
              timer: 1500,
            });
            this.$q.loading.hide()
          })
      },
      getSettings() {
        this.$http.get('/api/getpengaturanpinjamanreqpinjaman', {
          headers: this.$auth.getHeader(),
        })
          .then((res) => {
            this.persentase = res.data.bungaPinjaman
            this.id = res.data.id
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
      this.getAdaPinjaman()
    }
  }
</script>

<style scoped>

</style>
