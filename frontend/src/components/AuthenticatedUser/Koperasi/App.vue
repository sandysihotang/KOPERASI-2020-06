<template>
  <q-layout view="hhh LpR fFf">

    <q-header elevated class="bg-primary text-white">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="left = !left"/>
        <q-toolbar-title>
          Koperasi
        </q-toolbar-title>
          <q-btn flat round dense icon="fa fa-sign-out-alt" @click="logOut"/>
      </q-toolbar>
    </q-header>

    <q-drawer show-if-above v-model="left" side="left" bordered>
      <q-scroll-area class="fit">
        <q-list padding>
          <q-item clickable v-ripple :to="'/dashboard'">
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
