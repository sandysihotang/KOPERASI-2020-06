<template>
  <div>
    <q-card>
      <q-card-section v-if="show">
        <center>
          <p class="text-h6">Anggota Aktif Koperasi</p>
          <p>Koperasi {{ res.namaKoperasi }}</p>
        </center>
      </q-card-section>
      <q-separator/>
      <q-card-section v-if="show">
        <center>
          <table class="q-table">
            <tr>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td><b>Anggota Aktif</b></td>
              <td>{{ res.totalAnggota }}</td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
            </tr>
          </table>
        </center>
      </q-card-section>
      <q-card-section>
        <q-table
          :columns="columns"
          :data="data"/>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn color="primary" v-if="show" icon="print" label="Export Excel" @click="download"/>
        <q-btn label="Close" color="green" v-close-popup/>
      </q-card-actions>
    </q-card>
  </div>
</template>

<script>
  import moment from 'moment'

  export default {
    data() {
      return {
        show: false,
        dateFrom: null,
        dateTo: null,
        res: null,
        columns: [],
        data: []
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
      getDate(date) {
        moment.lang('id')
        return moment(date)
          .format('dddd, Do MMMM YYYY')
      },
      downloadFile(response, filename) {
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([response.data], { type: 'application/excel' })

        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        if (window.navigator && window.navigator.msSaveOrOpenBlob) {
          window.navigator.msSaveOrOpenBlob(newBlob)
          return
        }

        // For other browsers:
        // Create a link pointing to the ObjectURL containing the blob.
        const data = window.URL.createObjectURL(newBlob)
        const link = document.createElement('a')
        link.href = data
        link.download = `${filename}.xlsx`
        link.click()
        setTimeout(() => {
          // For Firefox it is necessary to delay revoking the ObjectURL
          window.URL.revokeObjectURL(data)
        }, 100)
        this.$swal({
          position: 'center',
          type: 'success',
          width: 300,
          title: 'Berhasil Mengexport Data',
          showConfirmButton: false,
          timer: 1500
        })
      },
      download() {
        this.$q.loading.show()
        this.$http.get('/api/getanggotalaporan/download', {
          headers: this.$auth.getHeader(),
          responseType: 'arraybuffer'
        })
          .then((res) => {
            this.downloadFile(res, 'AnggotaKoperasi')
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.notify({
              type: 'negative',
              message: `Terjadi kesalahan refresh (F5)`
            })
            this.$q.loading.hide()
          })
      },
      showLaporan() {
        this.$http.get('/api/getanggotalaporan', {
          headers: this.$auth.getHeader()
        })
          .then((res) => {
            this.res = res.data
            const columns = JSON.parse(res.data.pengaturanField);
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
            this.data = []
            const data = res.data.anggota
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
            this.show = true
            this.$q.loading.hide()
          })
          .catch(() => {
            this.$q.notify({
              type: 'negative',
              message: `Terjadi kesalahan refresh (F5)`
            })
            this.$q.loading.hide()
          })
      }
    },
    created() {
      this.$q.loading.show()
      this.showLaporan()
    }
  }
</script>

<style scoped>

</style>
