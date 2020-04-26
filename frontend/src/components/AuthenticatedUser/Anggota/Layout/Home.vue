<template>
  <div>
    <q-card>
      <center>
        {{nama}}
        <br>
        <h5>
          <q-badge color="primary" outline label="Total simpanan: Rp. 0"/>
        </h5>
      </center>
    </q-card>
    <q-separator/>
    <q-card>
      <q-card-section>
        Tagihan
      </q-card-section>
      <q-separator/>
      <center>
        <div class="text-purple">
          <q-icon name="fa fa-smile" size="xl"/>
        </div>
        <div class="text-black">
          Yeay kamu tidak punya tagihan!
        </div>
      </center>
    </q-card>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        nama: ''
      }
    },
    methods: {
      getName() {
        this.$http.get('/api/getnameanggota', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.nama = res.data
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      }
    },
    created() {
      this.getName();
    }
  }
</script>
