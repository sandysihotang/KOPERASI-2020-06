<template>
  <div v-if="!isHaveMember">
    <q-card
      class="my-card text-white"
      style="height: 100%; width:100%; background: radial-gradient(circle, #35a2ff 0%, #014a88 100%)"
    >
      <q-card-section>
        <div class="text-h6" align="center">Warning</div>
      </q-card-section>
      <q-card-section>
        <div class="row">
          <div class="col"/>
          <div class="col">
            <q-icon name="warning" class="text-red" style="font-size: 5rem;"/>
          </div>
          <div class="col"/>
        </div>
      </q-card-section>
      <q-card-section class="q-pt-none">
        <div align="justify">
          Silahkan isi terlebih dahulu form pendaftaran anggota
        </div>
      </q-card-section>
    </q-card>
  </div>
  <div v-else class="q-pa-md">
    <q-table
      :dense="$q.screen.lt.md"
      title="Daftar Anggota Koperasi"
      :data="data"
      :columns="columns"
      :filter="filter"
      row-key="id"
      selection="single"
      :selected.sync="selected"
    >
      <template v-slot:top-left>
        <q-btn icon="person" color="primary" size="xs"
               :to="(getStateKoperasi() === 2 ? '/daftaranggota' : '/daftaranggotaksu')"
               label="Tambah Anggota">
        </q-btn>
      </template>
      <template v-slot:top-right>
        <div class="row">
          <div class="col">
            <q-btn color="primary" icon="fa fa-ban" :disable="selected.length === 0" @click="ss"
                   size="xs"
                   label="NonActive"/>
          </div>
          <div class="col">
            <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
              <template v-slot:append>
                <q-icon name="search"/>
              </template>
            </q-input>
          </div>
        </div>
      </template>
    </q-table>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        filter: '',
        isHaveMember: false,
        data: [],
        columns: [],
        selected: [],
      }
    },
    methods: {
      ss() {
        this.$swal.fire({
          title: 'Anda yakin?',
          text: `Ingin nonaktifkan Anggota ini?`,
          type: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: `Ya, nonaktifkan!`,
        })
          .then((result) => {
            if (result.value) {
              this.change()
            } else {
              this.$q.loading.show();
              this.loadColumn();
            }
          });
      },
      change() {
        this.$q.loading.show()
        this.$http.put(`/api/nonactivememberkoperasi/${this.selected[0].id}`, {}, {
          headers: this.$auth.getHeader()
        })
          .then(() => {
            this.$q.notify({
              type: 'positive',
              message: `Berhasil menonaktifkan anggota`
            })
            this.loadColumn()
          })
          .catch(() => {
            this.$q.notify({
              type: 'negative',
              message: `Terjadi error silahkan refresh (F5)`
            })
            this.$q.loading.hide()
          })
      },
      isHave() {
        this.$q.loading.show();
        this.$http.get('/api/isHaveField', {
          headers: this.$auth.getHeader(),
        })
          .then((data) => {
            this.isHaveMember = data.data
            if (this.isHaveMember === true) this.loadColumn();
            else this.$q.loading.hide()
          })
          .catch((err) => {
            this.$q.loading.hide();
          });
      },
      loadColumn() {
        this.$http.get('/api/getcolumnmember', {
          headers: this.$auth.getHeader(),
        })
          .then((res) => {
            const columns = res.data;
            this.columns = []
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
      getStateKoperasi() {
        return parseInt(localStorage.getItem('jenisKoperasi'))
      },
      loadData() {
        this.$http.get('/api/getdatamember', {
          headers: this.$auth.getHeader(),
        })
          .then((res) => {
            const { data } = res;
            this.data = []
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
      }
    },
    created() {
      this.isHave()
    }
  };
</script>

<style scoped>
  .my-card {
    width: 100%;
    height: 100%;
    max-height: 400px;
    max-width: 250px;
    margin-top: 40px;
    padding: 10px;
    position: absolute;
    top: 50%;
    left: 50%;
    margin-right: -50%;
    transform: translate(-50%, -50%)
  }
</style>
