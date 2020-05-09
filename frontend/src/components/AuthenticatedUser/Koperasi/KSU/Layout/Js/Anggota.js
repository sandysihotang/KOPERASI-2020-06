import { Pie } from 'vue-chartjs';

export default {
  extends: Pie,
  methods: {
    get() {
      this.$http.get('/api/getanggotaaknonak', {
        headers: this.$auth.getHeader()
      })
        .then((res) => {
          this.renderChart({
            labels: ['Anggota Aktif', 'Anggota Tidak Aktif'],
            datasets: [
              {
                backgroundColor: ['#f87979', '#58E19D'],
                data: [res.data.aktif, res.data.nonAktif]
              }
            ]
          });
        })
        .catch(() => {
        });
    }
  },
  mounted() {
    this.get();
  }
};
