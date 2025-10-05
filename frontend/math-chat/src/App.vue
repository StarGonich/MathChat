<template>
  <p> Выбор страницы </p>
  <select v-model="str">
    <option value="reg">Страница регистрации</option>
    <option value="auth">Страница входа</option>
    <option value="mes">Страница мессенджера</option>
  </select>
  <div v-if="str === 'reg'">
    <RegistrationApp @authEvent="(msg) => str = msg" />
  </div>
  <div v-if="str === 'auth'">
    <AuthApp @regEvent="(msg) => str = msg"/>
  </div>
  <div v-else-if="str === 'mes'">
    <MessengerApp :userId="0" @quitEvent="(msg) => str = msg"/>
  </div>
</template>

<script setup>
import RegistrationApp from './components/RegistrationApp.vue'
import MessengerApp from './components/MessengerApp.vue'
import AuthApp from './components/AuthApp.vue'
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