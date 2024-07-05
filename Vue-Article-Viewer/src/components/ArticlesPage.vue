<template>
  <div>
    <h1>Articles</h1>
    <!-- List all the articles -->
    <ul>
      <li v-for="article in articles" :key="article.id">
        <!-- Link to each article -->
        <router-link :to="{ name: 'Article', params: { articleId: article.id } }">{{ article.title }}</router-link>
      </li>
    </ul>
    <!-- Display the NewArticleForm component -->
    <NewArticleForm @add-article="addArticle" />
  </div>
</template>

<script>
import NewArticleForm from './NewArticleForm.vue'

export default {
  components: {
    NewArticleForm,
  },
  data() {
    return {
      articles: [
        { id: 1, title: 'Article 1', content: 'This is the content of Article 1.' },
        { id: 2, title: 'Article 2', content: 'This is the content of Article 2.' },
      ],
    }
  },
  methods: {
    // Add a new article to the list
    addArticle(title, content) {
      const newArticle = {
        id: this.articles.length + 1,
        title,
        content,
      }
      this.articles.push(newArticle)
    },
  },
}
</script>

<style scoped>
.article {
  display: grid;
  grid-template-columns: 1fr;
  grid-auto-rows: 1fr;
  gap: 20px;
  padding: 20px;
}

.image {
  grid-row: 1;
  grid-column: 1;
  z-index: 0;
  justify-self: center;
  align-self: center;
  max-width: 100%;
  height: auto;
}

.caption {
  grid-row: 1;
  grid-column: 1;
  z-index: 1;
  justify-self: stretch;
  align-self: end;
  text-align: center;
  background-color: rgba(255, 255, 255, 0.5);
  padding: 20px;
  margin: 0;
  font-style: italic;
}

</style>
