<template>
  <div class="q-pa-md">
    <div class="row">
      <div class="col-md-2"/>
      <div class="col-md-8">
        <q-stepper
          v-model="step"
          ref="stepper"
          animated
          active-color="purple"
        >
          <q-step
            :name="1"
            prefix="1"
            title="Basic Pertanyaan"
          >
            <q-input filled square clearable type="text" v-model="form.name"
                     :rules="[val => !!val || 'Field is required']"
                     label="Nama Koperasi">
              <template v-slot:prepend>
                <q-icon name="fa fa-id-card"/>
              </template>
            </q-input>
            <q-input filled square clearable type="text" v-model="form.pendiri"
                     :rules="[val => !!val || 'Field is required']"
                     label="Nama Pendiri Koperasi">
              <template v-slot:prepend>
                <q-icon name="person"/>
              </template>
            </q-input>
            <q-input filled square clearable type="text" v-model="form.alamat"
                     :rules="[val => !!val || 'Field is required']"
                     label="Alamat Lengkap Koperasi">
              <template v-slot:prepend>
                <q-icon name="place"/>
              </template>
            </q-input>
            <q-input filled square clearable type="text" v-model="form.email"
                     :rules="[val => !!val || 'Field is required']"
                     label="Email Koperasi">
              <template v-slot:prepend>
                <q-icon name="email"/>
              </template>
            </q-input>
          </q-step>

          <q-step
            :name="2"
            prefix="2"
            title="Create an ad group"
            caption="Optional"
          >
            <q-input filled square v-model="form.date"
                     :rules="[val => !!val || 'Field is required']"
                     label="Tahun Berdiri Koperasi">
              <template v-slot:prepend>
                <q-icon name="event"/>
              </template>
              <template v-slot:append>
                <q-icon name="access_time" class="cursor-pointer">
                  <q-popup-proxy transition-show="scale" transition-hide="scale">
                    <q-date v-model="form.date"/>
                  </q-popup-proxy>
                </q-icon>
              </template>
            </q-input>
            <q-input filled square clearable type="text" v-model="form.izin"
                     :rules="[val => !!val || 'Field is required']"
                     label="No Izin Mendirikan Koperasi">
              <template v-slot:prepend>
                <q-icon name="lock"/>
              </template>
            </q-input>
            <q-select filled square ref="input" clearable type="text" v-model="form.jenis"
                      :options="options" :rules="['date', val => !!val || 'Field is required']"
                      label="Jenis Koperasi">
              <template v-slot:prepend>
                <q-icon name="list"/>
              </template>
            </q-select>
          </q-step>

          <q-step
            :name="3"
            prefix="3"
            title="Create an ad"
          >
            <div class="row">
              <div class="col-md-2"/>
              <div class="col-md-8">
                <q-uploader
                  :factory="factory"
                  label="Icon Koperasi(Jika Ada)"
                  style="width: 100%"
                  auto-upload
                />
              </div>
              <div class="col-md-2"/>
            </div>
          </q-step>

          <template v-slot:navigation>
            <q-stepper-navigation>
              <q-btn color="deep-orange"
                     v-if="step === 3" label="Finish" @click="createKoperasi"/>
              <q-btn @click="$refs.stepper.next()" color="deep-orange"
                     v-else label="Continue"/>
              <q-btn v-if="step > 1" flat color="deep-orange" @click="$refs.stepper.previous()"
                     label="Back" class="q-ml-sm"/>
            </q-stepper-navigation>
          </template>
        </q-stepper>
      </div>
      <div class="col-md-2"/>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        form: {
          date: '',
          name: '',
          pendiri: '',
          alamat: '',
          izin: '',
          jenis: '',
          email: '',
          image: null,
        },
        step: 1,
        options: ['Koperasi Serba Usaha (KSU)', 'Koperasi Simpan Pinjam (KSP)'],
      };
    },
    methods: {
      factory(files) {
        this.form.image = files;
      },
      createKoperasi() {
        this.$q.loading.show();
        if (this.ck()) {
          const formData = this.useFormData();
          this.$http.post('/api/createkoperasi', formData, {
            headers: this.$auth.getHeader(),
          })
            .then(() => {
              this.$q.loading.hide();
              this.$http.get('/api/currentuser', {
                headers: this.$auth.getHeader(),
              })
                .then((res) => {
                  this.$auth.setHaveKoperasi(res.data.haveKoperasi);
                  window.location.href = '/';
                })
                .catch((err) => {
                  this.$swal({
                    position: 'center',
                    type: 'error',
                    title: 'Ada gangguan jaringan silahkan refresh (F5)',
                    showConfirmButton: false,
                    timer: 1500,
                  });
                  this.$q.loading.hide();
                });
            })
            .catch(() => {
              this.$q.loading.hide();
              this.$swal({
                position: 'center',
                type: 'error',
                title: 'Periksa kembali data yang anda isi atau refresh halaman (F5)',
                showConfirmButton: false,
                timer: 1500,
              });
            });
        } else {
          this.$swal({
            position: 'center',
            type: 'error',
            title: 'Lengkapi data Untuk kebutuhan Diskoperindag',
            showConfirmButton: false,
            timer: 1500,
          });
        }
      },
      useFormData() {
        const formData = new FormData();
        if (this.form.image) formData.append('image', this.form.image[0]);
        formData.append('date', this.form.date);
        formData.append('name', this.form.name);
        formData.append('pendiri', this.form.pendiri);
        formData.append('alamat', this.form.alamat);
        formData.append('jenis', this.form.jenis);
        formData.append('izin', this.form.izin);
        formData.append('email', this.form.email);
        return formData;
      },
      ck() {
        return this.form.date !== '' && this.form.name !== '' && this.form.pendiri !== '' && this.form.alamat !== '' && this.form.izin !== '' && this.form.jenis !== '' && this.form.email !== '';
      },
    },
    created() {
    },
  };
</script>

<style scoped>

</style>
