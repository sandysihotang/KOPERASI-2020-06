<template>
  <div>
    <q-card class="full-width full-height">
      <q-card-section class="full-width absolute-top-left">
        <div class="row">
          <div class="col">
            <div class="row">
              <div class="col">
                <q-input filled v-model="dateFrom" mask="date" label="Tanggal Dari"
                         :rules="['date']" style="max-width: 90%;">
                  <template v-slot:append>
                    <q-icon name="event" class="cursor-pointer">
                      <q-popup-proxy ref="qDateProxy" transition-show="scale"
                                     transition-hide="scale">
                        <q-date v-model="dateFrom" @input="() => $refs.qDateProxy.hide()"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
              </div>
              <div class="col">
                <q-input filled v-model="dateTo" mask="date" label="Tanggal Sampai"
                         :rules="['date']" style="max-width: 90%;">
                  <template v-slot:append>
                    <q-icon name="event" class="cursor-pointer">
                      <q-popup-proxy ref="qDateProxy" transition-show="scale"
                                     transition-hide="scale">
                        <q-date v-model="dateTo" @input="() => $refs.qDateProxy.hide()"/>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
              </div>
              <div class="col">
                <q-btn color="primary" label="Tampilkan" icon="fa fa-file" size="xm"
                       @click="showLaporan"/>
              </div>
            </div>
          </div>
          <div class="col"></div>
        </div>
      </q-card-section>
      <q-separator/>
      <q-card-section class="justify-center">

      </q-card-section>
      <q-card-actions align="right" class="bg-white absolute-bottom-right text-teal">
        <q-btn label="Close" color="green" v-close-popup/>
      </q-card-actions>
    </q-card>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        dateFrom: null,
        dateTo: null
      }
    },
    methods: {
      showLaporan() {
        if (this.dateFrom === null || this.dateTo === null) {
          this.$q.notify({
            type: 'negative',
            message: `Silahkan masukkan tanggal`
          })
          return
        }
        this.$q.loading.show()
        this.$http.post('/api/getlaporanpemasukandanlaba', {
          dateFrom: this.dateFrom,
          dateTo: this.dateTo
        }, {
          headers: this.$auth.getHeader()
        })
      }
    }
  }
</script>

<style scoped>

</style>
