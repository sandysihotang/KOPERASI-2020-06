<template>
  <div>
    <q-layout view="hHh lpR fFf">

      <q-header elevated class="bg-primary text-white">
        <q-toolbar>
          <q-toolbar-title>
            {{ koperasi }}
          </q-toolbar-title>
          <q-btn flat round dense icon="fa fa-sign-out-alt" @click="logOut"/>
        </q-toolbar>
      </q-header>

      <q-page-container>
        <q-card>
          <q-separator/>
          <q-tab-panels v-model="tab" animated style="height: 100%; width: 100%;">
            <q-tab-panel name="mails">
              <Home/>
            </q-tab-panel>

            <q-tab-panel name="alarms">
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
    </q-layout>
  </div>
</template>

<script>
  import Home from './Layout/Home.vue';
  import Pinjaman from './Layout/Pinjaman.vue'

  export default {
    components: {
      Home,
      Pinjaman
    },
    data() {
      return {
        tab: 'mails',
        koperasi: 'Koperasi '
      }
    },
    methods: {
      logOut() {
        window.localStorage.clear();
        window.location.href = '/';
      },
      getNameKoperasi() {
        this.$q.loading.show();
        this.$http.get('/api/getnamekoperasi', {
          headers: this.$auth.getHeader(),
        })
          .then((res) => {
            this.koperasi = `${this.koperasi} ${res.data}`;
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      }
    },
    created() {
      this.getNameKoperasi();
    }
  }
</script>

<style scoped>

</style>
