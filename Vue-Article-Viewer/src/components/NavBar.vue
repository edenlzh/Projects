<!-- NavBar component displays a navigation bar with links to Articles and Gallery -->
<template>
  <div :class="$style.navBar">
    <!-- Link to the Articles page, "active" style applied if the current route includes "/articles" -->
    <router-link
      to="/articles/1"
      :class="[
        $style.link,
        $route.path.includes('/articles') ? $style.activeLink : '',
      ]"
    >
      Articles
    </router-link>
    <!-- Link to the Gallery page, "active" style applied if the current route is "/gallery" -->
    <router-link
      to="/gallery"
      :class="[
        $style.link,
        $route.path === '/gallery' ? $style.activeLink : '',
      ]"
    >
      Gallery
    </router-link>
  </div>
</template>

<!-- No script is needed for this component, as it only contains static navigation links -->
<script>
export default {};
</script>

<!-- Styles for the navigation bar -->
<style module>
.navBar {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: stretch;
  box-shadow: 3px 3px 3px rgba(128, 128, 128, 0.5);
  position: relative;
  z-index: 10;
  height: 54px;
}

.link {
  background-color: transparent;
  text-decoration: none;
  color: black;
  padding: 15px;
}

.activeLink {
  background-color: lightblue;
  font-weight: bold;
}
</style>