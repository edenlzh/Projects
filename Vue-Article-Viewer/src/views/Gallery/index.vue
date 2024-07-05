<template>
  <div>
    <!-- NavBar component -->
    <NavBar />
    <main>
      <div class="items">
        <!-- Loop through articles and display their images -->
        <div class="item" v-for="item in articles" :key="item.id">
          <div class="img">
            <img :src="item.image" alt="" />
          </div>
          <p>
            From article:
            <!-- Create a link to the corresponding article -->
            <router-link :to="'/articles/' + item.id">{{
              item.title
            }}</router-link>
          </p>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import initialArticles from "@/mock/data";
import NavBar from "@/components/NavBar.vue";
import { mapState } from "vuex";
export default {
  name: "VueTestIndex",
  components: {
    NavBar,
  },
  data() {
    return {
      initialArticles: [],
    };
  },
  computed: {
    ...mapState("article", ["articles"]),
  },
  mounted() {
    this.initialArticles = initialArticles;
  },

  methods: {},
};
</script>

<style lang="less" scoped>
main {
  background-color: RGB(209, 209, 209);
  height: 100vh;
  overflow-y: scroll;
  padding-bottom: 100px;
  box-sizing: border-box;

  .items {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
    padding: 20px;
    box-sizing: border-box;
    .item {
      height: 500px;
      background-color: #fff;

      p {
        display: block;
        height: 10%;
        text-align: center;
      }
      .img {
        height: 90%;
        padding: 20px;
        box-sizing: border-box;
        img {
          width: 100%;
          height: 100%;
        }
      }
    }
  }
}
</style>