<template>
  <div class="q-pa-md">
    <q-stepper
      v-model="step"
      ref="stepper"
      alternative-labels
      color="primary"
      animated
    >
      <q-step
        :name="1"
        title="Kelengkapan data di koperasi"
        icon="create_new_folder"
        :done="step > 1"
      >
        <div v-for="(field, index) in fields" :key="index">
          <component v-model="fieldData[field.cid]" @input="onInput" v-bind:is="getElement(field)"
                     :label="field.label" :required="field.required"
                     :field_options="field.field_options" :id="field.cid" :cid="field.cid"
                     :ref="field.cid" debounce="500"/>
        </div>
      </q-step>
      <q-step
        :name="2"
        title="Kelengkapan Akun"
        icon="settings"
        :done="step > 2"
      >
        <q-input square clearable v-model="form.username" type="username"
                 label="Username">
          <template v-slot:prepend>
            <q-icon name="person"/>
          </template>
        </q-input>
        <q-input square clearable v-model="form.password" type="password"
                 label="Password">
          <template v-slot:prepend>
            <q-icon name="lock"/>
          </template>
        </q-input>
        <q-input square clearable v-model="form.email" type="email"
                 label="Email">
          <template v-slot:prepend>
            <q-icon name="email"/>
          </template>
        </q-input>
      </q-step>
      <template v-slot:navigation>
        <q-stepper-navigation>
          <q-stepper-navigation>
            <q-btn v-if="step===2" @click="save" unelevated
                   size="lg"
                   color="purple-4" class="full-width text-white" label="Daftarkan Anggota">
            </q-btn>
            <q-btn v-else @click="$refs.stepper.next()" unelevated
                   size="lg"
                   color="purple-4" class="full-width text-white" label="Selanjutnya">
            </q-btn>
            <q-btn v-if="step > 1" flat color="primary"
                   @click="$refs.stepper.previous()" label="Kembali" class="q-ml-sm"/>
          </q-stepper-navigation>
        </q-stepper-navigation>
      </template>
    </q-stepper>
  </div>
</template>

<script>
  import {
    TextElement,
    ParagraphElement,
    CheckboxesElement,
    RadioElement,
    DateElement,
    TimeElement,
    DropdownElement,
    EmailElement,
    NameElement,
    SimpleNameElement,
    AddressElement,
    PhoneElement,
    FileElement,
    PaymentElement,
    TermsElement,
    PageBreakElement,
    SectionBreakElement,
  } from './CustomField';

  export default {
    components: {
      TextElement,
      ParagraphElement,
      CheckboxesElement,
      RadioElement,
      DateElement,
      TimeElement,
      DropdownElement,
      EmailElement,
      NameElement,
      SimpleNameElement,
      AddressElement,
      PhoneElement,
      FileElement,
      PaymentElement,
      TermsElement,
      PageBreakElement,
      SectionBreakElement,
    },
    data() {
      return {
        fields: [],
        fieldData: [],
        step: 1,
        form: {
          username: '',
          password: '',
          email: '',
        },
      }
    },
    methods: {
      save() {
        this.$q.loading.show();
        for (let i = 0; i < this.fields.length; i++) {
          if (this.fieldData[this.fields[i].cid] === undefined) {
            this.$q.loading.hide()
            this.showAlert('Lengkapi Data Koperasi', 'error')
            return
          }
        }
        const map = []
        for (let i = 0; i < this.fields.length; i++) {
          map.push({
            uid: this.fields[i].cid,
            value: this.fieldData[this.fields[i].cid]
          })
          this.form.fieldData = JSON.stringify(map);
        }
        this.$http.post('/api/saveanggota', this.form, {
          headers: this.$auth.getHeader(),
        })
          .then(() => {
            this.$q.loading.hide()
            this.showAlert('Data Anggota berhasil didaftarkan', 'success')
            this.$router.push('/anggotakoperasi')
          })
          .catch((error) => {
            this.$q.loading.hide()
            this.showAlert(error.response.data.err, 'error')
          })
      },
      onInput(val, id) {
        // console.debug(`${id}: ${JSON.stringify(val)}`)
      },
      getElement(field) {
        const nameParts = field.field_type.split('_');
        for (let i = 0; i < nameParts.length; i++) {
          nameParts[i] = nameParts[i].charAt(0)
            .toUpperCase() + nameParts[i].slice(1);
        }
        return `${nameParts.join('')}Element`;
      },
      showAlert($title, $status) {
        this.$swal({
          position: 'center',
          type: $status,
          title: $title,
          showConfirmButton: false,
          timer: 1500,
        });
      },
      loadColumn() {
        this.$http.get('/api/getcolumnmember', {
          headers: this.$auth.getHeader(),
        })
          .then((res) => {
            this.fields = res.data;
            this.$q.loading.hide();
          })
          .catch((err) => {
            this.$q.loading.hide();
          });
      },
    },
    created() {
      this.loadColumn()
    }
  }
</script>

<style scoped>

</style>
