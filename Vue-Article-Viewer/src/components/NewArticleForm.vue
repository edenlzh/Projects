<!-- NewArticleForm component displays a form for adding a new article -->
<template>
  <div>
    <h2>Add article</h2>
    <div :class="$style.form">
      <div :class="$style.formRow">
      <!-- Title input field -->
      <label>Title:</label>
        <input type="text" v-model="title" />
      </div>
      <div :class="$style.formRow">
      <!-- Content input field -->
      <label>Content:</label>
        <textarea rows="5" v-model="content" />
      </div>
      <div :class="[$style.formRow, $style.rowReverse]">
      <!-- Submit button -->
      <button :style="{ flexBasis: '100px', flexGrow: 0 }" @click="addArticle">
          Add
        </button>
      </div>
    </div>
  </div>
</template>

<!-- Component script handles form submission for creating or editing articles -->
<script>
export default {
  props: {
    onAddArticle: {
      type: Function,
      required: true,
    },
  },
  data() {
    return {
      title: '',
      content: '',
    };
  },
  methods: {
    addArticle() {
      this.onAddArticle(this.title, this.content);
      this.title = '';
      this.content = '';
    },
  },
};
</script>

<!-- Styles for the form -->
<style module>
.form .formRow {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  margin-bottom: 10px;
}

.form .formRow > * {
  flex-grow: 1;
}

.form .formRow > label {
  text-align: right;
  margin-right: 10px;
  flex-basis: 100px;
  flex-grow: 0;
}

.rowReverse {
  flex-direction: row-reverse;
}
</style>
