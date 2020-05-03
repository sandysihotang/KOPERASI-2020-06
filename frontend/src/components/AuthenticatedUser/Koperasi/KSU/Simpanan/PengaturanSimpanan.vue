<template>
  <div class="q-pa-md">
    <q-stepper
      v-model="step"
      ref="stepper"
      alternative-labels
      color="primary"
      animated
    >
      <q-step
        :name="1"
        title="Simpanan Pokok"
        icon="settings"
        :done="step > 1"
      >
        <q-form class="q-px-sm q-pt-xl">
          <q-input square clearable type="text" v-model="pokok" label="Minimal Jumlah Setoran"
                   :disable="Aturan"
                   mask="Rp #,###,###,###"
                   fill-mask="0"
                   reverse-fill-mask
                   unmasked-value
                   input-class="text-right">
            <template v-slot:prepend>
              <q-icon name="money"/>
            </template>
          </q-input>
        </q-form>
      </q-step>

      <q-step
        :name="2"
        title="Simpanan Wajib"
        icon="create_new_folder"
        :done="step > 2"
      >
        <q-form class="q-px-sm q-pt-xl">
          <q-input square clearable type="text" v-model="wajib" label="Minimal Jumlah Setoran"
                   :disable="Aturan"
                   mask="Rp #,###,###,###"
                   fill-mask="0"
                   reverse-fill-mask
                   unmasked-value
                   input-class="text-right">
            <template v-slot:prepend>
              <q-icon name="money"/>
            </template>
          </q-input>
        </q-form>
      </q-step>
      <q-step
        :name="3"
        title="Simpanan Sukarela"
        icon="create_new_folder"
        :done="step > 3"
      >
        <q-form class="q-px-sm q-pt-xl">
          <q-input square clearable type="text" v-model="sukarela" label="Minimal Jumlah Setoran"
                   :disable="Aturan"
                   mask="Rp #,###,###,###"
                   fill-mask="0"
                   reverse-fill-mask
                   unmasked-value
                   input-class="text-right">
            <template v-slot:prepend>
              <q-icon name="money"/>
            </template>
          </q-input>
        </q-form>
      </q-step>
      <template v-slot:navigation>
        <q-stepper-navigation>
          <q-stepper-navigation>
            <q-btn v-if="step===3" unelevated @click="saveAturan" :disable="Aturan"
                   size="lg"
                   color="purple-4" class="full-width text-white" label="Get Started">
            </q-btn>
            <q-btn v-else @click="$refs.stepper.next()" unelevated
                   size="lg"
                   color="purple-4" class="full-width text-white" label="Continue">
            </q-btn>
            <q-btn v-if="step > 1" flat color="primary"
                   @click="$refs.stepper.previous()" label="Back" class="q-ml-sm"/>
          </q-stepper-navigation>
        </q-stepper-navigation>
      </template>
    </q-stepper>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        step: 1,
        Aturan: false,
        pokok: null,
        wajib: null,
        sukarela: null
      }
    },
    methods: {
      existAturan() {
        this.$http.get('/api/existaturansimpanan', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.Aturan = res.data
            if (!this.Aturan) this.$q.loading.hide()
            else this.getAturan()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      getAturan() {
        this.$http.get('/api/getaturansimpanan', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            const { data } = res;
            for (let i = 0; i < 3; i++) {
              if (data[i].jenisSimpanan === 1) {
                this.pokok = data[i].minimalSimpanan
              } else if (data[i].jenisSimpanan === 2) {
                this.wajib = data[i].minimalSimpanan
              } else if (data[i].jenisSimpanan === 3) {
                this.sukarela = data[i].minimalSimpanan
              }
            }
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      saveAturan() {
        if (this.pokok === null || this.sukarela === null || this.wajib === null) {
          this.$swal({
            position: 'center',
            type: 'error',
            title: 'Isi Semua field',
            showConfirmButton: false,
            timer: 1500,
          });
          return
        }
        this.$q.loading.show()
        this.$http.post('/api/saveaturansimpanan', {
          pokok: this.pokok,
          sukarela: this.sukarela,
          wajib: this.wajib
        }, {
          headers: this.$auth.getHeader()
        })
          .then(() => {
            this.existAturan()
            this.$swal({
              position: 'center',
              type: 'success',
              title: 'Berhasil menerapkan aturan Simpanan',
              showConfirmButton: false,
              timer: 1500,
            });
          })
          .catch(() => {
            this.$swal({
              position: 'center',
              type: 'error',
              title: 'Gagal menerapkan aturan Simpanan, refresh(F5)',
              showConfirmButton: false,
              timer: 1500,
            });
            this.$q.loading.hide()
          })
      }
    },
    created() {
      this.$q.loading.show()
      this.existAturan();
    }
  }
</script>

<style scoped>

</style>
