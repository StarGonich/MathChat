<template>
  <div class="message-wrapper" :class="{ 'own-message': message.isOwn }">
    <div class="message" :class="{ 'own': message.isOwn }">
      <div class="message-content">
        <div v-if="message.latex" class="latex-content" ref="latexContent"></div>
        <div v-else class="text-content" align="left">{{ message.text }}</div>
      </div>
       <div class="message-footer">
        <span class="time" align="right">{{ message.time }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import katex from 'katex';

const props = defineProps({
  message: {
    type: Object,
    required: true
  }
});

const latexContent = ref(null);

const renderLatex = () => {
  if (props.message.latex && latexContent.value) {
    try {
      katex.render(props.message.latex, latexContent.value, {
        displayMode: true,
        throwOnError: false
      });
    } catch (error) {
      console.error('Error rendering LaTeX:', error);
      latexContent.value.textContent = props.message.latex;
    }
  }
};

onMounted(() => {
  renderLatex();
});

watch(() => props.message.latex, () => {
  renderLatex();
});
</script>

<style scoped>
.message-wrapper {
  display: flex;
  margin-bottom: 15px;
}

.message-wrapper.own-message {
  justify-content: flex-end;
}

.message {
  max-width: 60%;
  padding: 10px 14px;
  border-radius: 8px;
  background: white;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.message.own {
  background: #2185d0;
  color: white;
}

.message-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 6px;
  font-size: 0.85em;
  width: 100%;
}

.sender {
  font-weight: 600;
  margin-right: 10px;
}

.message.own .sender {
  color: rgba(255, 255, 255, 0.95);
}

.time {
  color: #999;
  font-size: 0.9em;
}

.message.own .time {
  color: rgba(255, 255, 255, 0.8);
}

.message-content {
  line-height: 1.5;
}

.text-content {
  word-wrap: break-word;
}

.latex-content {
  padding: 10px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 4px;
  overflow-x: auto;
}

.message.own .latex-content {
  background: rgba(255, 255, 255, 0.15);
}

.latex-content::-webkit-scrollbar {
  height: 6px;
}

.latex-content::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.05);
  border-radius: 3px;
}

.latex-content::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.2);
  border-radius: 3px;
}

.latex-content::-webkit-scrollbar-thumb:hover {
  background: rgba(0, 0, 0, 0.3);
}
</style>
