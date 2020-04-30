<template>
  <div class="q-editable-element">
    <div class="overlay" @click="onClick"></div>
    <component v-bind:is="element" :label="value.label" :required="value.required" :field_options="value.field_options" :id="value.id" :cid="value.cid" />
  </div>
</template>

<script>
import TextElement from '../elements/TextElement.vue';
import ParagraphElement from '../elements/ParagraphElement.vue';
import CheckboxesElement from '../elements/CheckboxesElement.vue';
import RadioElement from '../elements/RadioElement.vue';
import DateElement from '../elements/DateElement.vue';
import TimeElement from '../elements/TimeElement.vue';
import DropdownElement from '../elements/DropdownElement.vue';
import EmailElement from '../elements/EmailElement.vue';
import NameElement from '../elements/NameElement.vue';
import SimpleNameElement from '../elements/SimpleNameElement.vue';
import AddressElement from '../elements/AddressElement.vue';
import PhoneElement from '../elements/PhoneElement.vue';
import FileElement from '../elements/FileElement.vue';
import PaymentElement from '../elements/PaymentElement.vue';
import TermsElement from '../elements/TermsElement.vue';
import SectionBreakElement from '../elements/SectionBreakElement.vue';
import PageBreakElement from '../elements/PageBreakElement.vue';

export default {
  name: 'EditableElement',
  components: {
    TextElement, ParagraphElement, CheckboxesElement, RadioElement, DateElement, TimeElement, DropdownElement, EmailElement, NameElement, SimpleNameElement, AddressElement, PhoneElement, FileElement, PaymentElement, TermsElement, SectionBreakElement, PageBreakElement,
  },
  props: ['value'],
  methods: {
    onClick() {
      this.$emit('click', this.value);
    },
  },
  computed: {
    element() {
      const nameParts = this.value.field_type.split('_');
      for (let i = 0; i < nameParts.length; i++) {
        nameParts[i] = nameParts[i].charAt(0).toUpperCase() + nameParts[i].slice(1);
      }
      return `${nameParts.join('')}Element`;
    },
  },
};
</script>

<style scoped>
  .q-editable-element {
    position: relative;
    padding: 5px;
  }

  .overlay {
    position: absolute;
    z-index: 2;
    cursor: pointer;
    width: 100%;
    height: 100%;
  }
</style>
