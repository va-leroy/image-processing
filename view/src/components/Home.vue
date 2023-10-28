<script setup lang="ts">
import { ref } from 'vue';
import { api } from '@/http-api';
import { ImageType } from '@/image-type';

const SELECTED_ID = ref(-1);
const SELECTED_ID_1 = ref(-1);
const IMAGE_LIST = ref<ImageType[]>([]);

const BRIGHTNESS = { "min": -255, "max": 255 };
const HUE = { "min": 0, "max": 360 };
const MEAN = { "min": 1, "max": 100 };
const NOISE = { "min": 0, "max": 255 };
const GRAIN = { "min": 0, "max": 255 };

api.getImageList().then((data) => {
  IMAGE_LIST.value = data;
}).catch(e => {
  console.log(e.message);
});

function updateParameters() {
  const select = document.getElementById("algorithms") as HTMLSelectElement;
  const identifier = select?.options[select.selectedIndex].id;
  switch (identifier) {
    case "brightness":
      document.getElementById("label")!.style.visibility = "visible";
      document.getElementById("parameter")!.removeAttribute("disabled");
      document.getElementById("parameter")!.setAttribute("min", BRIGHTNESS.min.toString());
      document.getElementById("parameter")!.setAttribute("max", BRIGHTNESS.max.toString());
      break;
    case "hue":
      document.getElementById("label")!.style.visibility = "visible";
      document.getElementById("parameter")!.removeAttribute("disabled");
      document.getElementById("parameter")!.setAttribute("min", HUE.min.toString());
      document.getElementById("parameter")!.setAttribute("max", HUE.max.toString());
      break;
    case "mean":
      document.getElementById("label")!.style.visibility = "visible";
      document.getElementById("parameter")!.removeAttribute("disabled");
      document.getElementById("parameter")!.setAttribute("min", MEAN.min.toString());
      document.getElementById("parameter")!.setAttribute("max", MEAN.max.toString());
      break;
    case "noise":
      document.getElementById("label")!.style.visibility = "visible";
      document.getElementById("parameter")!.removeAttribute("disabled");
      document.getElementById("parameter")!.setAttribute("min", NOISE.min.toString());
      document.getElementById("parameter")!.setAttribute("max", NOISE.max.toString());
      break;
    case "grain":
      document.getElementById("label")!.style.visibility = "visible";
      document.getElementById("parameter")!.removeAttribute("disabled");
      document.getElementById("parameter")!.setAttribute("min", GRAIN.min.toString());
      document.getElementById("parameter")!.setAttribute("max", GRAIN.max.toString());
      break;
    default:
      document.getElementById("label")!.textContent = "No parameters required";
      document.getElementById("parameter")!.setAttribute("disabled", "true");
      (document.getElementById("parameter") as HTMLInputElement)!.value = "";
      break;
  }
}

function updateDisplay() {
  api.getImageListJSON().then((response: JSON) => {
    const body = JSON.parse(JSON.stringify(response));
    document.getElementById("identifier")!.textContent = body[SELECTED_ID.value].id;
    document.getElementById("name")!.textContent = body[SELECTED_ID.value].name;
    document.getElementById("type")!.textContent = body[SELECTED_ID.value].type;
    document.getElementById("size")!.textContent = body[SELECTED_ID.value].size;
    document.getElementById("clickable")!.setAttribute("href", "http://localhost:8080/images/" + body[SELECTED_ID.value].id);
    document.getElementById("clickable")!.setAttribute("download", "image-" + body[SELECTED_ID.value].id);
  });

  if (SELECTED_ID.value >= 0) {
    api.getImage(SELECTED_ID.value).then((data: Blob) => {
      const reader = new window.FileReader();
      reader.readAsDataURL(data);
      reader.onload = async () => {
        if (reader.result as string) {
          await document.getElementById("display")!.setAttribute("src", (reader.result as string));
        }
      }
    }).catch((e) => {
      console.log(e.message);
    });
  }
}

function processImage() {
  document.getElementById("information")!.textContent = "";
  if (SELECTED_ID.value >= 0 && SELECTED_ID_1.value != null) {
    const select = document.getElementById("algorithms") as HTMLSelectElement;
    const algorithm = select?.options[select.selectedIndex].id;

    let param = document.getElementById("parameter") as HTMLSelectElement;
    param.value = param.value == null || param.value == "" ? "0" : param.value;

    document.getElementById("information")!.style.color = "orange";
    document.getElementById("information")!.textContent = "Loading...";

    api.getProcessedImage(SELECTED_ID.value, algorithm, param.value).then((data: Blob) => {
      const reader = new window.FileReader();
      reader.readAsDataURL(data);

      reader.onload = async () => {
        if (reader.result as string) {
          await document.getElementById("display")!.setAttribute("src", (reader.result as string));
          document.getElementById("clickable")!.setAttribute("href", "http://localhost:8080/images/" + SELECTED_ID.value + "/" + algorithm + "/" + param.value);
          document.getElementById("clickable")!.setAttribute("download", "image-" + SELECTED_ID.value + "-" + algorithm);
          (document.getElementById("parameter") as HTMLInputElement)!.value = "";
          document.getElementById("information")!.style.color = "green";
          document.getElementById("information")!.textContent = "Successfully processed the image!";
        }
      }
    }).catch(() => {
      document.getElementById("information")!.style.color = "red";
      document.getElementById("information")!.textContent = "An error occurred while processing the image.";
    });
  } else {
    document.getElementById("information")!.style.color = "red";
    document.getElementById("information")!.textContent = "Please select an image and an algorithm.";
  }
}
</script>

<template>
  <div class="container">
    <h3>Apply a processing algorithm to an image</h3>
    <p>Click on the image whenever you want to download it.</p>

    <div class="content">
      <div class="left">
        <h3>Parameters</h3>

        <p>Choose an image</p>
        <select v-model="SELECTED_ID" @change="updateDisplay" id="list">
          <option v-for="image in IMAGE_LIST" :value="image.id" :key="image.id">{{ image.name }}</option>
        </select>

        <p>Choose an algorithm</p>
        <select v-model="SELECTED_ID_1" @change="updateParameters" id="algorithms">
          <option id="brightness">Brightness adjustment</option>
          <option id="histogram">Histogram equalization</option>
          <option id="hue">Gamma adjustment</option>
          <option id="mean">Blur using Mean convolution</option>
          <option id="gaussian">Blur using Gaussian convolution</option>
          <option id="sobel">Sobel edge detection</option>
          <option id="sepia">Sepia filter</option>
          <option id="vignette">Vignette filter</option>
          <option id="grain">Grain filter</option>
          <option id="noise">Noise filter</option>
          <option id="negative">Negative filter</option>
          <option id="gray">Grayscale filter</option>
          <option id="horizontal">Horizontal flip</option>
        </select>

        <p id="label">No parameters required</p>
        <input id="parameter" type="number">
        <button @click="processImage">Process</button>
      </div>

      <div class="middle">
        <a id="clickable"><img src="" alt="" id="display"></a>
        <h4 id="information"></h4>
      </div>

      <div class="right">
        <h3>Metadata</h3>

        <p>Identifier</p>
        <p id="identifier"></p>

        <p>Name</p>
        <p id="name"></p>

        <p>Type</p>
        <p id="type"></p>

        <p>Size</p>
        <p id="size"></p>
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

.content {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
}

.left,
.middle,
.right {
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #f7f7f7;
  text-align: left;
}

.middle {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 20px;
}

.left p,
.right p {
  margin-bottom: 5px;
}

.left select,
.left input {
  width: 100%;
  padding: 5px;
  margin-top: 5px;
}

.left button {
  background-color: #007BFF;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button {
  margin-top: 2vh;
}

.left button:hover {
  background-color: #0056b3;
}

.right p:nth-child(odd) {
  font-family: monospace;
}

label {
  color: cornflowerblue;
}

#display {
  max-width: 100%;
  max-height: 50vh;
  margin: 0 auto;
}

#display:hover {
  opacity: 80%;
}
</style>