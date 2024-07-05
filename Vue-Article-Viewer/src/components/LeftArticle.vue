<!-- LeftArticle component displays a list of articles in a side menu -->
<template>
  <div class="menu">
    <div class="title">Articles</div>
    <div class="article">
      <ul>
        <!-- Iterates through the array of articles and creates a list item for each -->
        <li
          v-for="item in articles"
          @click="toArticle(item.id)"
          :key="item.id"
          :class="{ active: active == item.id }"
        >
          <!-- Link to the corresponding article -->
          <router-link :to="'/articles/' + item.id">{{
            item.title
          }}</router-link>
        </li>
      </ul>
       <!-- Button to create a new article -->
       <span class="add" @click="$router.push('/addArticle')">New article</span>
    </div>
  </div>
</template>

<!-- Component uses Vuex to manage the state of the "articles" list -->
<script>
import { mapState } from "vuex";

export default {
  name: "VueTestLeftArticle",

  data() {
    return {
      active: this.$route.params.id,
      initialArticles: [],
    };
  },
  computed: {
    ...mapState("article", ["articles"]),
  },
  mounted() {},

  methods: {
    toArticle(id) {
      this.active = id;
      this.$router.push("/articles/" + id);
    },
  },
};
</script>

<!-- Styles for the side menu -->
<style lang="less" scoped>
.menu {
  position: fixed;
  top: 54px;
  left: 0;
  z-index: 11;
  width: 10vw;
  height: 100%;
  background-color: #fff;
  box-shadow: 3px 3px 3px rgba(128, 128, 128, 0.5);
  padding: 20px 0;
  box-sizing: border-box;
  text-align: center;
  .title {
    line-height: 40px;
    font-size: 20px;
    font-weight: 700;
  }
  .article {
    .add {
      height: 64px;
      line-height: 64px;
      text-align: center;
      display: block;
      background-color: RGB(144, 237, 145);
      font-weight: 700;
      margin-top: 20px;
      cursor: pointer;
    }
    ul {
      list-style: none;
      margin: 0;
      padding: 0;
      border-bottom: 1px solid #eee;
      .active {
        background-color: RGB(173, 213, 224) !important;
        font-weight: 700 !important;
      }
      li {
        padding: 20px 0;
        cursor: pointer;
        a {
          text-decoration: none;
          color: #000;
        }
      }
    }
  }
}
</style>