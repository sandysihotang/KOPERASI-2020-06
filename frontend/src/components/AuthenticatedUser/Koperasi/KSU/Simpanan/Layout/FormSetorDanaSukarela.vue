<template>
  <div class="q-pa-md">
    <q-card-section v-if="cc">
      <q-table
        :dense="$q.screen.lt.md"
        title="Detail Identitas Anggota"
        :data="dataS"
        :columns="columnsS"
        row-key="name"
      />
    </q-card-section>
    <q-card-section>
      <q-input
        @input="showNama"
        square clearable
        filled
        v-model="nama"
        label="Nama Pemohon"
      />
      <br>
      <q-input filled v-model="date" mask="date" label="Tanggal Transaksi" :rules="['date']">
        <template v-slot:append>
          <q-icon name="event" class="cursor-pointer">
            <q-popup-proxy ref="qDateProxy" transition-show="scale" transition-hide="scale">
              <q-date v-model="date" @input="() => $refs.qDateProxy.hide()"/>
            </q-popup-proxy>
          </q-icon>
        </template>
      </q-input>
      <br>
      <q-input
        square
        clearable
        type="text"
        v-model="jumlahSimpanan"
        label="Jumlah Simpanan"
        mask="Rp #,###,###,###"
        fill-mask="0"
        reverse-fill-mask
        unmasked-value
        input-class="text-right">
        <template v-slot:prepend>
          <q-icon name="money"/>
        </template>
      </q-input>
      <br>
      <center>
        <q-btn color="red" label="Close" v-close-popup/>&nbsp;
        <q-btn color="primary" label="Ajukan" @click="ajukan"/>
      </center>
    </q-card-section>
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
        cc: false,
        namaPengaju: false,
        dataS: [],
        columnsS: [],
        nama: null,
        data: [],
        columns: [
          {
            name: 'nama',
            label: 'Nama',
            align: 'center',
            field: row => `${row.userDetail.firstName} ${row.userDetail.lastName}`,
            sortable: true,
          }, {
            name: 'alamat',
            label: 'Alamat',
            align: 'center',
            field: row => row.userDetail.address,
            sortable: true,
          }, {
            name: 'notelepon',
            label: 'No Telepon',
            align: 'center',
            field: row => row.userDetail.noTelepon,
            sortable: true,
          },
        ],
        jumlahSimpanan: null,
        maxSimpanan: null,
        date: null,
        selected: [],
        select: null,
        filter: '',
        idAturan: null
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
      ajukan() {
        if (this.jumlahSimpanan < this.maxSimpanan) {
          this.$q.notify({
            type: 'negative',
            message: `Minimal Penyimpanan ${this.toIDR(this.maxSimpanan)}`
          })
          return
        }
        if (this.nama === null || this.jumlahSimpanan === null || this.date === null) {
          this.$q.notify({
            type: 'negative',
            message: 'Isi semua field'
          })
          return
        }
        this.$q.loading.show()
        this.$http.post(`/api/transaksisimpanan/${this.idAturan}`, {
          jumlahTransaksi: this.jumlahSimpanan,
          date: this.date
        }, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.$q.notify({
              type: 'positive',
              message: 'Transaksi berhasil'
            })
            this.cc = false
            this.nama = null
            this.$emit('refresh')
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      deleteName() {
        this.nama = null
      },
      loadColumn() {
        this.$q.loading.show()
        this.$http.get(`/api/getcolumnmemberfortransaksisimpanan/${this.selected[0].id}`, {
          headers: this.$auth.getHeader(),
        })
          .then((res) => {
            const columns = res.data;
            for (let i = 0; i < columns.length; i++) {
              this.columnsS.push(
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
        this.$http.get(`/api/getdatamemberfortransaksisimpanan/${this.selected[0].id}`, {
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
              this.dataS.push(ll)
            }
            this.cc = true
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.loading.hide();
          })
      },
      showNama() {
        this.namaPengaju = true
        this.loadDatas()
      },
      loadDatas() {
        this.$q.loading.show()
        this.$http.get('/api/getdataanggotasimpanansukarela', {
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
      setNama() {
        this.nama = `${this.selected[0].userDetail.firstName} ${this.selected[0].userDetail.lastName}`
        const res = this.selected[0].aktivasiSimpanan
        for (let i = 0; i < res.length; i++) {
          if (res[i].jenisSimpanan === 2) {
            this.idAturan = res[i].id
            break
          }
        }
        this.loadColumn()
      },
      getAturan() {
        this.$http.get('/api/getaturansimpanan/3', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.jumlahSimpanan = res.data.minimalSimpanan
            this.maxSimpanan = res.data.minimalSimpanan
          })
      }
    },
    created() {
      this.getAturan()
    }
  }
</script>

<style scoped>

</style>
