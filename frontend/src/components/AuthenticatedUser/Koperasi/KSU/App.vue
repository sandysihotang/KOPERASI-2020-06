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
        <q-btn dense color="primary" round icon="info" class="q-ml-md" @click="notif">
          <q-badge color="red" floating v-if="existNotif">{{ totalNotif }}</q-badge>
        </q-btn>
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
            icon="fas fa-piggy-bank"
            label="Pinjaman">
            <q-list padding>
              <q-item clickable v-ripple :to="'/pengajuanpinjaman'">
                <q-item-section>Pengajuan Pinjaman</q-item-section>
              </q-item>
              <q-item clickable v-ripple :to="'/transaksipinjaman'">
                <q-item-section>Transaksi Pinjaman</q-item-section>
              </q-item>
            </q-list>
          </q-expansion-item>
          <q-expansion-item
            expand-separator
            icon="money"
            label="Simpanan">
            <q-list padding>
              <q-item clickable v-ripple :to="'/transaksisimpanan'">
                <q-item-section>Transaksi Simpanan</q-item-section>
              </q-item>
              <q-item clickable v-ripple :to="'/aktivasisimpanan'">
                <q-item-section>Aktivasi Simpanan</q-item-section>
              </q-item>
            </q-list>
          </q-expansion-item>
          <q-expansion-item
            expand-separator
            icon="settings"
            label="Pengaturan">
            <q-list padding>
              <q-item clickable v-ripple :to="'/pengaturanpendaftarananggota'">
                <q-item-section>Form Pendaftaran Anggota</q-item-section>
              </q-item>
              <q-item clickable v-ripple :to="'/pengaturanpinjaman'">
                <q-item-section>Pinjaman</q-item-section>
              </q-item>
              <q-item clickable v-ripple :to="'/pengaturansimpanan'">
                <q-item-section>Simpanan</q-item-section>
              </q-item>
            </q-list>
          </q-expansion-item>
          <q-item clickable v-ripple :to="'/laporan'">
            <q-item-section avatar>
              <q-icon color="primary" name="report"/>
            </q-item-section>
            <q-item-section>Laporan</q-item-section>
          </q-item>
          <q-expansion-item
            expand-separator
            icon="send"
            label="Kirim Laporan Ke Dinas Koperasi Toba">
            <q-list padding>
              <q-item clickable v-ripple :to="'/kirimlaporan'">
                <q-item-section>Kirim Laporan</q-item-section>
              </q-item>
              <q-item clickable v-ripple :to="'/laporanterkirim'">
                <q-item-section>Laporan Terkirim</q-item-section>
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
        title: 'Selamat datang Koperasi ',
        image: null,
        existNotif: false,
        totalNotif: 0
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
            this.getNotif()
          })
      },
      getNotif() {
        this.$http.get('/api/getnotifikasikoperasi', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            const { data } = res
            this.existNotif = data.exist
            if (this.existNotif) {
              this.totalNotif = data.total
            }
          })
          .catch(() => {
            this.$q.notify({
              type: 'negative',
              message: `Terjadi kesalahan, refresh (F5)`
            })
          })
      },
      notif() {
        this.$q.loading.show()
        this.$http.get('/api/changestatusnotifkoperasi', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.getNotif()
            const temp = window.location;
            const ss = temp.toString()
              .split("/")
            if (ss[ss.length - 1] !== 'notifikasiksp') {
              this.$router.push('/notifikasiksp')
            } else {
              this.$q.loading.hide()
            }
          })
      }
    },
    created() {
      this.getStateKoperasi()
    }
  };
</script>
