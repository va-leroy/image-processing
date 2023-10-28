<script setup lang="ts">
import { defineProps } from 'vue';
import { api } from '@/http-api';

const PROPS = defineProps<{ id: number }>()

api.getImage(PROPS.id).then((data: Blob) => {
  const reader = new window.FileReader();
  reader.readAsDataURL(data);
  reader.onload = () => {
    const figure = document.getElementById("figure");
    if (figure !== null) {
      const element = document.createElement("img");
      element.setAttribute("id", PROPS.id.toString());

      let a = document.createElement("a");
      a.setAttribute("href", "http://localhost:8080/images/" + PROPS.id);
      a.setAttribute("download", "image-" + PROPS.id);
      a.appendChild(element);
      figure.appendChild(a);

      if (reader.result as string) {
        element.setAttribute("src", (reader.result as string));
      }
    }
  };
}).catch(e => {
  console.log(e.message);
});
</script>

<template>
  <figure id="figure"></figure>
</template>

<style>
#figure img {
  max-width: 30vw;
  display: block;
  margin: 10px auto;
}

#figure img:hover {
  opacity: 80%;
}
</style>