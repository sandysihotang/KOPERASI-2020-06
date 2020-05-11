<template>
  <div>
    <q-page-container>
      <q-card>
        <q-separator/>
        <q-tab-panels v-model="tab" animated style="height: 100%; width: 100%;">
          <q-tab-panel name="mails">
            <Home/>
          </q-tab-panel>

          <q-tab-panel name="alarms">
            <Simpanan/>
          </q-tab-panel>

          <q-tab-panel name="movies">
            <Pinjaman/>
          </q-tab-panel>
        </q-tab-panels>
      </q-card>
    </q-page-container>

    <q-footer class="bg-white">
      <q-tabs
        v-model="tab"
        dense
        class="text-grey"
        active-color="primary"
        indicator-color="primary"
        align="justify"
        narrow-indicator
      >
        <q-tab name="mails" icon="home" label="Home"/>
        <q-tab name="alarms" icon="alarm" label="Simpanan"/>
        <q-tab name="movies" icon="movie" label="Pinjaman"/>
      </q-tabs>
    </q-footer>
  </div>
</template>

<script>
  import Home from './Home.vue';
  import Pinjaman from './Pinjaman.vue'
  import Simpanan from './Simpanan.vue'

  export default {
    components: {
      Home,
      Pinjaman,
      Simpanan
    },
    data() {
      return {
        tab: 'mails',
      }
    }
  }
</script>

<style scoped>

</style>
