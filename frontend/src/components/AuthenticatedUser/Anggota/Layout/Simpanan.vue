<template>
  <div class="q-pa-md">
    <q-card>
      <q-card-section>
        <q-item clickable v-ripple>
          <q-item-section avatar>
            <q-avatar color="primary" text-color="white">
              <q-icon name="check"/>
            </q-avatar>
          </q-item-section>

          <q-item-section class="text-black">
            <q-item-label class="text-caption">Simpanan Pokok
            </q-item-label>
            <q-item-label caption lines="1">
              <q-chip class="glossy" color="primary" text-color="white">Saldo: {{ toIDR(parseInt(pokok))}}
              </q-chip>
            </q-item-label>
          </q-item-section>
        </q-item>
        <q-item clickable v-ripple>
          <q-item-section avatar>
            <q-avatar color="primary" text-color="white">
              <q-icon name="check"/>
            </q-avatar>
          </q-item-section>

          <q-item-section class="text-black">
            <q-item-label class="text-caption">Simpanan Wajib
            </q-item-label>
            <q-item-label caption lines="1">
              <q-chip class="glossy" color="primary" text-color="white">Saldo: {{ toIDR(parseInt(wajib))}}
              </q-chip>
            </q-item-label>
          </q-item-section>
        </q-item>
        <q-item clickable v-ripple>
          <q-item-section avatar>
            <q-avatar color="primary" text-color="white">
              <q-icon name="check"/>
            </q-avatar>
          </q-item-section>

          <q-item-section class="text-black">
            <q-item-label class="text-caption">Simpanan Sukarela
            </q-item-label>
            <q-item-label caption lines="1">
              <q-chip class="glossy" color="primary" text-color="white">Saldo: {{
                toIDR(parseInt(sukarela))}}
              </q-chip>
            </q-item-label>
          </q-item-section>
        </q-item>
      </q-card-section>
      <q-card-section>
        <q-btn color="primary" class="full-width" label="History Selengkapnya" to="/history"/>
      </q-card-section>
    </q-card>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        pokok: 0,
        wajib: 0,
        sukarela: 0
      }
    },
    methods: {
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
      getSimpanan() {
        this.$http.get('/api/getsimpananuser', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            const { data } = res
            for (let i = 0; i < 3; i++) {
              if (data[i].jenisSimpanan === 1) {
                this.pokok = data[i].totalSimpanan
              } else if (data[i].jenisSimpanan === 2) {
                this.wajib = data[i].totalSimpanan
              } else {
                this.sukarela = data[i].totalSimpanan
              }
            }
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
    },
    created() {
      this.$q.loading.show()
      this.getSimpanan()
    }
  }
</script>

<style scoped>

</style>
