<template>
<div class="contact-item">
  <div class="contact-avatar">
    <img class="avatar-circle" v-if="imageUrl && imageUrl.length > 0" :src="imageUrl" />
    <div class="avatar-circle" v-else>{{ contact.avatar }}</div>
  </div>
    
  <div class="contact-info">
    <button v-if="!contact.inContacts" class="ui right floated icon primary button" @click="$emit('createContact')">
      <i class="plus icon"></i>
    </button>
    <button v-else class="ui right floated icon button">
      <i class="check icon"></i>
    </button>
    <div class="contact-header">
      <span class="contact-name" align="left">{{ contact.name }}</span>
    </div>
    <div class="contact-footer">
      <span class="contact-email" align="left">Начните общение!</span>
    </div>
  </div>
</div>
</template>

<script setup>
const props = defineProps({
  contact: {
    type: Object,
    required: true
  }
});

import { ref } from 'vue';
import axios from 'axios'
import { onMounted } from 'vue';
const ax_file = axios.create({
    baseURL: process.env.VUE_APP_SERVER_URL,
    withCredentials: true,
    responseType: 'blob'
})

const baseURL = process.env.VUE_APP_SERVER_URL

const imageUrl = ref(null)

async function parseAvatar(){
  if(props.contact.imageUrl && props.contact.imageUrl.length>0){
  try{
    ax_file.get(baseURL + '/api/files/download/' + props.contact.imageUrl)
    .then((response) => {image.value = response.data; imageUrl.value = URL.createObjectURL(image.value)})
  }catch(e){
    console.log(e)
  }
}
}

onMounted(async () => {
  parseAvatar()
})

defineEmits(['createContact']);
</script>

<style scoped>
.contact-item {
  display: flex;
  align-items: center;
  padding: 12px 15px;
  cursor: pointer;
  background: white;
  border-bottom: 1px solid #f0f0f0;
  transition: background-color 0.2s;
}

.contact-item:hover {
  background: #f8f9fa;
}

.contact-avatar {
  position: relative;
  margin-right: 12px;
  flex-shrink: 0;
}

.avatar-circle {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  background: linear-gradient(135deg, #2185d0, #1678c2);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
}

.status-indicator {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  border: 2px solid white;
}

.contact-info {
  flex: 1;
  min-width: 0;
  overflow: hidden;
}

.contact-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.contact-name {
  font-weight: 600;
  color: #1b1c1d;
}

.contact-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.contact-email {
  font-size: 0.9em;
  color: #767676;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
}

.ui.mini.circular.label {
  margin-left: 8px;
}

.center-vert{
    display: grid;
    justify-content: flex-end;
    place-items: center;
    height: 100%;
}
</style>
