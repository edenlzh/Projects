<!-- PageWithNavbar component displays a page with a NavBar component and a router-view -->
<template>
  <div class="container">
    <nav>
      <NavBar />
    </nav>
    <router-view />
  </div>
</template>

<!-- Imports the NavBar component -->
<script>
import NavBar from "./NavBar.vue";

export default {
  components: {
    NavBar,
  },
};
</script>
