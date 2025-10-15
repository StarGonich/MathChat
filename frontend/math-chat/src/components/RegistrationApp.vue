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
            <button class = "ui button primary" @click.prevent="register" :disabled="!user.firstname || !user.lastname || !user.email || !user.password || !isCorrectPass">Зарегистрироваться</button>
            <button class = "ui button" @click="auth">Есть аккаунт? Войти</button>
            <div class="ui message" v-if="msg" @click="close">
                {{ msg }}
            </div>
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

const msg = ref('')

const emit = defineEmits(['quitEvent'])

function onInputPass(){
    isCorrectPass.value = user.value.password === password.value
}

async function register() {
    try {
        let resp = {}
        await axios.post('http://localhost:8080/register', user.value)
            .then(response => resp = response.data)
        if (!resp.firstname){
            msg.value = resp
        }else{
            emit('quitEvent', 'email')
        }
        console.log(resp)
    } catch (e) {
        msg.value = 'Не удалось зарегистрироваться, повторите попытку позже'
    }
}

function auth(){
    emit('quitEvent', 'auth')
}

function close(){
    msg.value = ''
}
</script>