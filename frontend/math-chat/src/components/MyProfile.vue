<template>
    <div class="back">
        <div class="window">
            <div class="ui attached stackable menu">
                <div class="ui container">
                    <a class="item">
                        <p>Профиль пользователя {{ username }}</p>
                    </a>
                    <a class="right item" @click="$emit('closeWindow')">
                        <i class="close icon"></i>
                    </a>
                </div>
            </div>
            <form class="ui form">
                <p>Личная информация</p>
                <div class="my-avatar">
                    <div class="avatar-circle">{{ avatar }}</div>
                </div>
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
                <div class="ui clearing divider"></div>
                <button class="ui primary button" @click.prevent="update">Сохранить изменения</button>
            </form>
            <div class="ui clearing divider"></div>
            <button class="ui red button" @click="$emit('quit')">Выйти из профиля</button>
            <div class="ui info message" v-if="msg">
                <i class="close icon" @click="msg = ''"></i>
                <div class="header">
                    {{ msg }}
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import axios from 'axios'
const ax = axios.create({
    baseURL: process.env.VUE_APP_SERVER_URL,
    withCredentials: true
})

const baseURL = process.env.VUE_APP_SERVER_URL

const props = defineProps({
    user: {
        type: Object,
        required: true
    }
})

const username = ref(props.user.username)
const user = ref(props.user)
const avatar = computed(() => {
    if(user.value.firstname && user.value.lastname){
        return user.value.firstname.slice(0, 1) + user.value.lastname.slice(0, 1)
    }else{
        return "NN"
    }
})
const msg = ref('')

async function update(){
    try{
        await ax.put(baseURL + '/update/'+props.user.id, user.value)
            .then(response => user.value = response.data)
        username.value = user.value.username
        emit('updateProfile')
    }catch(e){
        console.log(e)
        msg.value = 'Изменения не сохранены, попробуйте позже'
    }
}

const emit = defineEmits(['closeWindow', 'updateProfile', 'quit'])

</script>

<style scoped>
.window {
  position: fixed;
  z-index: 999;
  top: 15%;
  left: 35%;
  width: 30%;
  background-color: #ffffff;
  border: 3px solid #000000;
  padding: 15px 15px;
}

.back {
    position: fixed;
    z-index: 998;
    top: 0%;
    left: 0%;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
}

.my-avatar {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.avatar-circle {
  width: 120px;
  height: 120px;
  align-self: center;
  border-radius: 50%;
  background: linear-gradient(135deg, #2185d0, #1678c2);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 48px;
}

</style>