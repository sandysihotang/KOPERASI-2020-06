<template>
  <div class="row q-col-gutter-sm q-form-phone q-form-builder-element">
    <div class="col-12 col-md-6 col-lg-3">
      <q-input v-model="innerValue" type="tel" :label="getLabel" mask="(###) ### - ####" unmasked-value :debounce="debounce" :rules="getRules()" :ref="id">
        <template v-slot:append>
          <q-icon name="phone" />
        </template>
      </q-input>
      <p v-if="hint" class="text-caption" v-html="hint"></p>
    </div>
  </div>
</template>

<script>
import { QInput, QIcon } from 'quasar'
import FormElement from './FormElement'

export default {
  name: 'PhoneElement',
  extends: FormElement,
  components: { QInput, QIcon }
}
</script>
