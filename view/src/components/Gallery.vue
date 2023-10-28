<script setup lang="ts">
import { ref } from 'vue'
import { api } from '@/http-api';
import { ImageType } from '@/image-type';
import Image from './Image.vue';

const IMAGE_LIST = ref<ImageType[]>([]);

api.getImageList().then((data) => {
  IMAGE_LIST.value = data;
}).catch(e => {
  console.log(e.message);
});
</script>

<template>
  <div class="container">
    <h3>Gallery</h3>
    <p>Click on an image to download it.</p>
    <div class="gallery">
      <div class="image-grid">
        <Image v-for="image in IMAGE_LIST" :key="image.id" :id="image.id" />
      </div>
    </div>
  </div>
</template>
  
<style scoped>
.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin: 1vh auto;
  text-align: center;
}

.gallery {
  flex-wrap: wrap;
  gap: 10px;
  justify-content: space-between;
}
</style>