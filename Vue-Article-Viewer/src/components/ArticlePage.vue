<template>
  <!-- Display the article if it exists -->
  <div v-if="article">
    <h2>{{ article.title }}</h2>
    <p>{{ article.content }}</p>
    <!-- Link to go back to the articles list -->
    <router-link to="/articles">Back to articles</router-link>
  </div>
  <!-- Display a message if the article doesn't exist -->
  <div v-else>
    <p>Sorry, we couldn't find the article you're looking for!</p>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

// Mock articles data
const mockArticles = [
      { id: 1, title: 'Article 1', content: 'This is the content of Article 1.' },
      { id: 2, title: 'Article 2', content: 'This is the content of Article 2.' },
    ]

export default {
  setup() {
    const article = ref(null)
    const route = useRoute()

    // Fetch article when component is mounted
    onMounted(() => {
      const id = route.params.articleId
      const foundArticle = mockArticles.find((a) => a.id == id)
      if (foundArticle) {
        article.value = foundArticle
      }
    })


    return { article }
  },
}
</script>

<style>
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
