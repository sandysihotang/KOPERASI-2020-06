<template>
  <div>
    <q-layout view="hHh lpR fFf">

      <q-header elevated class="bg-primary text-white">
        <q-toolbar>
          <q-toolbar-title>
            {{ koperasi }}
          </q-toolbar-title>
          <q-btn dense color="primary" round icon="info" class="q-ml-md" @click="notif">
            <q-badge color="red" floating v-if="existNotif">{{ totalNotif }}</q-badge>
          </q-btn>
          <q-btn flat round dense icon="fa fa-sign-out-alt" @click="logOut"/>
        </q-toolbar>
      </q-header>
      <router-view/>
    </q-layout>
  </div>
</template>

<script>

  export default {
    data() {
      return {
        koperasi: 'Koperasi ',
        existNotif: false,
        totalNotif: 0
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
            this.getNotif();
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      getNotif() {
        this.$q.loading.show();
        this.$http.get('/api/notifikasianggota', {
          headers: this.$auth.getHeader(),
        })
          .then((res) => {
            const { data } = res
            this.existNotif = data.exist
            if (this.existNotif === true) {
              this.totalNotif = data.total
            }
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      notif() {
        this.$http.get('/api/changestatusnotif', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.getNotif()
            const temp = window.location;
            const ss = temp.toString()
              .split("/")
            if (ss[ss.length - 1] !== 'notifikasi') {
              this.$router.push('/notifikasi')
            } else {
              this.$q.loading.hide()
            }
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
