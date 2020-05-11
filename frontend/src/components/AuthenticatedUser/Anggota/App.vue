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
      <router-view/>
    </q-layout>
  </div>
</template>

<script>

  export default {
    data() {
      return {
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
