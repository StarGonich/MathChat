<template>
    <div>
        <p> Список пользователей </p>
        <button @click="getUsers">Получить пользователей</button>
        <p>{{users}}</p>
    </div>
    <div>
        <p> Список чатов пользователя </p>
        <button @click="getChats">Получить список чатов</button>
        <p>{{chats}}</p>
    </div>
    <div>
        <p> Сообщения </p>
        <button @click="getMessenges">Открыть беседу</button>
        <p>{{messages}}</p>
    </div>
</template>

<script setup>
import axios from 'axios'
import { ref } from 'vue'

let users = ref([])
let chats = ref([])
let messages = ref([])
const userId = 1
const chatId = 1

async function getUsers() {
    try {
        await axios.get('http://localhost:8090/api/user/findAll')
            .then(response => users.value = response.data)
    } catch (e) {
        alert(e)
    }
}

async function getChats() {
    try {
        await axios.get('http://localhost:8090/api/messenger/' + userId)
            .then(response => chats.value = response.data)
    } catch (e) {
        alert(e)
    }
}

async function getMessenges() {
    try {
        await axios.get('http://localhost:8090/api/messenger/chat/' + chatId)
            .then(response => messages.value = response.data)
    } catch (e) {
        alert(e)
    }
}
</script>