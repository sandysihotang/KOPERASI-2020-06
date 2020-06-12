<template>
  <q-layout view="hHh lpR fFf">

    <q-header
      style="background: radial-gradient(circle at 40% 91%, rgba(251, 251, 251,0.04) 0%, rgba(251, 251, 251,0.04) 50%,rgba(229, 229, 229,0.04) 50%, rgba(229, 229, 229,0.04) 100%),radial-gradient(circle at 66% 97%, rgba(36, 36, 36,0.04) 0%, rgba(36, 36, 36,0.04) 50%,rgba(46, 46, 46,0.04) 50%, rgba(46, 46, 46,0.04) 100%),radial-gradient(circle at 86% 7%, rgba(40, 40, 40,0.04) 0%, rgba(40, 40, 40,0.04) 50%,rgba(200, 200, 200,0.04) 50%, rgba(200, 200, 200,0.04) 100%),radial-gradient(circle at 15% 16%, rgba(99, 99, 99,0.04) 0%, rgba(99, 99, 99,0.04) 50%,rgba(45, 45, 45,0.04) 50%, rgba(45, 45, 45,0.04) 100%),radial-gradient(circle at 75% 99%, rgba(243, 243, 243,0.04) 0%, rgba(243, 243, 243,0.04) 50%,rgba(37, 37, 37,0.04) 50%, rgba(37, 37, 37,0.04) 100%),linear-gradient(90deg, rgb(34, 222, 237),rgb(135, 89, 215));"
      className="text-white" height-hint="98">
      <q-toolbar>
        <q-btn label="TobaKo" flat/>
        <q-space/>
        <q-tabs shrink>
          <q-route-tab to="#home" label="Beranda"/>
          <q-route-tab to="#about" label="Tentang"/>
          <q-route-tab to="#contact" label="Kontak"/>
          <q-btn color="dark-orange" elevated text-color="white" icon="person" :to="'/login'">Masuk
          </q-btn>
        </q-tabs>
      </q-toolbar>
    </q-header>

    <q-page-container id="home"
                      style="height: 100vh; width: 100%; background: radial-gradient(circle at 40% 91%, rgba(251, 251, 251,0.04) 0%, rgba(251, 251, 251,0.04) 50%,rgba(229, 229, 229,0.04) 50%, rgba(229, 229, 229,0.04) 100%),radial-gradient(circle at 66% 97%, rgba(36, 36, 36,0.04) 0%, rgba(36, 36, 36,0.04) 50%,rgba(46, 46, 46,0.04) 50%, rgba(46, 46, 46,0.04) 100%),radial-gradient(circle at 86% 7%, rgba(40, 40, 40,0.04) 0%, rgba(40, 40, 40,0.04) 50%,rgba(200, 200, 200,0.04) 50%, rgba(200, 200, 200,0.04) 100%),radial-gradient(circle at 15% 16%, rgba(99, 99, 99,0.04) 0%, rgba(99, 99, 99,0.04) 50%,rgba(45, 45, 45,0.04) 50%, rgba(45, 45, 45,0.04) 100%),radial-gradient(circle at 75% 99%, rgba(243, 243, 243,0.04) 0%, rgba(243, 243, 243,0.04) 50%,rgba(37, 37, 37,0.04) 50%, rgba(37, 37, 37,0.04) 100%),linear-gradient(90deg, rgb(34, 222, 237),rgb(135, 89, 215));">
      <Home/>
    </q-page-container>
    <q-page-container id="about"
                      style="height: 100vh; width:100%;background-image: radial-gradient(circle at 82% 60%, rgba(59, 59, 59,0.06) 0%, rgba(59, 59, 59,0.06) 69%,transparent 69%, transparent 100%),radial-gradient(circle at 36% 0%, rgba(185, 185, 185,0.06) 0%, rgba(185, 185, 185,0.06) 59%,transparent 59%, transparent 100%),radial-gradient(circle at 58% 82%, rgba(183, 183, 183,0.06) 0%, rgba(183, 183, 183,0.06) 17%,transparent 17%, transparent 100%),radial-gradient(circle at 71% 32%, rgba(19, 19, 19,0.06) 0%, rgba(19, 19, 19,0.06) 40%,transparent 40%, transparent 100%),radial-gradient(circle at 77% 5%, rgba(31, 31, 31,0.06) 0%, rgba(31, 31, 31,0.06) 52%,transparent 52%, transparent 100%),radial-gradient(circle at 96% 80%, rgba(11, 11, 11,0.06) 0%, rgba(11, 11, 11,0.06) 73%,transparent 73%, transparent 100%),radial-gradient(circle at 91% 59%, rgba(252, 252, 252,0.06) 0%, rgba(252, 252, 252,0.06) 44%,transparent 44%, transparent 100%),radial-gradient(circle at 52% 82%, rgba(223, 223, 223,0.06) 0%, rgba(223, 223, 223,0.06) 87%,transparent 87%, transparent 100%),radial-gradient(circle at 84% 89%, rgba(160, 160, 160,0.06) 0%, rgba(160, 160, 160,0.06) 57%,transparent 57%, transparent 100%),linear-gradient(90deg, rgb(46, 75, 248),rgb(166, 255, 237));">
      <About/>
    </q-page-container>
    <q-page-container id="contact"
                      style="height: 100vh; width:100%;background-image: radial-gradient(circle at 53% 25%, rgba(203, 203, 203,0.04) 0%, rgba(203, 203, 203,0.04) 36%,transparent 36%, transparent 100%),radial-gradient(circle at 48% 27%, rgba(22, 22, 22,0.04) 0%, rgba(22, 22, 22,0.04) 45%,transparent 45%, transparent 100%),radial-gradient(circle at 65% 50%, rgba(219, 219, 219,0.04) 0%, rgba(219, 219, 219,0.04) 61%,transparent 61%, transparent 100%),radial-gradient(circle at 78% 82%, rgba(229, 229, 229,0.04) 0%, rgba(229, 229, 229,0.04) 26%,transparent 26%, transparent 100%),radial-gradient(circle at 99% 75%, rgba(96, 96, 96,0.04) 0%, rgba(96, 96, 96,0.04) 31%,transparent 31%, transparent 100%),radial-gradient(circle at 17% 28%, rgba(188, 188, 188,0.04) 0%, rgba(188, 188, 188,0.04) 15%,transparent 15%, transparent 100%),radial-gradient(circle at 19% 19%, rgba(25, 25, 25,0.04) 0%, rgba(25, 25, 25,0.04) 68%,transparent 68%, transparent 100%),radial-gradient(circle at 35% 23%, rgba(31, 31, 31,0.04) 0%, rgba(31, 31, 31,0.04) 18%,transparent 18%, transparent 100%),linear-gradient(90deg, rgb(138, 193, 238),rgb(20, 21, 171));">
      <div class="q-pa-md">
        <center>
          <q-card>
            <img src="https://drive.google.com/uc?export=view&id=1qCLDpHbn-eN-lQN_mOSd7_vPT9zTg2EW"
                 style="width: 100%; max-width: 100px;">
            <q-card-section>
              <div class="text-h6 center">Koperasi</div>
            </q-card-section>
            <q-card-section class="q-pt-none" align="justify">
              Koperasi adalah sebuah organisasi ekonomi yang dimiliki dan dioperasikan oleh
              orang-seorang demi kepentingan bersama. Koperasi melandaskan kegiatan berdasarkan
              prinsip gerakan ekonomi rakyat yang berdasarkan asas kekeluargaan.
              <br>
              Prinsip koperasi adalah suatu sistem ide-ide abstrak yang merupakan petunjuk untuk
              membangun koperasi yang efektif dan tahan lama.[3] Prinsip koperasi terbaru yang
              dikembangkan International Cooperative Alliance (Federasi koperasi non-pemerintah
              internasional) adalah
              <br>
              <ul>
                <li>Keanggotaan yang bersifat terbuka dan sukarela</li>
                <li>Pengelolaan yang demokratis</li>
                <li>Partisipasi anggota dalam ekonomi</li>
                <li>Kebebasan dan otonomi</li>
                <li>Pengembangan pendidikan, pelatihan, dan informasi</li>
              </ul>
            </q-card-section>
          </q-card>
        </center>
      </div>
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
      About: () => import('./Landing/About.vue')
    },
  };
</script>
<style scoped>
</style>
