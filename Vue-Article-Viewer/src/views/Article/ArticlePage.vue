<template>
  <div>
    <!-- NavBar component -->
    <NavBar />
    <!-- LeftArticle component -->
    <LeftArticle />
    <!-- Router view with a class of "main" -->
    <router-view class="main"></router-view>
  </div>
</template>

<script>
import NavBar from "@/components/NavBar.vue";
import LeftArticle from "@/components/LeftArticle.vue";
export default {
  name: "VueTestArticlePage",
  components: {
    NavBar,
    LeftArticle,
  },
  data() {
    return {};
  },

  mounted() {},

  methods: {},
};
</script>

<style lang="less" scoped>
.main {
  background-color: RGB(208, 208, 208);
  width: 90vw;
  height: 90vh;
  position: absolute;
  right: 0;
  overflow-y: scroll;
  padding-bottom: 100px;
}
</style>