<template>
    <div>
      <q-card
        class="my-card text-white"
        style="background: radial-gradient(circle, #35a2ff 0%, #014a88 100%)"
      >
        <q-card-section>
          <div class="text-h6" align="center">Koperasi Belum Diaktifkan</div>
        </q-card-section>
        <q-card-section>
          <div class="row">
            <div class="col"/>
            <div class="col">
              <q-icon name="warning" class="text-red" style="font-size: 5rem;" />
            </div>
            <div class="col"/>
          </div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <div align="justify">
            Tunggu aktivasi dari dinas koperasi, info akan dikirim melalui e-mail koperasi
          </div>
        </q-card-section>
      </q-card>
    </div>
</template>

<script>
export default {
  name: 'PendingActivation',
};
</script>

<style scoped>
  .my-card {
    width: 100%;
    max-width: 250px;
    padding: 10px;
    margin: 0;
    position: absolute;
    top: 50%;
    left: 50%;
    margin-right: -50%;
    transform: translate(-50%, -50%)
  }
</style>
