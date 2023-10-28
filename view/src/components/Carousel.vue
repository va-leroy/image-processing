<script setup lang="ts">
import { ref } from 'vue'
import { api } from '@/http-api';
import { ImageType } from '@/image-type';

const IMAGE_LIST = ref<ImageType[]>([]);
let current = 0;
let index: number;

api.getImageList().then((data) => {
  IMAGE_LIST.value = data;
  index = IMAGE_LIST.value.length;
}).catch(e => {
  console.log(e.message);
});

api.getImage(0).then((data: Blob) => {
  const reader = new window.FileReader();
  reader.readAsDataURL(data);
  reader.onload = async () => {
    if (reader.result as string) {
      await updateDisplay(current, reader.result as string);
    }
  }
}).catch(e => {
  console.log(e.message);
});

function handleCarousel(direction: 'previous' | 'next') {
  if (direction === 'previous') {
    if (current - 1 < 0) {
      current = index - 1;
    } else {
      current--;
    }
  } else {
    if (current + 1 > index - 1) {
      current = 0;
    } else {
      current++;
    }
  }
  api.getImage(current).then((data: Blob) => {
    const reader = new window.FileReader();
    reader.readAsDataURL(data);
    reader.onload = async () => {
      if (reader.result as string) {
        await updateDisplay(current, reader.result as string);
      }
    }
  }).catch(e => {
    console.log(e.message);
  });
}

async function updateDisplay(current: number, result: string) {
  await document.getElementById("display")!.setAttribute("src", result);
  document.getElementById("carousel")!.setAttribute("href", `http://localhost:8080/images/${current}`);
  document.getElementById("carousel")!.setAttribute("download", `image-${current}`);
}
</script>

<template>
  <div class="container">
    <h3>Carousel</h3>
    <p>Click on the buttons to scroll through the server images.</p>
    <div class="carousel-wrapper">
      <button class="arrow-button" @click="() => handleCarousel('previous')">&#8249;</button>
      <a href="" id="carousel" class="image-link"><img src="" alt="" id="display" class="centered-image"></a>
      <button class="arrow-button" @click="() => handleCarousel('next')">&#8250;</button>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 1vh auto;
}

.container p:last-of-type {
  margin-bottom: 5vh;
}

.carousel-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.centered-image {
  display: block;
  width: 50vw;
  margin: 0 auto;
}

.centered-image:hover {
  opacity: 80%;
}

.arrow-button {
  position: absolute;
  top: 50%;
  background-color: #f8f9fa;
  border: 1px solid #ced4da;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 20px;
  transform: translateY(-50%);
  z-index: 1;
}

.arrow-button:first-of-type {
  left: 10px;
}

.arrow-button:last-of-type {
  right: 10px;
}
</style>