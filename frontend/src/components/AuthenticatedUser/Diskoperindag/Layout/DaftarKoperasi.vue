<template>
  <div class="q-pa-md">
    <q-table
      :dense="$q.screen.lt.md"
      title="Daftar Koperasi Di Toba"
      :data="data"
      :columns="columns"
      row-key="name"
      :filter="filter"
    >
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td key="namaKoperasi" :props="props">
            {{ props.row.namaKoperasi}}
          </q-td>
          <q-td key="jenisKoperasi" :props="props">
            {{ (props.row.jenisKoperasi === 1 ? 'Koperasi Serba Usaha (KSU)' :
            'Koperasi Simpan Pinjam (KSP)') }}
          </q-td>
          <q-td key="namaPendiri" :props="props">
            {{ props.row.namaPendiri}}
          </q-td>
          <q-td key="alamatKoperasi" :props="props">
            {{ props.row.alamatKoperasi }}
          </q-td>
          <q-td key="tahunBerdiriKoperasi" :props="props">
            {{ getDate(props.row.tahunBerdiriKoperasi) }}
          </q-td>
          <q-td key="email" :props="props">
            {{ props.row.email }}
          </q-td>
          <q-td key="noIzinKoperasi" :props="props">
            {{ props.row.noIzinKoperasi }}
          </q-td>
          <q-td key="logoKoperasi" :props="props">
            <div v-if="props.row.logoKoperasi!==null">
              <q-avatar>
                <img :src="dataUrl(props.row.logoKoperasi)" alt="">
              </q-avatar>
            </div>
            <div v-else>Logo Koperasi Tidak Ada</div>
          </q-td>
          <q-td key="aksi" :props="props">
            <div class="col">{{ toggle(props.row.id, props.row.haveKoperasi) }}
              <q-toggle
                v-model="selected[props.row.id]"
                @input="show(props.row.id)"
              />
            </div>
          </q-td>
        </q-tr>
      </template>
      <template v-slot:top-right>
        <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
          <template v-slot:append>
            <q-icon name="search"/>
          </template>
        </q-input>
      </template>
    </q-table>
    <q-dialog v-model="acc" persistent transition-show="scale"
              transition-hide="scale">
      <q-card>
        <q-card-section>
          <div class="text-h6">Kirim Pesan Untuk Koperasi</div>
        </q-card-section>
        <q-separator/>
        <q-card-section>
          <div class="q-pa-md" style="max-width: 300px">
            <q-input
              v-model="text"
              filled
              label="Pesan"
              type="textarea"
            />
          </div>
          <br>
          <center>
            <q-btn color="red" label="Tutup" v-close-popup/>&nbsp;
            <q-btn color="primary" label="Kirim Pesan" @click="changeState(id)" v-close-popup/>
          </center>
        </q-card-section>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
  import moment from 'moment'

  export default {
    data() {
      return {
        text: null,
        acc: false,
        filter: '',
        columns: [
          {
            name: 'namaKoperasi',
            label: 'Nama Koperasi',
            align: 'center',
            field: 'namaKoperasi',
            sortable: true,
          },
          {
            name: 'jenisKoperasi',
            label: 'Jenis Koperasi',
            align: 'center',
            field: 'jenisKoperasi',
            sortable: true,
          },
          {
            name: 'namaPendiri',
            label: 'Nama Pendiri',
            align: 'center',
            field: 'namaPendiri',
            sortable: true,
          },
          {
            name: 'alamatKoperasi',
            label: 'Alamat Koperasi',
            align: 'center',
            field: 'alamatKoperasi',
            sortable: true,
          },
          {
            name: 'tahunBerdiriKoperasi',
            align: 'center',
            label: 'Tahun Berdiri Koperasi',
            field: 'tahunBerdiriKoperasi',
            sortable: true,
          },
          {
            name: 'email',
            align: 'center',
            label: 'Email Koperasi',
            field: 'email',
            sortable: true,
          },
          {
            name: 'noIzinKoperasi',
            align: 'center',
            label: 'No Izin Koperasi',
            field: 'noIzinKoperasi',
          },
          {
            name: 'logoKoperasi',
            align: 'center',
            label: 'Logo Koperasi',
            field: 'logoKoperasi',
          },
          {
            name: 'aksi',
            align: 'center',
            label: 'Aksi',
            field: 'aksi',
          },
        ],
        data: [],
        selected: [],
        id: null
      };
    },
    methods: {
      dataUrl(r) {
        return `data:image/jpeg;base64,${r}`
      },
      getDate(e) {
        return moment(e)
          .format('dddd, Do MMMM YYYY');
      },
      getData() {
        this.$http.get('/api/getallkoperasi', {
          headers: this.$auth.getHeader(),
        })
          .then((res) => {
            this.data = res.data;
            this.$q.loading.hide();
          })
          .catch(() => {
            this.$q.loading.hide();
          });
      },
      show(id) {
        this.acc = true
        this.id = id
      },
      changeState(id) {
        this.$swal.fire({
          title: 'Anda yakin?',
          text: `Ingin ${this.selected[id] ? 'nonaktifkan' : 'mengaktifakan'} koperasi ini?`,
          type: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: `Ya, ${this.selected[id] ? 'nonaktifkan' : 'aktifkan'}!`,
        })
          .then((result) => {
            if (result.value) {
              this.change(id);
            } else {
              this.$q.loading.show();
              this.text = null
              this.getData();
              this.$q.loading.hide();
            }
          });
      },
      toggle(id, state) {
        this.selected[id] = (state === 3);
      },
      change(id) {
        if (this.text === null) {
          this.$q.notify({
            type: 'negative',
            message: 'Isi pesan untuk koperasi'
          })
          return
        }
        this.$q.loading.show();
        this.$http.post('/api/changestatekoperasi', {
          id,
          state: this.selected[id],
          text: this.text
        }, {
          headers: this.$auth.getHeader(),
        })
          .then(() => {
            this.id = null
            this.text = null
            this.getData();
            this.$q.loading.hide();
            this.$swal.fire(
              'Success!',
              'Status Koperasi berhasil diubah',
              'success',
            );
          })
          .catch(() => {
            this.$q.loading.hide();
            this.$swal.fire(
              'Error!',
              'Terjadi kesalahan, refresh(F5)',
              'error',
            );
          });
      },
    },
    created() {
      this.$q.loading.show();
      moment.lang('id')
      this.getData();
    },
  };
</script>
<style scoped>

</style>
