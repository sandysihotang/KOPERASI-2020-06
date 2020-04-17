<template>
  <q-page>
    <q-form-builder v-model="fields"/>
    <div class="q-pa-md q-form-container">
      <h4>Form Rendering</h4>
      <div v-for="(field, index) in fields" :key="index">
        <component v-model="fieldData[field.cid]" @input="onInput" v-bind:is="getElement(field)"
                   :label="field.label" :required="field.required"
                   :field_options="field.field_options" :id="field.cid" :cid="field.cid"
                   :ref="field.cid" debounce="500"/>
      </div>
    </div>
  </q-page>
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
      };
    },
    methods: {
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
  };
</script>

<style lang="stylus">
  .q-form-container
    max-width 820px
    margin auto
</style>
