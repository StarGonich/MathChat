<template>
    <form>
        <div>
            <label for="firstname">Имя: </label>
            <input v-model="user.firstname" name="firstname" place-holder="Имя">
            <span v-if="!user.firstname"> Нет имени </span>
        </div>
        <div>
            <label for="firstname">Фамилия: </label>
            <input v-model="user.lastname" name="lastname" place-holder="Фамилия">
            <span v-if="!user.lastname"> Нет фамилии </span>
        </div>
        <div>
            <label for="firstname">Почта: </label>
            <input v-model="user.email" name="email" place-holder="Почта">
            <span v-if="!user.email"> Нет почты </span>
        </div>
        <div>
            <label for="firstname">Пароль: </label>
            <input v-model="user.password" name="password" place-holder="Пароль" type="password" @input="onInputPass">
            <span v-if="!user.password"> Нет пароля </span>
        </div>
        <div>
            <label for="firstname">Подтвердите пароль: </label>
            <input v-model="password" name="password" place-holder="Пароль" type="password" @input="onInputPass">
            <span v-if="!isCorrectPass"> Пароли не совпадают </span>
        </div>
        <button @click="register" :disabled="!user.firstname || !user.lastname || !user.email || !user.password || !isCorrectPass">Зарегистрироваться</button>
    </form>
</template>

<script setup>
import axios from 'axios'
import { ref } from 'vue'
const user = ref({
    firstname: '',
    lastname: '',
    email: '',
    password: ''
})
const password = ref('')
const isCorrectPass = ref(false)

function onInputPass(){
    isCorrectPass.value = user.value.password === password.value
}

async function register() {
    try {
        let msg = ''
        await axios.post('http://localhost:8090/register', user.value)
            .then(response => msg = 'Имя: ' + response.data.firstname + ", почта: " + response.data.email)
        alert('Успешная регистрация!\nОтвет сервера: ' + msg)
    } catch (e) {
        alert(e)
    }
}
</script>