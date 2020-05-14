<template>
  <q-layout view="hhh LpR fFf">

    <q-header elevated class="bg-primary text-white">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="left = !left"/>
        <q-avatar v-if="image">
          <img :src="dataUrl(image)" alt="">
        </q-avatar>
        <q-avatar v-else icon="person"/>
        <q-toolbar-title>
          {{ title }}
        </q-toolbar-title>
        <q-btn flat round dense icon="fa fa-sign-out-alt" @click="logOut"/>
      </q-toolbar>
    </q-header>

    <q-drawer show-if-above v-model="left" side="left" bordered>
      <q-scroll-area class="fit">
        <q-list padding>
          <q-item clickable v-ripple :to="'/dashboardkoperasiksu'">
            <q-item-section avatar>
              <q-icon color="primary" name="fa fa-chart-bar"/>
            </q-item-section>
            <q-item-section>Dashboard</q-item-section>
          </q-item>
          <q-item clickable v-ripple :to="'/anggotakoperasiksu'">
            <q-item-section avatar>
              <q-icon color="primary" name="person"/>
            </q-item-section>
            <q-item-section>Anggota Koperasi</q-item-section>
          </q-item>
          <q-expansion-item
            expand-separator
            icon="fas fa-piggy-bank"
            label="Pinjaman">
            <q-list padding>
              <q-item clickable v-ripple :to="'/pengajuanpinjamanksu'">
                <q-item-section>Pengajuan Pinjaman</q-item-section>
              </q-item>
              <q-item clickable v-ripple :to="'/transaksipinjamanksu'">
                <q-item-section>Transaksi Pinjaman</q-item-section>
              </q-item>
            </q-list>
          </q-expansion-item>
          <q-expansion-item
            expand-separator
            icon="inventory"
            label="Produk">
            <q-list padding>
              <q-item clickable v-ripple :to="'/daftarproduk'">
                <q-item-section>Daftar Produk</q-item-section>
              </q-item>
              <q-item clickable v-ripple :to="'/daftarprodukmasuk'">
                <q-item-section>Produk Masuk</q-item-section>
              </q-item>
              <q-item clickable v-ripple :to="'/pengkasiran'">
                <q-item-section>Pengkasiran</q-item-section>
              </q-item>
              <q-item clickable v-ripple :to="'/produkterjual'">
                <q-item-section>Produk Terjual</q-item-section>
              </q-item>
            </q-list>
          </q-expansion-item>
          <q-expansion-item
            expand-separator
            icon="money"
            label="Simpanan">
            <q-list padding>
              <q-item clickable v-ripple :to="'/transaksisimpananksu'">
                <q-item-section>Transaksi Simpanan</q-item-section>
              </q-item>
              <q-item clickable v-ripple :to="'/aktivasisimpananksu'">
                <q-item-section>Aktivasi Simpanan</q-item-section>
              </q-item>
            </q-list>
          </q-expansion-item>
          <q-expansion-item
            expand-separator
            icon="settings"
            label="Pengaturan">
            <q-list padding>
              <q-item clickable v-ripple :to="'/pengaturanpendaftarananggotaksu'">
                <q-item-section>Form Pendaftaran Anggota</q-item-section>
              </q-item>
              <q-item clickable v-ripple :to="'/pengaturanpinjamanksu'">
                <q-item-section>Pinjaman</q-item-section>
              </q-item>
              <q-item clickable v-ripple :to="'/pengaturansimpananksu'">
                <q-item-section>Simpanan</q-item-section>
              </q-item>
            </q-list>
          </q-expansion-item>
          <q-item clickable v-ripple :to="'/laporanksu'">
            <q-item-section avatar>
              <q-icon color="primary" name="report"/>
            </q-item-section>
            <q-item-section>Laporan</q-item-section>
          </q-item>
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
        title: 'Selamat datang Koperasi ',
        image: null
      };
    },
    methods: {
      dataUrl(i) {
        return `data:image/jpeg;base64,${i}`
      },
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
            this.title = `${this.title} ${res.data.nama}`
            this.image = res.data.logoKoperasi
          })
      }
    },
    created() {
      this.getStateKoperasi()
    }
  };
</script>
