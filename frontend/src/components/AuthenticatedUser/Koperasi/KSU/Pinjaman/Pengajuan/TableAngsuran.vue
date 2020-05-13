<template>
  <div class="q-pa-md">
    <q-card class="full-width">
      <q-card-section>Identitas Pengaju</q-card-section>
      <q-card-section>
        <q-table
          :dense="$q.screen.lt.md"
          :data="data"
          :columns="columns"
          row-key="name"
        />
      </q-card-section>
      <q-card-section>
        <q-table
          :dense="$q.screen.lt.md"
          :data="dataAjuan"
          :columns="columnsAjuan"
          row-key="name"/>
      </q-card-section>
    </q-card>
  </div>
</template>

<script>
  import moment from 'moment'

  export default {
    props: ['user'],
    data() {
      return {
        columns: [],
        data: [],
        dataAjuan: [],
        columnsAjuan: [
          {
            name: 'termin',
            label: 'Termin Ke #',
            align: 'center',
            field: row => row.urutanKe,
            sortable: true,
          },
          {
            name: 'status',
            label: 'Status',
            align: 'center',
            field: row => (row.statusBayar ? 'Paid' : 'Unpaid'),
            sortable: true,
          },
          {
            name: 'angsuranPokok',
            label: 'Angsuran Pokok',
            align: 'center',
            field: row => this.toIDR(parseInt(row.angsuranPokok)),
            sortable: true,
          },
          {
            name: 'bunga',
            label: 'Bunga',
            align: 'center',
            field: row => this.toIDR(parseInt(row.bunga)),
            sortable: true,
          },
          {
            name: 'totalAngsuran',
            label: 'Total Angsuran',
            align: 'center',
            field: row => this.toIDR(parseInt(row.totalAngsuran)),
            sortable: true,
          },
          {
            name: 'tglJatuhTempo',
            label: 'Tanggal Jatuh Tempo',
            align: 'center',
            field: (row) => {
              moment.lang('id')
              return moment(row.tanggalJatuhTempo)
                .format('dddd, Do MMMM YYYY')
            },
            sortable: true,
          },
          {
            name: 'denda',
            label: 'Denda',
            align: 'center',
            field: row => this.toIDR(parseInt(row.denda === null ? 0 : row.denda)),
            sortable: true,
          },
          {
            name: 'totalTagihan',
            label: 'Total Tagihan',
            align: 'center',
            field: row => this.toIDR(parseInt(row.totalTagihan === null ? 0 : row.totalTagihan)),
            sortable: true,
          },
          {
            name: 'totalBayar',
            label: 'Total Bayar',
            align: 'center',
            field: row => this.toIDR(parseInt(row.totalBayar === null ? 0 : row.totalBayar)),
            sortable: true,
          }
        ]
      }
    },
    methods: {
      toIDR(num) {
        const nums = `${num}`
        let ans = ''
        let coma = 0
        for (let i = nums.length - 1; i >= 0; i--) {
          ans = `${ans}${nums[i]}`
          if (coma === 2 && i !== 0) {
            ans = `${ans},`
            coma = 0;
          } else {
            coma++;
          }
        }
        let res = 'Rp '
        for (let i = ans.length - 1; i >= 0; i--) {
          res = `${res}${ans[i]}`
        }
        return res;
      },
      loadColumn() {
        this.$q.loading.show()
        this.$http.get(`/api/getcolumnmember/${this.user.id}`, {
          headers: this.$auth.getHeader(),
        })
          .then((res) => {
            const columns = res.data;
            for (let i = 0; i < columns.length; i++) {
              this.columns.push(
                {
                  name: columns[i].cid,
                  label: columns[i].label,
                  align: 'center',
                  field: columns[i].cid,
                  sortable: true,
                }
              )
            }
            this.loadData();
          })
          .catch((err) => {
            this.$q.loading.hide();
          });
      },
      loadData() {
        this.$http.get(`/api/getdatamember/${this.user.id}`, {
          headers: this.$auth.getHeader(),
        })
          .then((res) => {
            const { data } = res;
            for (let i = 0; i < data.length; i++) {
              let str = `{"id" : ${data[i].id},`
              const obj = JSON.parse(data[i].data)
              for (let j = 0; j < obj.length; j++) {
                str = `${str} "${obj[j].uid}":`
                if (obj[j].value instanceof Object) {
                  const val = Object.values(obj[j].value)
                  for (let k = 0; k < val.length; k++) {
                    if (k === val.length - 1) {
                      if (j === obj.length - 1) {
                        str = `${str} ${val[k]}"`
                      } else {
                        str = `${str} ${val[k]}",`
                      }
                    } else if (k === 0) {
                      str = `${str} "${val[k]}`
                    } else {
                      str = `${str} ${val[k]}`
                    }
                  }
                } else if (j === obj.length - 1) {
                  str = `${str} "${obj[j].value}"`
                } else {
                  str = `${str} "${obj[j].value}",`
                }
              }
              str = `${str} }`
              const ll = JSON.parse(str)
              this.data.push(ll)
            }
            this.loaddataAjuan()
          })
          .catch(() => {
            this.$q.loading.hide();
          })
      },
      loaddataAjuan() {
        this.$http.get(`/api/getdataajuan/${this.user.id}`, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.dataAjuan = res.data
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      }
    },
    created() {
      this.loadColumn()
    }
  }
</script>

<style scoped>

</style>
