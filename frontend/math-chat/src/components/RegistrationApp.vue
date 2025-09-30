<template>
    <div class="ui text container">
        <h2 class="ui center header">Регистрация</h2>
        <form class="ui raised left aligned segment">
            <div>
                <p><label>Имя: </label></p>
                <div class="ui input">
                    <input v-model="user.firstname" place-holder="Имя">
                </div>
                <div class="ui left pointing red basic  label" v-if="!user.firstname">
                    Нет имени
                </div>
            </div>
            <div class="ui clearing divider"></div>
            <div>
                <p><label>Фамилия: </label></p>
                <div class="ui input">
                    <input v-model="user.lastname" place-holder="Фамилия">
                </div>
                <div class="ui left pointing red basic  label" v-if="!user.lastname">
                    Нет фамилии
                </div>
            </div>
            <div class="ui clearing divider"></div>
            <div>
                <p><label>Почта: </label></p>
                <div class="ui input">
                    <input v-model="user.email" place-holder="Почта">
                </div>
                <div class="ui left pointing red basic  label" v-if="!user.email">
                    Нет почты
                </div>
            </div>
            <div class="ui clearing divider"></div>
            <div>
                <p><label>Пароль: </label></p>
                <div class="ui input">
                    <input v-model="user.password" place-holder="Пароль" type="password" @input="onInputPass">
                </div>
                <div class="ui left pointing red basic  label" v-if="!user.password">
                    Нет пароля
                </div>
            </div>
            <div class="ui clearing divider"></div>
            <div>
                <p><label>Подтвердите пароль: </label></p>
                <div class="ui input">
                    <input v-model="password" place-holder="Пароль" type="password" @input="onInputPass">
                </div>
                <div class="ui left pointing red basic  label" v-if="!isCorrectPass">
                    Пароли не совпадают
                </div>
            </div>
            <div class="ui clearing divider"></div>
            <button class = "ui button primary" @click="register" :disabled="!user.firstname || !user.lastname || !user.email || !user.password || !isCorrectPass">Зарегистрироваться</button>
            <button class = "ui button" @click="auth">Есть аккаунт? Войти</button>
        </form>
    </div>
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
const emit = defineEmits(['authEvent'])

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

function auth(){
    emit('authEvent', 'auth')
}
</script>