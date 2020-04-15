<template>
  <div class="row q-col-gutter-sm q-form-text q-form-builder-element">
    <div class="col">
      <q-input v-model="innerValue" :label="getLabel" :debounce="debounce" :rules="getRules()" :ref="id" />
      <p v-if="hint" class="text-caption" v-html="hint"></p>
    </div>
  </div>
</template>

<script>
import { QInput } from 'quasar'
import FormElement from './FormElement'

export default {
  name: 'TextElement',
  extends: FormElement,
  components: { QInput }
}
</script>
