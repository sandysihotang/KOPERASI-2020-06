<template>
  <q-layout view="hHh lpR fFf">

    <q-header style="background: radial-gradient(circle at 40% 91%, rgba(251, 251, 251,0.04) 0%, rgba(251, 251, 251,0.04) 50%,rgba(229, 229, 229,0.04) 50%, rgba(229, 229, 229,0.04) 100%),radial-gradient(circle at 66% 97%, rgba(36, 36, 36,0.04) 0%, rgba(36, 36, 36,0.04) 50%,rgba(46, 46, 46,0.04) 50%, rgba(46, 46, 46,0.04) 100%),radial-gradient(circle at 86% 7%, rgba(40, 40, 40,0.04) 0%, rgba(40, 40, 40,0.04) 50%,rgba(200, 200, 200,0.04) 50%, rgba(200, 200, 200,0.04) 100%),radial-gradient(circle at 15% 16%, rgba(99, 99, 99,0.04) 0%, rgba(99, 99, 99,0.04) 50%,rgba(45, 45, 45,0.04) 50%, rgba(45, 45, 45,0.04) 100%),radial-gradient(circle at 75% 99%, rgba(243, 243, 243,0.04) 0%, rgba(243, 243, 243,0.04) 50%,rgba(37, 37, 37,0.04) 50%, rgba(37, 37, 37,0.04) 100%),linear-gradient(90deg, rgb(34, 222, 237),rgb(135, 89, 215));" className="text-white" height-hint="98">
      <q-toolbar>
        <q-toolbar-title>
          Koperasi
        </q-toolbar-title>
        <q-tabs align="left">
          <q-route-tab to="#home" label="Beranda"/>
          <q-route-tab to="#about" label="Tentang"/>
          <q-route-tab to="#contact" label="Kontak"/>
          <q-route-tab to="#service" label="Service"/>
        </q-tabs>
        <q-btn color="dark-orange" elevated text-color="white" icon="person">Masuk</q-btn>
      </q-toolbar>
    </q-header>

    <q-page-container id="home" style="height: 100vh; width: 100%; background: radial-gradient(circle at 40% 91%, rgba(251, 251, 251,0.04) 0%, rgba(251, 251, 251,0.04) 50%,rgba(229, 229, 229,0.04) 50%, rgba(229, 229, 229,0.04) 100%),radial-gradient(circle at 66% 97%, rgba(36, 36, 36,0.04) 0%, rgba(36, 36, 36,0.04) 50%,rgba(46, 46, 46,0.04) 50%, rgba(46, 46, 46,0.04) 100%),radial-gradient(circle at 86% 7%, rgba(40, 40, 40,0.04) 0%, rgba(40, 40, 40,0.04) 50%,rgba(200, 200, 200,0.04) 50%, rgba(200, 200, 200,0.04) 100%),radial-gradient(circle at 15% 16%, rgba(99, 99, 99,0.04) 0%, rgba(99, 99, 99,0.04) 50%,rgba(45, 45, 45,0.04) 50%, rgba(45, 45, 45,0.04) 100%),radial-gradient(circle at 75% 99%, rgba(243, 243, 243,0.04) 0%, rgba(243, 243, 243,0.04) 50%,rgba(37, 37, 37,0.04) 50%, rgba(37, 37, 37,0.04) 100%),linear-gradient(90deg, rgb(34, 222, 237),rgb(135, 89, 215));">
      <Home/>
    </q-page-container>
    <q-page-container id="about" style="height: 100vh; width:100%">
      <h1>About</h1>
    </q-page-container>
    <q-page-container id="contact" style="height: 100vh; width:100%">
      <h1>Kontak</h1>
    </q-page-container>
    <q-page-container id="service" style="height: 100vh; width:100%">
      <h1>Service</h1>
    </q-page-container>
  </q-layout>
</template>

<script>
export default {
  data() {
    return {};
  },
  components: {
    Home: () => import('./Landing/Home.vue'),
  },
};
</script>
<style scoped>
</style>
