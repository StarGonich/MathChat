<template>
  <div class="message-input-container">
    <div class="input-area">
      <div class="ui action fluid input">
        <input
          type="text"
          placeholder="Введите сообщение..."
          v-model="inputValue"
          @keypress.enter="handleSend"
        />
        <div v-if="isPreview" class="ui pointing below label"">
          <div v-for="line in formatArr(inputValue, 45)" :key="line.id">
            <div v-if="line.isLatex">
              <vue-latex  :expression="line.content"/>
            </div>
            <div v-else>{{ line.content }}</div>
          </div>
        </div>
        <button 
          class="ui icon button"
          @click="isPreview = !isPreview"
        >
          <i :class="isPreview ? 'pencil alternate icon' : 'eye icon'"></i>
        </button>
        <button 
          class="ui blue icon button"
          @click="handleSend"
          :disabled="!inputValue.trim() || isPreview"
        >
          <i class="paper plane icon"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const emit = defineEmits(['send'])

const isPreview = ref(false)
const inputValue = ref('')

function formatArr(str, n){
    let ss = []
    let s = str.split(' ')
    let i = 0
    let j = 0
    let cur = ''
    let latex = false
    while(i < s.length){
        if(s[i] == '$'){
            if(latex){
                latex = false
                ss.push(
                    {
                        id: j,
                        content: cur.slice(0),
                        isLatex: true
                    }
                )
                j++
                cur = ''
            }else{
                latex = true
                if(cur != ''){
                    ss.push(
                        {
                            id: j,
                            content: cur.slice(0),
                            isLatex: false
                        }
                    )
                    j++
                    cur = ''
                }
            }
        }else if(cur.length + s[i].length <= n){
            cur += ' ' + s[i]
        }else if(!latex){
            ss.push(
                {
                    id: j,
                    content: cur.slice(0),
                    isLatex: false
                }
            )
            j++
            cur = s[i]
        }
        i++
    }
    ss.push(
        {
            id: j,
            content: cur.slice(0),
            isLatex: false
        }
    )
    return ss
}

const handleSend = () => {
  if (!inputValue.value.trim()) return;
  
  const message = { text: inputValue.value };
  
  emit('send', message);
  inputValue.value = '';
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
