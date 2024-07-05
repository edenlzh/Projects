<template>
  <div>
    <!-- Display the PageNotFound component if the type is 'pageNotFound' -->
    <PageNotFound v-if="type === 'pageNotFound'" />
    <!-- Display the ArticleNotFound component if the type is 'articleNotFound' -->
    <ArticleNotFound v-else-if="type === 'articleNotFound'" />
  </div>
</template>

<!-- "type" prop is used to determine which error page should be displayed -->
<script>
export default {
  props: {
    type: {
      type: String,
      required: true,
    },
  },
  components: {
    PageNotFound: {
      props: {
        pathname: {
          type: String,
          required: true,
        },
      },
      // Message to be displayed if the page is not found
      template: `
        <p>Sorry, we couldn't find what you're looking for! Is the path <code>{{ pathname }}</code> correct?</p>
      `,
    },
    ArticleNotFound: {
      template: `
        <p>Sorry, we couldn't find the article you're looking for!</p>
      `,
    },
  },
};
</script>
