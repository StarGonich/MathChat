<template>
  <p> Выбор страницы </p>
  <select v-model="str">
    <option value="reg">Страница регистрации</option>
    <option value="email">Страница подтверждения почты</option>
    <option value="auth">Страница входа</option>
    <option value="mes">Страница мессенджера</option>
    <option value="dark">Тёмная тема</option>
    <option value="test">Тест</option>
  </select>
</template>

<script setup>
import { ref, watch } from 'vue'
const str = ref('')
const emit = defineEmits(['switchEvent'])

watch(str, (msg) => {
    emit('switchEvent', msg)
})
</script>