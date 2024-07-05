<template>
  <div>
    <NavBar />
    <p>
      Sorry, we couldn't find what you're looking for! Is the path
      <code>{{ this.$router.fullPath }}</code> correct?
    </p>
  </div>
</template>

<script>
import NavBar from "@/components/NavBar.vue";
export default {
  name: "VueTest404",
  components: {
    NavBar,
  },
  data() {
    return {};
  },

  mounted() {},

  methods: {},
};
</script>

<style lang="less" scoped>
</style>