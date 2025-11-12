<template>
    <div class="ui text container">
        <h2 class="ui image header">
            <img src="./static/images/icon.webp" class="image">
            <div class="content">
                Вход
            </div>
        </h2>
        <div class="ui raised left aligned container segment">
            <form class="ui large form">
                <div class="field">
                    <div :class="emailClass">
                        <i class="user icon"></i>
                        <input type="text" name="email" v-model="user.email" placeholder="Почта">
                    </div>
                </div>
                <div class="ui clearing divider"></div>
                <div class="field">
                    <div :class="passClass">
                        <i class="lock icon"></i>
                        <input type="password" name="password" v-model="user.password" placeholder="Пароль">
                    </div>
                </div>
                <div class="ui clearing divider"></div>
                <button class = "ui button primary" @click.prevent="auth" :disabled="!user.email || !user.password">Войти</button>
                <button class = "ui button" @click="register">Нет аккаунта? Зарегистрироваться</button>
                <button class = "ui button" @click.prevent="forgot" :disabled="!user.email">Забыли пароль?</button>
                <button class = "ui button" @click.prevent="github">Войти через GitHub</button>
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
import { ref, watch } from 'vue'
import axios from 'axios'
const ax = axios.create({
    baseURL: 'http://localhost:8080',
    withCredentials: true
})

const user = ref({
    email: '',
    password: ''
})

const emailClass = ref('ui left icon input')
const passClass = ref('ui left icon input')
watch(user.value, (newUser) =>{
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

const msg=ref('')

async function auth() {
    try{
        let req = "username=" + user.value.email + "&password=" + user.value.password
        req = req.replace("@", "%40")
        let resp = ""
        await ax.post('http://localhost:8080/login', req).then(response => resp = response.data)
        console.log(resp)
        if(!resp.name){
            msg.value = "Неудачная попытка входа"
        }else{
            router.push({name: 'Messenger', params: {userId: resp.name}})
        }
    }catch(e){
        console.log(e)
        msg.value = "Не удалось войти, попробуйте позже"
    }
}

function register(){
    router.push({name: 'Register'})
}

async function forgot(){
    try{
        await axios.post('http://localhost:8080/changePassword', {email: user.value.email})
            .then(response => msg.value = response.data)
    }catch(e){
        msg.value = "Не удалось поменять пароль, попробуйте позже"
    }
}

async function github(){
    try{
        await axios.post('https://github.com/login/device/code&client_id=')
            .then(resp => console.log(resp))
    }catch(e){
        console.log(e)
    }
}

function close(){
    msg.value = ''
}

document.body.style.overflow = 'hidden'
</script>