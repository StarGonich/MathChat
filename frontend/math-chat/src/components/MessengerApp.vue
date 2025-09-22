<template>
    <p> Список пользователей </p>
    <button @click="getUsers">Получить пользователей</button>
    <p>{{users}}</p>
</template>

<script setup>
import axios from 'axios'
import { ref } from 'vue'

let users = ref([])

async function getUsers() {
    try {
        await axios.get('http://localhost:8090/api/user/findAll')
            .then(response => users.value = response.data)
    } catch (e) {
        alert(e)
    }
}
</script>