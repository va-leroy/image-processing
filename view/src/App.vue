<script setup lang="ts">
</script>

<template>
  <div id="app">
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup"
        aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse justify-content-center" id="navbarNavAltMarkup">
        <div class="navbar-nav">
          <router-link :to="{ path: '/' }" class="nav-item nav-link"
            :class="{ 'active': $route.path === '/' }">Home</router-link>
          <router-link :to="{ path: '/gallery' }" class="nav-item nav-link"
            :class="{ 'active': $route.path === '/gallery' }">Gallery</router-link>
          <router-link :to="{ path: '/carousel' }" class="nav-item nav-link"
            :class="{ 'active': $route.path === '/carousel' }">Carousel</router-link>
          <router-link :to="{ path: '/upload' }" class="nav-item nav-link"
            :class="{ 'active': $route.path === '/upload' }">Upload</router-link>
        </div>
      </div>
    </nav>
    <div>
      <router-view />
    </div>
  </div>
</template>

<style>
.active {
  background-color: #0d6efd !important;
  color: white !important;
}

.navbar-nav a {
  text-decoration: none !important;
  border-radius: 5px;
  margin: 0 5px;
  transition: background-color 0.3s, color 0.3s;
}

.navbar-nav a:hover {
  background-color: #0d6efd;
  color: white;
  transition: background-color 0.3s, color 0.3s;
}

#app {
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
}
</style>