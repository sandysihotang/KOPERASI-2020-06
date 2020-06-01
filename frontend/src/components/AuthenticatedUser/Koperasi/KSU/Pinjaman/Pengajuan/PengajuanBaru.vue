<template>
  <div class="q-pa-md">
    <q-card>
      <div class="q-pa-md full-width">
        <div class="q-gutter-md">
          <q-input
            @input="showNama"
            square clearable
            filled
            v-model="nama"
            label="Nama Pemohon"
          />
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
            v-model="jaminan"
            label="Jaminan"
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
            @input="handle"
          />
        </div>
      </div>
    </q-card>
    <q-space/>
    <q-card v-if="showHandle">
      <q-list bordered separator>
        <q-item>
          <q-item-section>Angsuran Pokok</q-item-section>
          <q-item-section>:</q-item-section>
          <q-item-section>{{ toIDR(parseInt((price / 100) / tenor))}}</q-item-section>
        </q-item>
        <q-item>
          <q-item-section>Bunga(%)</q-item-section>
          <q-item-section>:</q-item-section>
          <q-item-section>{{persentase}} %</q-item-section>
        </q-item>
        <q-item>
          <q-item-section>Bunga Angsuran</q-item-section>
          <q-item-section>:</q-item-section>
          <q-item-section>{{ toIDR(parseInt((price/100) * persentase / 100)) }}</q-item-section>
        </q-item>
        <q-item>
          <q-item-section>Total Angsuran</q-item-section>
          <q-item-section>:</q-item-section>
          <q-item-section>{{ toIDR(parseInt(((price/100) / tenor) + ((price/100) * persentase / 100))) }}
          </q-item-section>
        </q-item>
        <q-item>
          <q-item-section>Total Bunga/Jasa</q-item-section>
          <q-item-section>:</q-item-section>
          <q-item-section>{{ toIDR(parseInt((price / 100) * persentase / 100 * tenor)) }}</q-item-section>
        </q-item>
        <q-item>
          <q-item-section>Total Pinjaman</q-item-section>
          <q-item-section>:</q-item-section>
          <q-item-section>{{ toIDR(parseInt((price/100) + ((price/100) * persentase / 100 * tenor))) }}
          </q-item-section>
        </q-item>
        <q-btn color="primary" class="full-width" @click="ajukanPinjaman">Ajukan Pinjaman</q-btn>
      </q-list>
    </q-card>

    <q-dialog v-model="namaPengaju" persistent transition-show="scale"
              transition-hide="scale">
      <q-card>
        <q-card-section>
          <q-table
            :dense="$q.screen.lt.md"
            title="Daftar Nama Anggota"
            :data="data"
            :columns="columns"
            row-key="id"
            selection="single"
            :selected.sync="selected"
            :filter="filter">
            <template v-slot:top-right>
              <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
                <template v-slot:append>
                  <q-icon name="search"/>
                </template>
              </q-input>
            </template>
          </q-table>
        </q-card-section>
        <q-card-section>
          <q-card-actions align="right" class="bg-white text-teal">
            <q-btn flat label="Close" v-close-popup @click="deleteName"/>
            <q-btn flat label="Pilih" v-close-popup :disable="selected.length === 0"
                   @click="setNama"/>
          </q-card-actions>
        </q-card-section>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        showHandle: false,
        price: 0,
        jaminan: null,
        tenor: 0,
        nama: null,
        namaPengaju: false,
        data: [],
        columns: [
          {
            name: 'nama',
            label: 'Nama',
            align: 'center',
            field: row => `${row.first_name} ${row.last_name}`,
            sortable: true,
          }, {
            name: 'alamat',
            label: 'Alamat',
            align: 'center',
            field: row => row.address,
            sortable: true,
          }, {
            name: 'notelepon',
            label: 'No Telepon',
            align: 'center',
            field: row => row.no_telepon,
            sortable: true,
          },
        ],
        selected: [],
        filter: '',
        persentase: null,
        id: null
      }
    },
    methods: {
      setNama() {
        this.nama = `${this.selected[0].first_name} ${this.selected[0].last_name}`
      },
      deleteName() {
        this.nama = null
      },
      showNama() {
        this.namaPengaju = true
        this.loadData()
      },
      loadData() {
        this.$q.loading.show()
        this.$http.get('/api/getdatapengajureqpinjaman', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.data = res.data
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      handle(s) {
        this.showHandle = s.length !== 0
      },
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
      ajukanPinjaman() {
        if (parseInt(this.price) === 0 || parseInt(this.tenor) === 0 || this.jaminan.length === 0) {
          this.$q.notify({
            type: 'negative',
            message: 'Isi Semua Field'
          })
          return
        }
        this.$q.loading.show()
        this.$http.post(`/api/requestpeminjamanfrompengurus/${this.selected[0].id}`, {
          id: this.id,
          jaminan: this.jaminan,
          tenor: this.tenor,
          price: this.price
        }, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            if (res.data.errCode === 400) {
              this.$q.notify({
                type: 'negative',
                message: res.data.err
              })
              this.$q.loading.hide()
            } else {
              this.showHandle = false;
              this.price = 0
              this.tenor = 0
              this.jaminan = null
              this.$emit('get')
              this.$q.notify({
                type: 'positive',
                message: 'Berhasil membuat peminjaman'
              })
            }
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      getSettings() {
        this.$http.get('/api/getpengaturanpinjamanreqpinjamaninpengurus', {
          headers: this.$auth.getHeader(),
        })
          .then((res) => {
            this.persentase = res.data.bunga_pinjaman
            this.id = res.data.id
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide()
            this.$swal({
              position: 'center',
              type: 'error',
              title: 'Terjadi Kesalhan, refresh (F5)',
              showConfirmButton: false,
              timer: 1500,
            });
          })
      },
    },
    created() {
      this.getSettings()
    }
  }
</script>

<style scoped>

</style>
