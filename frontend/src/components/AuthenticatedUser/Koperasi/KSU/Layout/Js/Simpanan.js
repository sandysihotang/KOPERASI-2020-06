import { Bar } from 'vue-chartjs';

export default {
  extends: Bar,
  methods: {
    get() {
      this.$http.get('/api/getsimpanananggota', {
        headers: this.$auth.getHeader()
      })
        .then((res) => {
          this.renderChart({
            labels: ['Simpanan Pokok', 'Simpanan Wajib', 'Simpanan Sukarela'],
            datasets: [
              {
                label: 'Simpanan',
                backgroundColor: ['#F5AF19', '#7830E0', '#28E80B'],
                data: [res.data.pokok, res.data.wajib, res.data.sukarela]
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
