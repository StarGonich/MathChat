<template>
  <div class="message-input-container">
    
    <div v-if="isLatexMode && latexPreview" class="latex-preview">
      <div class="preview-label">Preview:</div>
      <div class="preview-content" ref="previewContent"></div>
    </div>
    
    <div class="input-area">
      <div class="ui action fluid input">
        <input
          v-if="!isLatexMode"
          type="text"
          :placeholder="isLatexMode ? 'Enter LaTeX expression (e.g., E = mc^2)' : 'Введите сообщение...'"
          v-model="inputValue"
          @keypress.enter="handleSend"
        />
        <textarea
          v-else
          :placeholder="'Enter LaTeX expression (e.g., E = mc^2)'"
          v-model="inputValue"
          @keypress.enter.prevent="handleSend"
          rows="1"
          class="latex-textarea"
        ></textarea>
        <button 
          class="ui blue icon button"
          @click="handleSend"
          :disabled="!inputValue.trim()"
        >
          <i class="paper plane icon"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue';
import katex from 'katex';

const emit = defineEmits(['send']);

const isLatexMode = ref(false);
const inputValue = ref('');
const latexPreview = ref('');
const previewContent = ref(null);

const renderPreview = () => {
  if (isLatexMode.value && inputValue.value.trim() && previewContent.value) {
    try {
      katex.render(inputValue.value, previewContent.value, {
        displayMode: true,
        throwOnError: false
      });
      latexPreview.value = inputValue.value;
    } catch (error) {
      console.error('Error rendering preview:', error);
      if (previewContent.value) {
        previewContent.value.textContent = inputValue.value;
      }
    }
  } else {
    latexPreview.value = '';
  }
};

watch([isLatexMode, inputValue], () => {
  nextTick(() => {
    renderPreview();
  });
});

const handleSend = () => {
  if (!inputValue.value.trim()) return;
  
  const message = isLatexMode.value
    ? { latex: inputValue.value }
    : { text: inputValue.value };
  
  emit('send', message);
  inputValue.value = '';
  latexPreview.value = '';
};
</script>

<style scoped>
.message-input-container {
  border-top: 1px solid #ddd;
  background: white;
  padding-top: 15px;
}

.input-controls {
  margin-bottom: 12px;
}

.ui.toggle.checkbox {
  display: inline-flex;
  align-items: center;
}

.ui.toggle.checkbox label {
  padding-left: 4rem;
  color: #1b1c1d;
  font-weight: 600;
}

.latex-preview {
  background: #f9fafb;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 12px;
  margin-bottom: 12px;
}

.preview-label {
  font-size: 0.85em;
  color: #767676;
  margin-bottom: 8px;
  font-weight: 600;
}

.preview-content {
  padding: 10px;
  background: white;
  border-radius: 4px;
  min-height: 40px;
  overflow-x: auto;
}

.preview-content::-webkit-scrollbar {
  height: 6px;
}

.preview-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.preview-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.input-area {
  position: relative;
}

.ui.action.input {
  display: flex;
}

.ui.action.input input,
.latex-textarea {
  flex: 1;
  border: 1px solid rgba(34, 36, 38, 0.15);
  padding: 12px 14px;
  font-size: 14px;
  border-radius: 4px 0 0 4px;
  outline: none;
  font-family: 'Lato', 'Helvetica Neue', Arial, Helvetica, sans-serif;
}

.latex-textarea {
  resize: none;
  min-height: 44px;
  max-height: 120px;
  overflow-y: auto;
  line-height: 1.5;
}

.ui.action.input input:focus,
.latex-textarea:focus {
  border-color: #85b7d9;
}

.ui.action.input button {
  border-radius: 0 4px 4px 0;
  padding: 12px 20px;
}

.ui.button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
