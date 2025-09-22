<template>
    <form>
        <input v-model="user.firstname" place-holder="Имя">
        <input v-model="user.lastname" place-holder="Фамилия">
        <input v-model="user.email" place-holder="Почта">
        <input v-model="user.password" place-holder="Пароль">
        <button @click="register">Зарегистрироваться</button>
    </form>
    <p>{{user}}</p>
    <button @click="getUsers">Получить пользователей</button>
    <p>{{users}}</p>
</template>

<script setup>
import axios from 'axios'
import { ref } from 'vue'
const user = ref({
    id: 0,
    firstname: '',
    lastname: '',
    email: '',
    password: ''
})
let users = ref([])

async function register() {
    try {
        await axios.post('http://localhost:8090/register', user.value)
        alert('Успешная регистрация!')
    } catch (e) {
        alert(e)
    }
}

async function getUsers() {
    try {
        await axios.get('http://localhost:8090/api/user/findAll')
            .then(response => users.value = response.data)
    } catch (e) {
        alert(e)
    }
}
</script>