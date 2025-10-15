<template>
    <div class="ui text container">
        <h2 class="ui image header">
            <img src="./static/images/user.png" class="image">
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
                <button class = "ui button primary" @click="auth" :disabled="!user.email || !user.password">Войти</button>
                <button class = "ui button" @click="register">Нет аккаунта? Зарегистрироваться</button>
                <button class = "ui button" @click.prevent="forgot" :disabled="!user.email">Забыли пароль?</button>
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
import axios from 'axios'
import { ref, watch } from 'vue'
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
const emit = defineEmits(['quitEvent'])
const msg=ref('')

function auth() {
    emit('quitEvent', 'mes')
}

function register(){
    emit('quitEvent', 'reg')
}

async function forgot(){
    try{
        await axios.post('http://localhost:8080/changePassword', {email: user.value.email})
            .then(response => msg.value = response.data)
    }catch(e){
        msg.value = "Не удалось поменять пароль, попробуйте позже"
    }
}

function close(){
    msg.value = ''
}
</script>