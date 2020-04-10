<template>
  <q-layout view="hHh lpR fFf">
    <q-header elevated class="bg-primary text-white">
      <q-toolbar>
        <q-toolbar-title>
          Daftar Koperasi
        </q-toolbar-title>
        <q-btn flat round dense icon="fa fa-sign-out-alt" @click="logOut">
          <q-tooltip>Keluar</q-tooltip>
        </q-btn>
      </q-toolbar>
    </q-header>

    <q-page-container>
      <router-view />
    </q-page-container>

  </q-layout>
</template>

<script>
export default {
  data() {
    return {
    };
  },
  methods: {
    logOut() {
      window.localStorage.clear();
      window.location.href = '/';
    },
  },
};
</script>
<style scoped>

</style>
