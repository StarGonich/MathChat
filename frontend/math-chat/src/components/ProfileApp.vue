<template>
    <div class="back">
        <div class="window">
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
                <div button v-if="props.type == 'own'">
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
                <button class="ui primary button" v-if="props.type == 'own'" @click.prevent="update">Сохранить изменения</button>
                <button class="ui primary button" v-if="props.type == 'stranger'" @click.prevent="createChat">Начать общение</button>
            </form>
            <div class="ui info message" v-if="msg">
                <i class="close icon" @click="close"></i>
                <div class="header">
                    {{ msg }}
                </div>
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
    type: {
        required: true
    },
    watchId: {
        required: true
    }
})

const username = ref(props.user.username)
const user = ref(props.user)
const msg = ref('')
const changes = ref(false)
//const ava = ref([])

async function update(){
    try{
        await axios.put('http://localhost:8080/api/user/update', user.value)
            .then(response => user.value = response.data)
        username.value = user.value.username
        changes.value = true
    }catch(e){
        console.log(e)
        msg.value = 'Изменения не сохранены, попробуйте позже'
    }
}

//let bgImage = null

// Обработчик изменения файла
/*document.getElementById('bgImage').addEventListener('change', e => {
    ava.value = e.target.files[0];
    const reader = new FileReader();
    reader.onload = event => {
        bgImage = new Image();
        bgImage.onload = () => drawBackground();
        bgImage.src = event.target.result;
    };
    reader.readAsDataURL(file);
});

onMounted(() =>{
    const c = document.getElementById("myCanvas");
    this.canvas = c.getContext('2d');
    ctx.fillRect(10, 10, 100, 80);
})

function drawBackground(){
    ctx.drawImage(bgImage, 0, 0, bgImage.width, bgImage.height);
}*/

async function createChat() {
    try{
        await axios.post('http://localhost:8080/chat/create/'+props.watchId, "with="+user.value.id)
        changes.value = true
        quit()
    }catch(e){
        console.log(e)
        msg.value = 'Не удалось создать чат, попробуйте позже'
    }
}

function close(){
    msg.value = ''
}

const emit = defineEmits(['quitEvent'])

function quit(){
    emit('quitEvent', changes.value)
}
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
</style>