<template>
  <div class="q-pa-md">
    <q-card class="full-width">
      <q-card-section>Idenitas Pengaju</q-card-section>
      <q-card-section>
        <q-table
          :dense="$q.screen.lt.md"
          :data="data"
          :columns="columns"
          row-key="name"
        />
      </q-card-section>
    </q-card>
    <q-card class="full-width">
      <q-card-section>
        <q-input
          filled
          v-model="price"
          label="Total pinjaman"
          mask="Rp #,###,###,###.##"
          fill-mask="0"
          reverse-fill-mask
          unmasked-value
          input-class="text-right"
        />
        <q-input
          filled
          v-model="tenor"
          label="Tenor (Bulan)"
          mask="## Bln"
          fill-mask="0"
          reverse-fill-mask
          unmasked-value
          input-class="text-right"
        />
        <q-select
          filled
          v-model="model"
          :options="options"
          stack-label
        />
      </q-card-section>
      <q-card-section>
        <q-list bordered separator>
          <q-item>
            <q-item-section>Angsuran Pokok</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>{{ toIDR((price / 100) / tenor)}}</q-item-section>
          </q-item>
          <q-item>
            <q-item-section>Bunga(%)</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>{{persentase}} %</q-item-section>
          </q-item>
          <q-item>
            <q-item-section>Bunga Angsuran</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>{{ toIDR((price/100) * persentase / 100) }}</q-item-section>
          </q-item>
          <q-item>
            <q-item-section>Total Angsuran</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>{{ toIDR(((price/100) / tenor) + ((price/100) * persentase / 100)) }}
            </q-item-section>
          </q-item>
          <q-item>
            <q-item-section>Total Bunga/Jasa</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>{{ toIDR((price / 100) * persentase / 100 * tenor) }}</q-item-section>
          </q-item>
          <q-item>
            <q-item-section>Total Pinjaman</q-item-section>
            <q-item-section>:</q-item-section>
            <q-item-section>{{ toIDR((price/100) + ((price/100) * persentase / 100 * tenor)) }}
            </q-item-section>
          </q-item>
        </q-list>
      </q-card-section>
    </q-card>
  </div>
</template>

<script>
  export default {
    props: ['user'],
    data() {
      return {
        columns: [],
        data: [],
        price: null,
        tenor: null,
        persentase: null,
        model: '',
        options: [
          'Cancel', 'Approved', 'Rejected', 'On Reviewed', 'Awaiting Approval',
        ]
      };
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
      reload() {
        this.price = this.user.jumlahPinjaman * 100
        this.tenor = this.user.tenor
        this.persentase = this.user.pengaturanPinjaman.bungaPinjaman
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
      call() {
        this.$q.loading.show()
        const status = new Map();
        status.set('Awaiting Approval', 5)
        status.set('On Reviewed', 4)
        status.set('Rejected', 3)
        status.set('Approved', 2)
        status.set('Cancel', 1)
        this.$http.put(`/api/savepinjamanfrompengurus/${this.user.id}`, {
          price: this.price,
          tenor: this.tenor,
          status: status.get(this.model)
        }, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
          })
          .catch(() => {
            this.$q.loading.hide()
          })
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
            this.$q.loading.hide();
          })
          .catch(() => {
            this.$q.loading.hide();
          })
      },
    },
    created() {
      this.reload()
      this.model = this.options[this.user.status - 1];
      this.loadColumn()
    }
  }
</script>

<style scoped>

</style>
