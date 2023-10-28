<script setup lang="ts">
import { ref } from 'vue';
import { api } from '@/http-api';

const TARGET = ref<HTMLInputElement>();
const TEXT = ref<string>('');
const ALERT_TYPE = ref<string>('');
const SELECTED_FILE_NAME = ref<string>('');

function handleUploadToServer() {
  if (TARGET.value !== null && TARGET.value !== undefined && TARGET.value.files !== null) {
    const file = TARGET.value.files[0];
    if (file === undefined) return;

    let formData = new FormData();
    formData.append("file", file);

    api.createImage(formData).then(() => {
      TEXT.value = "Successfully uploaded " + file.name + "!";
      ALERT_TYPE.value = 'alert-success';
      if (TARGET.value !== undefined) {
        TARGET.value.value = '';
      }
      SELECTED_FILE_NAME.value = '';
    }).catch(e => {
      ALERT_TYPE.value = 'alert-error';
      TEXT.value = "Error: " + e.message + ".";
    });
  }
}

function handleUploadToClient(event: Event) {
  TEXT.value = '';
  TARGET.value = event.target as HTMLInputElement;

  if (TARGET.value !== null && TARGET.value !== undefined && TARGET.value.files !== null) {
    let name = TARGET.value.files[0].name;
    SELECTED_FILE_NAME.value = name;
    let extension = name.split('.').pop();
    if (extension !== "jpg" && extension !== "png") {
      ALERT_TYPE.value = 'alert-error';
      TEXT.value = "Only JPEG and PNG file formats are supported.";
    } else {
      ALERT_TYPE.value = '';
    }
  }
}
</script>

<template>
  <div class="container">
    <h3 class="my-4">Upload an image</h3>
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="form-group d-flex align-items-center">
          <label for="file" class="custom-file-upload btn btn-secondary mr-2">
            Choose File
            <input type="file" id="file" ref="file" @change="handleUploadToClient" class="d-none">
          </label>
          <button @click="handleUploadToServer" class="btn btn-success">Upload</button>
        </div>
        <div class="selected-file-name mt-3" v-if="SELECTED_FILE_NAME">{{ SELECTED_FILE_NAME }}</div>
      </div>
    </div>
    <div class="row mt-4">
      <div class="col-md-6">
        <div v-if="TEXT" :class="['alert', ALERT_TYPE, 'text-center']">{{ TEXT }}</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.align-items-center>* {
  margin-left: 1vw;
  margin-right: 1vw;
}

.custom-file-upload {
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
  background-color: #6c757d;
  color: white;
  width: 50vw;
}

.btn-success {
  width: 15vw;
  background-color: #28a745;
  color: white;
}

.alert {
  padding: 10px;
  border-radius: 5px;
  width: 100%;
  text-align: center;
}

.alert-success {
  color: green;
  background-color: lightgreen;
  width: 50vw;
}

.alert-error {
  color: red;
  background-color: lightcoral;
  width: 50vw;
}

.selected-file-name {
  margin-top: 10px;
  text-align: center;
}
</style>