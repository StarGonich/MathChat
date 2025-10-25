<template>
  <div v-if="dev">
    <DevApp @switchEvent="(msg) => str = msg" />
  </div>
  <div v-if="str === 'reg'">
    <RegistrationApp @quitEvent="(msg) => str = msg" />
  </div>
  <div v-if="str === 'email'">
    <SendEmailApp @quitEvent="(msg) => str = msg" />
  </div>
  <div v-if="str === 'auth'">
    <AuthApp @quitEvent="(msg) => str = msg"/>
  </div>
  <div v-if="str === 'mes'">
    <MessengerApp :userId="id" @quitEvent="(msg) => str = msg"/>
  </div>
</template>

<script setup>
import DevApp from './components/DevApp.vue'
import RegistrationApp from './components/RegistrationApp.vue'
import SendEmailApp from './components/SendEmailApp.vue'
import MessengerApp from './components/MessengerApp.vue'
import AuthApp from './components/AuthApp.vue'
import { ref } from 'vue'
const str = ref('auth')
const dev = false
let id = ref(4);

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