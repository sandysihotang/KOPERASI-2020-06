<template>
  <q-layout view="hhh LpR fFf">

    <q-header elevated class="bg-primary text-white">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="left = !left"/>
        <q-toolbar-title>
          {{ title }}
        </q-toolbar-title>
        <q-btn flat round dense icon="fa fa-sign-out-alt" @click="logOut"/>
      </q-toolbar>
    </q-header>

    <q-drawer show-if-above v-model="left" side="left" bordered>
      <q-scroll-area class="fit">
        <q-list padding>
          <q-item clickable v-ripple :to="'/dashboardkoperasi'">
            <q-item-section avatar>
              <q-icon color="primary" name="fa fa-chart-bar"/>
            </q-item-section>
            <q-item-section>Dashboard</q-item-section>
          </q-item>
          <q-item clickable v-ripple :to="'/anggotakoperasi'">
            <q-item-section avatar>
              <q-icon color="primary" name="person"/>
            </q-item-section>
            <q-item-section>Anggota Koperasi</q-item-section>
          </q-item>
          <q-expansion-item
            expand-separator
            icon="settings"
            label="Pengaturan">
            <q-list padding>
              <q-item clickable v-ripple :to="'/pengaturanpendaftarananggota'">
                <q-item-section>Pengaturan Form Pendaftaran Anggota</q-item-section>
              </q-item>
              <q-item clickable v-ripple :to="'/pengaturanpinjaman'">
                <q-item-section>Pengaturan Pinjaman</q-item-section>
              </q-item>
            </q-list>
          </q-expansion-item>
        </q-list>
      </q-scroll-area>
    </q-drawer>
    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<script>
  export default {
    data() {
      return {
        left: false,
        title: 'Selamat datang Koperasi '
      };
    },
    methods: {
      logOut() {
        window.localStorage.clear();
        window.location.href = '/';
      },
      getStateKoperasi() {
        this.$http.get('/api/getstatekoperasi',
          {
            headers: this.$auth.getHeader(),
          })
          .then((res) => {
            this.title = `${this.title} ${res.data}`
          })
      }
    },
    created() {
      this.getStateKoperasi()
    }
  };
</script>
