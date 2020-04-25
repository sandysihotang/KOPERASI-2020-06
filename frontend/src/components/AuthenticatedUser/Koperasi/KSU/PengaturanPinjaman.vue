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
        title="Pengaturan Pinjaman"
        icon="settings"
        :done="step > 1"
      >
        <q-input
          square clearable
          filled
          v-model="form.bungaPinjaman"
          label="Bunga Pinjaman"
          mask="#,## %"
          fill-mask="0"
          reverse-fill-mask
          unmasked-value
          input-class="text-right"
        />

        <q-input
          square clearable
          filled
          v-model="form.minTenor"
          label="Min Tenor"
          input-class="text-right"
          mask="# bln"
          fill-mask="0"
          reverse-fill-mask
          unmasked-value
        />

        <q-input
          square clearable
          filled
          v-model="form.maxTenor"
          label="Max Tenor"
          input-class="text-right"
          mask="# bln"
          fill-mask="0"
          reverse-fill-mask
          unmasked-value
        />
      </q-step>

      <q-step
        :name="2"
        title="Pengaturan Keterlambatan"
        icon="create_new_folder"
        :done="step > 2"
      >
        <q-input
          square clearable
          filled
          v-model="form.ambangBatasDenda"
          label="Ambang Batas Denda"
          input-class="text-right"
          mask="# hri"
          fill-mask="0"
          reverse-fill-mask
          unmasked-value
        />

        <q-input
          v-model="form.persentaseDenda"
          label="Persentase Denda"
          square clearable
          filled
          mask="#,## %"
          fill-mask="0"
          reverse-fill-mask
          unmasked-value
          input-class="text-right"
        />
      </q-step>
      <template v-slot:navigation>
        <q-stepper-navigation>
          <q-stepper-navigation>
            <q-btn v-if="step===2" @click="save" unelevated
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
        form: {
          bungaPinjaman: null,
          minTenor: null,
          maxTenor: null,
          ambangBatasDenda: null,
          persentaseDenda: null
        }
      }
    },
    methods: {
      save() {
        this.$q.loading.show();
        this.$http.post('/api/savepengaturanpinjaman', this.form, {
          headers: this.$auth.getHeader(),
        })
          .then((res) => {
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      getData() {
        this.$q.loading.show();
        this.$http.get('/api/getpengaturanpinjaman', {
          headers: this.$auth.getHeader(),
        })
          .then((res) => {
            if (res.data !== null) {
              const { data } = res;
              this.form.bungaPinjaman = data.bungaPinjaman * 100;
              this.form.minTenor = data.minTenor;
              this.form.maxTenor = data.maxTenor;
              this.form.ambangBatasDenda = data.ambangBatasDenda;
              this.form.persentaseDenda = data.persentaseDenda * 100
            }
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      }
    },
    created() {
      this.getData()
    }
  }
</script>

<style scoped>

</style>
