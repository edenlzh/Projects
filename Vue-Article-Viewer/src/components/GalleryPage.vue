<!-- GalleryPage component displays a gallery of images from the provided "articles" prop -->
<template>
  <main :class="$style.gallery">
    <!-- Iterates through the array of articles and creates a div for each article's image -->
    <div
      v-for="article in articles"
      :key="article.id"
      :class="['box', $style.imageBox]"
    >
      <!-- Image of the article -->
      <img :src="article.image" />
      <!-- Caption for the article image -->
      <p :class="$style.caption">
        <span>From article: </span>
        <!-- Link to the corresponding article -->
        <router-link :to="'/articles/' + article.id">{{ article.title }}</router-link>
      </p>
    </div>
  </main>
</template>

<!-- "articles" prop contains an array of article objects that includes image URLs -->
<script>
export default {
  props: {
    articles: {
      type: Array,
      required: true,
    },
  },
};
</script>

<!-- <style>
.gallery {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-auto-rows: 1fr;
  gap: 20px;
  padding: 20px;
}

.imageBox {
  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: 1fr;
}

.imageBox img {
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
 -->