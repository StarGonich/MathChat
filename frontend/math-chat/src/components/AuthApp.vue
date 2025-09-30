<template>
    <div class="ui text container">
        <h2 class="ui center header">Вход</h2>
        <form class="ui raised left aligned container segment">
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
            <button class = "ui button primary" @click="auth" :disabled="!user.email || !user.password">Войти</button>
            <button class = "ui button" @click="register">Нет аккаунта? Зарегистрироваться</button>
        </form>
    </div>
</template>

<script setup>
import { ref } from 'vue'
const user = ref({
    email: '',
    password: ''
})
const emit = defineEmits(['regEvent'])

function auth() {
    alert('Успешная авторизация!')
}

function register(){
    emit('regEvent', 'reg')
}
</script>