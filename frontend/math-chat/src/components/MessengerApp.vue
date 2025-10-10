<template>
    <div class="ui attached stackable menu">
        <div class="ui container">
            <a class="item">
                <i class="user icon"></i> {{user.login}} {{user.email}}
            </a>
            <a class="right item"  @click="quit">
                <i class="sign-out icon"></i> Выход
            </a>
        </div>
    </div>
    <div class="ui container">
        <div class="ui two column grid">
            <div class="six wide column">
                <div class="ui segment">
                    <div class="ui search">
                        <div class="ui icon input">
                            <input class="prompt" type="text" placeholder="Найти юзера...">
                            <i class="search icon"></i>
                        </div>
                        <div class="results"></div>
                    </div>
                    <div class="ui segment" v-for="user in chatUsers" :key="user.id">
                        <p>{{user.login}}</p>
                        <p>{{user.email}}</p>
                        <button @click="updateChat(user.id, user.chatId)">Открыть диалог</button>
                    </div>
                </div>
            </div>
            <div class="ten wide column">
                <div class="ui fluid container" v-if="chatId.id != -1">
                    <h2 class="ui center header">{{chatUser.login}}</h2>
                    <div v-for="message in messages" :key="message.id">
                        <div class="ui left aligned segment" v-if="message.userId != userId">
                            <p>{{message.messageText}}</p>
                        </div>
                        <div class="ui right aligned segment" v-else>
                            <p>{{message.messageText}}</p>
                        </div>
                    </div>
                    <div class="ui fluid action input" v-if="chatId.id != -1">
                        <input type="text" v-model="textMessage" placeholder="Сообщение">
                        <button class="ui button" @click="post">Отправить</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="ui container">
        <div>
            <p> Список пользователей </p>
            <p>{{allUsers1}}</p>
        </div>
        <div>
            <p> Список чатов пользователя </p>
            <p>{{chats}}</p>
        </div>
        <div>
            <p> Сообщения </p>
            <button @click="getMessages">Открыть беседу</button>
            <p>{{messages}}</p>
            <p>{{userId}}</p>
            <p>{{chatId}}</p>
        </div>
    </div>
</template>

<script setup>
import axios from 'axios'
import { ref, onMounted } from 'vue'

let allUsers = ref([])
let allUsers1 = ref([])
let user = ref({
    id: 0,
    login: '',
    email: ''
})

let chats = ref([])
let chatUsers = ref([])
let chatUser = ref({
    id: 0,
    login: '',
    password: ''
})
let chatId = -1

let messages = ref([])
const props = defineProps({
  userId: {
    type: Number,
    required: true
  }
})
const textMessage = ref('')

const emit = defineEmits(['quitEvent'])

onMounted(async () => {
    try {
        await axios.get('http://localhost:8080/api/user/findAll')
            .then(response => allUsers.value = response.data)
    } catch (e) {
        console.log(e)
    }
    for(let i = 0; i < allUsers.value.length; i++){
        allUsers1.value.push({
            id: allUsers.value[i].id,
            login: allUsers.value[i].firstname + " " + allUsers.value[i].lastname,
            email: allUsers.value[i].email
        })
    }
    user = allUsers1.value[props.userId-1]
    try {
        await axios.get('http://localhost:8080/api/messenger/' + props.userId)
            .then(response => chats.value = response.data)
    } catch (e) {
        console.log(e)
    }
    for(let i = 0; i < chats.value.length; i++){
        chatUsers.value.push({
            id: (chats.value[i].userIdMin + chats.value[i].userIdMax - props.userId),
            login: allUsers1.value[(chats.value[i].userIdMin + chats.value[i].userIdMax - props.userId)].login,
            email: allUsers1.value[(chats.value[i].userIdMin + chats.value[i].userIdMax - props.userId)].email,
            chatId: chats.value[i].id
        })
    }
})

async function updateChat(id1, id2) {
    chatUser = allUsers1.value[id1]
    chatId = id2
    try {
        await axios.get('http://localhost:8080/api/messenger/chat/' + chatId)
            .then(response => messages.value = response.data)
    } catch (e) {
        alert(e)
    }
}

function post(){
    messages.value.push({
        id: messages.value.length,
        userId: props.userId,
        chatId: chatId,
        messageText: textMessage.value
    })
    textMessage.value = ''
}

function quit() {
    emit('quitEvent', 'auth')
}

var content = [
    {
      title: 'Horse'
    },
    {
      title: 'Cow'
    }
];

$('.ui.search')
  .search({
    source: content,
    fullTextSearch: false
  })
;
</script>