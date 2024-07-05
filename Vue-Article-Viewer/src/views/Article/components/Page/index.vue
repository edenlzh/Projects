<template>
  <div class="main">
    <main>
      <!-- Display article content when available -->
      <div v-if="article.length != 0">
        <h1>{{ article.title }}</h1>
        <div class="content">
          <div class="img">
            <img :src="article.image" alt="" />
          </div>
          <p>
            {{ article.content }}
          </p>
        </div>
      </div>
      <!-- Display message when the article is not found -->
      <div v-else>
        <p>Sorry, we couldn't find the article you're looking for!</p>
      </div>
    </main>
  </div>
</template>

<script>
import initialArticles from "@/mock/data";
import { mapState } from "vuex";
import { toRaw } from "@vue/reactivity";
export default {
  name: "VueTestIndex",
  watch: {
    $route(to, from) {
      if (to.path != from.path) {
        window.location.reload();
      }
    },
  },
  data() {
    return {
      initialArticles: [],
      article: [],
    };
  },
  computed: {
    ...mapState("article", ["articles"]),
  },
  created() {
    this.initialArticles = initialArticles;
    this.getArticle();
  },

  methods: {
    // Get article based on route parameter
    getArticle() {
      const articles = toRaw(this.articles);
      console.log(articles);
      articles.some((item) => {
        if (item.id == this.$route.params.id) {
          this.article = item;
          return true;
        }
      });
    },
  },
};
</script>

<style lang="less" scoped>
.main {
  padding: 20px;
  box-sizing: border-box;
  main {
    background-color: #fff;
    height: 100vh;
    width: 100%;
    box-shadow: 3px 3px 3px rgba(128, 128, 128, 0.5);
    padding: 20px;
    box-sizing: border-box;
    h1 {
      margin: 0;
    }
    .content {
      .img {
        width: 400px;
        height: 360px;
        float: left;
        margin-right: 20px;
        margin-bottom: 10px;
        img {
          width: 100%;
          height: 100%;
        }
        p {
          float: right;
        }
      }
    }
  }
}
</style>