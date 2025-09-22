<template>
  <p> Выбор страницы </p>
  <select v-model="str">
    <option value="reg">Страница регистрации</option>
    <option value="mes">Страница мессенджера</option>
  </select>
  <div v-if="str === 'reg'">
    <RegistrationApp />
  </div>
  <div v-else-if="str === 'mes'">
    <MessengerApp />
  </div>
</template>

<script setup>
import RegistrationApp from './components/RegistrationApp.vue'
import MessengerApp from './components/MessengerApp.vue'
import { ref } from 'vue'
const str = ref('reg')
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>