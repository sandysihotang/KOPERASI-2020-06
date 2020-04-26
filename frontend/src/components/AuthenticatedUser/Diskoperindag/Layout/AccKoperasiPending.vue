<template>
  <div class="q-pa-md">
    <q-table
      title="Account yang pending"
      :data="data"
      :columns="columns"
      row-key="name"
      :filter="filter"
    >
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td key="name" :props="props">
            {{ `${props.row.userDetail.firstName} ${props.row.userDetail.lastName}` }}
          </q-td>
          <q-td key="username" :props="props">
            {{ props.row.username }}
          </q-td>
          <q-td key="address" :props="props">
            {{ props.row.userDetail.address }}
          </q-td>
          <q-td key="email" :props="props">
            {{ props.row.email }}
          </q-td>
          <q-td key="aksi" :props="props">
            <div class="row">
              <div class="col">
                <!--                <q-btn class="glossy" @click="terima(props.row.id)" round color="primary" icon="check" size="10px">-->
                <!--                  <q-tooltip content-class="bg-accent">Terima</q-tooltip>-->
                <!--                </q-btn>-->
              </div>
              <div class="col">
                <q-btn color="primary" label="view" @click="viewDetail(props.row.id)"/>
              </div>
              <div class="col">
                <!--                <q-btn class="glossy" @click="tolak(props.row.id)" round color="deep-orange" icon="close" size="10px">-->
                <!--                  <q-tooltip content-class="bg-accent">Tolak</q-tooltip>-->
                <!--                </q-btn>-->
              </div>
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
  </div>
</template>

<script>

  export default {
    data() {
      return {
        filter: '',
        columns: [
          {
            name: 'name',
            label: 'Nama Lengkap',
            align: 'center',
            field: 'name',
            sortable: true,
          },
          {
            name: 'username',
            label: 'Username',
            align: 'center',
            field: 'username',
            sortable: true,
          },
          {
            name: 'address',
            align: 'center',
            label: 'Alamat',
            field: 'address',
            sortable: true,
          },
          {
            name: 'email',
            align: 'center',
            label: 'Email',
            field: 'email',
            sortable: true,
          },
          {
            name: 'aksi',
            align: 'center',
            label: 'Plihan',
            field: 'aksi',
          },
        ],
        data: [],
      };
    },
    methods: {
      viewDetail(id) {
        this.$router.push(`/detail/${id}`);
      },
      getData() {
        this.$q.loading.show();
        this.$http.get('/api/getnonauthenticateduser', {
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
      terima(id) {
        this.$q.loading.show();
        this.$http.post('/api/terimaacc', { id }, {
          headers: this.$auth.getHeader(),
        })
          .then(() => {
            this.getData();
            this.$q.loading.hide();
            this.$swal({
              position: 'center',
              type: 'success',
              title: 'Akun berhasil diterima',
              showConfirmButton: false,
              timer: 1500,
            });
          })
          .catch(() => {
            this.getData();
            this.$q.loading.hide();
            this.$swal({
              position: 'center',
              type: 'error',
              title: 'Telah terjadi Error silahkan refreh (F5)',
              showConfirmButton: false,
              timer: 1500,
            });
          });
      },
      tolak(id) {
      },
    },
    created() {
      this.getData();
    },
  };
</script>

<style scoped>

</style>
