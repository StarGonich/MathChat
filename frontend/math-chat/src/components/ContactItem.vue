<template>
  <div 
    class="contact-item"
    :class="{ 'active': isSelected }"
    @click="$emit('click')"
  >
    <div class="contact-avatar">
      <img class="avatar-circle" v-if="imageUrl && imageUrl.length > 0" :src="imageUrl" />
      <div class="avatar-circle" v-else>{{ contact.avatar }}</div>
      <div v-if="contact.online" class="status-indicator online"></div>
      <div v-else class="status-indicator offline"></div>
    </div>
    
    <div class="contact-info" align="left">
      <div class="contact-header">
        <span class="contact-name" align="left">{{ contact.name }}</span>
        <span class="last-message-time" align="right">{{ contact.lastMessageTime }}</span>
      </div>
      <div class="contact-footer">
        <span class="last-message" align="left">{{ contact.lastMessage }}</span>
        <span v-if="contact.unreadCount > 0" class="ui mini circular blue label">
          <b>{{ contact.unreadCount }}</b>
        </span>
        <i v-else-if="contact.lastMessageStatus == 'UNREAD'" class="ui check circle outline icon"></i>
        <i v-else-if="contact.lastMessageStatus == 'READ'" class="ui check circle icon"></i>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios'
import { onMounted } from 'vue';
const ax_file = axios.create({
    baseURL: process.env.VUE_APP_SERVER_URL,
    withCredentials: true,
    responseType: 'blob'
})

const baseURL = process.env.VUE_APP_SERVER_URL

const props = defineProps({
  contact: {
    type: Object,
    required: true
  },
  isSelected: {
    type: Boolean,
    default: false
  }
});

const imageUrl = ref(null)

async function parseAvatar(){
  if(props.contact.imageUrl && props.contact.imageUrl.length > 0){
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

defineEmits(['click']);
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

.contact-item.active {
  background: #e8f4fd;
  border-left: 3px solid #2185d0;
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

.status-indicator.online {
  background: #21ba45;
}

.status-indicator.offline {
  background: #767676;
}

.contact-info {
  flex: 1;
  min-width: 0;
  overflow: hidden;
}

.contact-header {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  margin-bottom: 4px;
}

.contact-name {
  font-weight: 600;
  color: #1b1c1d;
}

.last-message-time {
  font-size: 0.85em;
  color: #999;
}

.contact-footer {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

.last-message {
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
</style>
