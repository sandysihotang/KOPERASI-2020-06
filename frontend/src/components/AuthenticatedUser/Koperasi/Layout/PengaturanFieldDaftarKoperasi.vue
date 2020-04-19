<template>
  <div v-if="!isHaveMember">
    <q-page class="q-mt-lg">
      <div class="row">
        <q-btn class="full-width" glossy color="primary" @click="saveForm()">Simpan Form</q-btn>
      </div>
      <div class="row">
        <q-form-builder v-model="fields"/>
      </div>
      <div class="row">
        <div class="q-form-container">
          <h4>Form Rendering</h4>
          <div v-for="(field, index) in fields" :key="index">
            <component v-model="fieldData[field.cid]" @input="onInput" v-bind:is="getElement(field)"
                       :label="field.label" :required="field.required"
                       :field_options="field.field_options" :id="field.cid" :cid="field.cid"
                       :ref="field.cid" debounce="500"/>
          </div>
        </div>
      </div>
    </q-page>
  </div>
  <div v-else>
    <q-card
      class="my-card text-white"
      style="height: 100%; width:100%; background: radial-gradient(circle, #35a2ff 0%, #014a88 100%)"
    >
      <q-card-section>
        <div class="text-h6" align="center">Field sudah ditetapkan</div>
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
          Form pendaftaran untuk anggota baru sudah ditetapkan
        </div>
      </q-card-section>
    </q-card>
  </div>
</template>

<script>
  import QFormBuilder from './CustomField/QFormBuilder.vue';
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
  } from './CustomField/index';

  export default {
    name: 'SampleApp',
    components: {
      QFormBuilder,
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
        isHaveMember: true,
      };
    },
    methods: {
      isHave() {
        this.$q.loading.show();
        this.$http.get('/api/isHaveField', {
          headers: this.$auth.getHeader(),
        })
          .then((data) => {
            this.isHaveMember = data.data;
            this.$q.loading.hide();
          })
          .catch((err) => {
            this.$q.loading.hide();
          });
      },
      saveForm() {
        this.$swal.fire({
          title: 'Anda yakin?',
          text: 'Ingin menyimpan form untuk daftar anggota ini?',
          type: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: 'Ya, Simpan!',
        })
          .then((result) => {
            this.$q.loading.show()
            if (result.value) {
              this.save();
            }
            this.$q.loading.hide()
          });
      },
      save() {
        this.$http.post('/api/saveformregister', { formField: JSON.stringify(this.fields) }, {
          headers: this.$auth.getHeader(),
        })
          .then(() => {
            this.$q.loading.hide();
            this.isHave();
            this.$swal({
              position: 'center',
              type: 'success',
              title: 'Berhasil menyimpan form',
              showConfirmButton: false,
              timer: 1500,
            });
          })
          .catch(() => {
            this.$q.loading.hide();
            this.$swal({
              position: 'center',
              type: 'error',
              title: 'Terjadi Error silahkan refresh kembali (F5)',
              showConfirmButton: false,
              timer: 1500,
            });
          })
      },
      /**
       * When a value is input into the rendered form, echo it to the debug line
       */
      onInput(val, id) {
        // console.debug(`${id}: ` + JSON.stringify(val))
      },
      /**
       * Determine the name of the Element object based on the 'field_type' of the field data object
       */
      getElement(field) {
        const nameParts = field.field_type.split('_');
        for (let i = 0; i < nameParts.length; i++) {
          nameParts[i] = nameParts[i].charAt(0)
            .toUpperCase() + nameParts[i].slice(1);
        }
        return `${nameParts.join('')}Element`;
      },
    },
    watch: {
      fields: {
        handler(val) {
          // console.debug(val)
        },
        deep: true,
      },
    },
    created() {
      this.isHave();
    },
  };
</script>

<style lang="stylus">
  .q-form-container
    max-width 820px
    margin auto

  .my-card {
    width: 100%;
    height: 100%;
    max-height: 400px
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
