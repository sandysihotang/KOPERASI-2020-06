<template>
  <div class="q-pa-md">
    <q-card-section>
      Pengajuan Baru
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
      <q-select
        filled
        v-model="select"
        :options="options"
        label="Jenis Simpanan"
        emit-value
        @input="setAturan"
        map-options
      />
      <br>
      <q-input filled v-model="date" mask="date" label="Tanggal Mulai" :rules="['date']">
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
        closeUp: false,
        nama: null,
        namaPengaju: false,
        filter: '',
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
        selected: [],
        select: null,
        options: [
          {
            label: 'Simpanan Pokok',
            value: 1
          }, {
            label: 'Simpanan Wajib',
            value: 2
          }, {
            label: 'Simpanan Sukarela',
            value: 3
          }
        ],
        date: null,
        maxSimpanan: null,
        aturan: [],
        jumlahSimpanan: null
      }
    },
    methods: {
      call() {
        this.$emit('call')
      },
      ajukan() {
        if (this.select === null || this.date === null || this.jumlahSimpanan === null || this.selected.length === 0) {
          this.$swal({
            position: 'center',
            type: 'error',
            title: 'Isi Semua field',
            showConfirmButton: false,
            timer: 1500,
          });
          return
        }
        this.$q.loading.show()
        this.$http.post(`/api/aktivasisimpanan/${this.selected[0].id}/${this.select}`, {
          date: this.date,
          jumlahSimpanan: this.jumlahSimpanan,
        }, {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            if (res.data.errCode === 400) {
              this.$q.notify({
                type: 'negative',
                message: `Pengajuan aktivasi dengan user yang sama sudah pernah dilakukan`
              })
              this.$q.loading.hide()
            } else {
              this.$emit("call")
              this.$q.notify({
                type: 'positive',
                message: `Pengajuan Berhasil Dilakukan`
              })
              this.select = null
              this.selected = []
              this.jumlahSimpanan = null
              this.nama = null
            }
          })
          .catch(() => {
            this.$q.loading.hide()
          })
      },
      res() {
        if (parseInt(this.jumlahSimpanan) < parseInt(this.maxSimpanan)) {
          this.jumlahSimpanan = parseInt(this.maxSimpanan)
        }
      },
      setAturan() {
        for (let i = 0; i < this.aturan.length; i++) {
          if (this.aturan[i].jenisSimpanan === this.select) {
            this.maxSimpanan = this.aturan[i].minimalSimpanan
            this.jumlahSimpanan = this.maxSimpanan
            return
          }
        }
      },
      setNama() {
        this.nama = `${this.selected[0].userDetail.firstName} ${this.selected[0].userDetail.lastName}`
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
        this.$http.get('/api/getdatapengajusimpanan', {
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
      getAturan() {
        this.$http.get('/api/getaturansimpanan', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.aturan = res.data
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
