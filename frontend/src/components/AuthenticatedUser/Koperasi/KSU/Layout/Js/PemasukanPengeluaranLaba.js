import { Line } from 'vue-chartjs';

export default {
  extends: Line,
  methods: {
    get() {
      this.$http.get('/api/getlabapengeluaranpemasukanmonth', {
        headers: this.$auth.getHeader()
      })
        .then((res) => {
          this.renderChart({
            labels: ['Pemasukan', 'Pengeluaran', 'Laba'],
            datasets: [
              {
                label: 'Laporan',
                backgroundColor: ['#f87979', '#58E19D'],
                data: [res.data.pemasukan, res.data.pengeluaran, res.data.laba]
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
