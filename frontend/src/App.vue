<template>
  <div>
    <q-btn color="blue" label="S"/>
    <div>
      <q-spinner-audio
        color="primary"
        size="2em"
      />
    </div>
  </div>
</template>

<script>

export default {
  name: 'LayoutDefault',


  data() {
    return {
      leftDrawerOpen: this.$q.platform.is.desktop,
    };
  },
};
</script>

<style>
</style>
