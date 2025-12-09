<template>
  <div class="twelve wide column chat-column">
    <div class="chat-window">
      <div v-if="contact" class="chat-header">
        <div class="header-info">
          <div class="contact-avatar-small">
            <div class="avatar-circle-small">{{ contact.avatar }}</div>
            <div v-if="contact.online" class="status-indicator-small online"></div>
            <div v-else class="status-indicator-small offline"></div>
          </div>
          <div class="header-text">
            <h4 class="ui header contact-name-header">{{ contact.name }} {{ contact.username }}</h4>
            <span class="status-text" align="left">{{ contact.online ? "Онлайн" : "Не в сети" }}</span>
          </div>
        </div>
      </div>
      
      <div v-if="contact" class="messages-container" ref="messagesContainer">
        <MessageObject
          v-for="message in messages"
          :key="message.id"
          :message="message"
        />
        <div v-if="messages.length == 0" class="empty-chat">
          <i class="huge comments outline icon"></i>
          <h3>Поздоровайтесь!</h3>
          <p>И начните общение с {{ contact.name }}</p>
        </div>
      </div>
      
      <div v-if="!contact" class="empty-state">
        <i class="huge comments outline icon"></i>
        <h3>Начните общение</h3>
        <p>Выберите собеседника в левой части приложения</p>
      </div>
      
      
      <MessageInput
        v-if="contact"
        @send="handleSendMessage"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue';
import MessageObject from './MessageObject.vue';
import MessageInput from './MessageInput.vue';

const props = defineProps({
  contact: {
    type: Object,
    default: null
  },
  messages: {
    type: Array,
    default: () => []
  }
});

const emit = defineEmits(['sendMessage']);

const messagesContainer = ref(null);

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
    }
  });
};

watch(() => props.messages, () => {
  scrollToBottom();
}, { deep: true });

watch(() => props.contact, () => {
  scrollToBottom();
});

const handleSendMessage = (message) => {
  emit('sendMessage', message);
  scrollToBottom();
};
</script>

<style scoped>
.chat-column {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: white;
}

.chat-window {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.chat-header {
  padding: 15px 20px;
  border-bottom: 1px solid #ddd;
  background: white;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.header-info {
  display: flex;
  align-items: center;
}

.contact-avatar-small {
  position: relative;
  margin-right: 12px;
}

.avatar-circle-small {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #2185d0, #1678c2);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
}

.status-indicator-small {
  position: absolute;
  bottom: 2px;
  right: 0;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  border: 2px solid white;
}

.status-indicator-small.online {
  background: #21ba45;
}

.status-indicator-small.offline {
  background: #767676;
}

.header-text {
  display: flex;
  flex-direction: column;
}

.contact-name-header {
  margin: 0 0 2px 0;
  color: #1b1c1d;
}

.status-text {
  font-size: 0.85em;
  color: #767676;
  margin-left: 2px;
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f9fafb;
}

.messages-container::-webkit-scrollbar {
  width: 8px;
}

.messages-container::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.messages-container::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

.messages-container::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

.empty-state {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #767676;
  text-align: center;
}

.empty-chat {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #767676;
  text-align: center;
  height: 100%;
}

.empty-state i {
  color: #ddd;
  margin-bottom: 20px;
}

.empty-state h3 {
  color: #1b1c1d;
  margin-bottom: 10px;
}

.empty-state p {
  color: #999;
}
</style>
