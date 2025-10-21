<template>
  <div class="modal">
    <div class="ui attached stackable menu">
        <div class="ui container">
            <a class="item">
                <p>Профиль пользователя {{ username }}</p>
            </a>
            <a class="right item"  @click="quit">
                <i class="close icon"></i>
            </a>
        </div>
    </div>
    <form class="ui form">
        <p>Личная информация</p>
        <div class="field">
            <div class="ui left icon input">
                <i class="user icon"></i>
                <input type="text" name="username" v-model="user.username" placeholder="Ник">
            </div>
        </div>
        <div class="field">
            <div class="ui left icon input">
                <i class="user icon"></i>
                <input type="text" name="firstname" v-model="user.firstname" placeholder="Имя">
            </div>
        </div>
        <div class="field">
            <div class="ui left icon input">
                <i class="user icon"></i>
                <input type="text" name="lastname" v-model="user.lastname" placeholder="Фамилия">
            </div>
        </div>
        <div class="field">
            <div class="ui left icon input">
                <i class="user icon"></i>
                <input type="text" name="email" v-model="user.email" placeholder="Почта">
            </div>
        </div>
        <div button v-if="props.updated">
            <div class="ui clearing divider"></div>
            <p>Пароль</p>
            <div class="field">
                <div class="ui left icon input">
                    <i class="lock icon"></i>
                    <input type="password" name="password" v-model="user.password" placeholder="Пароль">
                </div>
            </div>
        </div>
        <div class="ui clearing divider"></div>
        <button v-if="props.updated" @click.prevent="update">Сохранить изменения</button>
    </form>
    <div class="ui info message" v-if="msg">
        <i class="close icon" @click="close"></i>
        <div class="header">
            {{ msg }}
        </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const props = defineProps({
    user: {
        required: true
    },
    updated: {
        required: true
    }
})

const username = ref(props.user.username)
const user = ref(props.user)
const msg = ref('')

async function update(){
    try{
        await axios.put('http://localhost:8080/api/user/update', user.value)
            .then(response => user.value = response.data)
        username.value = user.value.username
    }catch(e){
        console.log(e)
        msg.value = 'Изменения не сохранены, попробуйте позже'
    }
}

function close(){
    msg.value = ''
}

const emit = defineEmits(['quitEvent'])

function quit(){
    emit('quitEvent')
}
</script>

<style scoped>
.modal {
  position: fixed;
  z-index: 999;
  top: 20%;
  left: 35%;
  width: 30%;
  background-color: #ffffff;
  border: 3px solid #000000;
}
</style>