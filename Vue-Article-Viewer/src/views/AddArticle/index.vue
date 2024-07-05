<template>
  <div class="main">
    <main>
      <!-- Add article heading -->
      <h1>Add article</h1>
      <!-- Input field for article title -->
      <div class="form-item">
        <label class="label" for="">Title：</label>
        <input type="text" v-model="form.title" placeholder="Title" />
      </div>
      <!-- Textarea for article content -->
      <div class="form-item">
        <label class="label" for="">Content：</label>
        <textarea v-model="form.content" rows="10"></textarea>
      </div>
      <!-- Add button -->
      <div class="form-item btn">
        <button @click="add">Add</button>
      </div>
    </main>
  </div>
</template>

<script>
import initialArticles from "@/mock/data";
import { toRaw } from "@vue/reactivity";
import store from "@/store";
export default {
  name: "VueTestAddArticle",

  data() {
    return {
      form: {
        content: "",
        title: "",
      },
      initialArticles: [],
    };
  },

  // Initialize initialArticles on component mount
  mounted() {
    this.initialArticles = initialArticles;
  },

  methods: {
    // Method to add new article
    add() {
      let id = this.initialArticles.length;

      // Get existing articles from local storage
      let oldArticle = localStorage.getItem("articles");
      oldArticle = JSON.parse(oldArticle);
      // Set new article ID
      if (oldArticle) {
        this.form.id = id + oldArticle.length + 1;
      } else {
        this.form.id = id + 1;
      }

      // Set article image
      this.form.image = require("../../../public/images/source.gif");
      // Save new article in local storage
      if (oldArticle) {
        oldArticle.push(this.form);
        localStorage.setItem("articles", JSON.stringify(oldArticle));
      } else {
        localStorage.setItem("articles", JSON.stringify([this.form]));
      }
      // Update Vuex store with new article
      if (oldArticle) {
        let temp = toRaw(store.state.article.articles);
        temp.push(oldArticle);
        store.state.article.articles = temp;
      } else {
        let temp = toRaw(store.state.article.articles);
        temp.push(toRaw(this.form));
        store.state.article.articles = temp;
      }
      window.location.href = "/articles/" + this.form.id;
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
    .btn {
      display: flex;
      justify-content: flex-end;
      button {
        width: 100px;
        cursor: pointer;
      }
    }
    .form-item {
      width: 100%;
      display: flex;
      margin-top: 20px;
      .label {
        width: 100px;
        display: block;
        text-align: right;
      }
      input {
        width: 100%;
      }
      textarea {
        width: 100%;
      }
    }
  }
}
</style>