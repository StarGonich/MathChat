<template>
    <div class="ui text container">
        <h2 class="ui image header">
            <img src="./static/images/icon.webp" class="image">
            <div class="content">
                Регистрация
            </div>
        </h2>
        <div class="ui raised left aligned container segment">
            <form class="ui large form">
                <div class="field">
                    <div class="ui left icon input">
                        <i class="user icon"></i>
                        <input type="text" name="username" v-model="user.username" placeholder="Никнейм">
                    </div>
                </div>
                <div class="field">
                    <div :class="firstClass">
                        <i class="user icon"></i>
                        <input type="text" name="firstname" v-model="user.firstname" placeholder="Имя*">
                    </div>
                </div>
                <div class="field">
                    <div :class="lastClass">
                        <i class="user icon"></i>
                        <input type="text" name="lastname" v-model="user.lastname" placeholder="Фамилия*">
                    </div>
                </div>
                <div class="ui clearing divider"></div>
                <div class="field">
                    <div :class="emailClass">
                        <i class="user icon"></i>
                        <input type="text" name="email" v-model="user.email" placeholder="Почта*">
                    </div>
                </div>
                <div class="ui clearing divider"></div>
                <div class="field">
                    <div :class="passClass">
                        <i class="lock icon"></i>
                        <input type="password" name="password" v-model="user.password" placeholder="Пароль*" @input="onInputPass">
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="lock icon"></i>
                        <input type="password" name="password" v-model="password" placeholder="Подтвердите пароль*" @input="onInputPass">
                    </div>
                </div>
                <div class="ui left red message" v-if="!isCorrectPass">
                    <i class="times icon"></i>
                    Пароли не совпадают
                </div>
                <div class="ui left green message" v-else>
                    <i class="check icon"></i>
                    Пароли совпадают
                </div>
                <div class="ui clearing divider"></div>
                <button class = "ui button primary" @click.prevent="register" :disabled="!user.firstname || !user.lastname || !user.email || !user.password || !isCorrectPass">Зарегистрироваться</button>
                <button class = "ui button" @click="auth">Есть аккаунт? Войти</button>
                <div class="ui info message" v-if="msg">
                    <i class="close icon" @click="close"></i>
                    <div class="header">
                        {{ msg }}
                    </div>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup>
import router from '@/router'
import axios from 'axios'
import { ref, watch } from 'vue'

const user = ref({
    username: '',
    firstname: '',
    lastname: '',
    email: '',
    password: ''
})

const firstClass = ref('ui left icon input')
const lastClass = ref('ui left icon input')
const emailClass = ref('ui left icon input')
const passClass = ref('ui left icon input')
watch(user.value, (newUser) =>{
    if (newUser.firstname){
        firstClass.value = 'ui left icon input'
    }else{
        firstClass.value = 'ui left icon error input'
    }
    if (newUser.lastname){
        lastClass.value = 'ui left icon input'
    }else{
        lastClass.value = 'ui left icon error input'
    }
    if (newUser.email){
        emailClass.value = 'ui left icon input'
    }else{
        emailClass.value = 'ui left icon error input'
    }
    if (newUser.password){
        passClass.value = 'ui left icon input'
    }else{
        passClass.value = 'ui left icon error input'
    }
})
const password = ref('')
const isCorrectPass = ref(false)

const msg = ref('')

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
            router.push({name: 'SendEmail'})
        }
        console.log(resp)
    } catch (e) {
        msg.value = 'Не удалось зарегистрироваться, повторите попытку позже'
    }
}

function auth(){
    router.push({name: 'Home'})
}

function close(){
    msg.value = ''
}

document.body.style.overflow = 'auto'
</script>