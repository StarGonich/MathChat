<template>
    <div class="box">
    <div class="top_menu">
        <div :class="menuClass">
            <div class="ui container">
                <a class="item" @click="showProf(user, 'own')">
                    <i class="user icon"></i> {{user.login}} {{user.email}}
                </a>
                <a class="right item"  @click="switchDark">
                    <i class="moon icon"></i>
                    <span v-if="dark"> Светлая тема </span>
                    <span v-else> Тёмная тема </span>
                </a>
                <a class="item"  @click="quit">
                    <i class="sign-out icon"></i> Выход
                </a>
            </div>
        </div>
    </div>
    <div class="grid">
        <div class="left_part">
            <div :class="segmClassSec">
                <div :class="segmClassBasic">
                    <div class="ui search">
                        <div :class="inputClass" @click="showFind">
                            <input class="prompt" type="text" placeholder="Найти юзера...">
                            <i class="search icon"></i>
                        </div>
                        <div class="results"></div>
                    </div>
                </div>
                <div class="left_list">
                    <div v-for="user in chatUsers" :key="user.id">
                        <div :class="segmClassTert">
                            <div class="clickable">
                                <div class="ui two column grid" @click="updateChat(user.chatId, user)">
                                    <div class="three wide column">
                                        <v-stage :config="stageSize">
                                            <v-layer>
                                                <v-image
                                                    :config="{
                                                    x: 3,
                                                    y: 3,
                                                    image: defImage,
                                                    width: 60,
                                                    height: 60,
                                                    cornerRadius: 30
                                                    }"
                                                />
                                            </v-layer>
                                        </v-stage>
                                    </div>
                                    <div class="thirteen wide column">
                                        <p align="left">
                                            <b>{{formatLdots(user.login, 25)}}</b><br/>
                                            <b>{{formatLdots(user.email, 25)}}</b><br/>
                                            {{formatLdots(user.lastMessage, 25)}}
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="right_part">
            <div :class="segmClassSec">
                <div class="right_chat_title">
                    <div class='title'>
                    <h2 class="ui center header" @click.prevent="showProf(chatUser, 'chat')">
                        <div class="clickable">
                            {{chatUser.login}}
                        </div>
                    </h2>
                    </div>
                    <div class="right_chat">
                        <div v-for="message in messages" :key="message.none" class="message">
                            <div :class="position(message.userId)">
                                <div v-for="line in formatArr(message.messageText, 45)" :key="line.id">
                                    <div v-if="line.isLatex">
                                        <vue-latex  :expression="line.content"/>
                                    </div>
                                    <div v-else>{{ line.content }}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="ui fluid action input">
                    <input type="text" v-model="textMessage" placeholder="Сообщение">
                    <button class="ui button" @click.prevent="post" :disabled="!textMessage || chatId == -1">Отправить</button>
                </div>
            </div>
        </div>
    </div>
    <ProfileApp :user="profUser" :type="profType" :watchId="userId" v-if="openProfile" @quitEvent="(ch) => updateProfApp(ch)" />
    <FindApp :users="allUsers" v-if="openFind" @quitEvent="openFind = false" @selectEvent="(id) => select(id)"/>
    </div>
</template>

<script setup>
import router from '@/router'
import { ref, onMounted, computed } from 'vue'
import { useImage } from 'vue-konva';
import ProfileApp from './ProfileApp.vue'
import FindApp from './FindApp.vue'
import axios from 'axios'
const ax = axios.create({
    baseURL: 'http://localhost:8080',
    withCredentials: true
})

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
let chatId = ref(-1)

let messages = ref([])
const props = defineProps({
  userId: {
    type: Number,
    required: true
  }
})
const textMessage = ref('')
const [defImage] = useImage('https://wallpapers.com/images/hd/user-profile-silhouette-icon-8klsuuvmvdroxn2t.jpg');

let openProfile = ref(false)
let profUser = ref({})
let profType = ref('none')

let socket = {}

onMounted(async () => {
    socket = new WebSocket('ws://localhost:8080/ws')

    socket.onmessage = async () => {
        try{
            await ax.get('http://localhost:8080/search/' + props.userId)
                .then(response => chats.value = response.data)
            if(chatId.value != -1){
                await ax.get('http://localhost:8080/chat/' + chatId.value + '?id=' + props.userId)
                    .then(response => messages.value = response.data)
            }
        }catch(e){
            console.log(e)
        }
    }

    try {
        await ax.get('http://localhost:8080/api/user/findAll')
            .then(response => allUsers.value = response.data)
    } catch (e) {
        console.log(e)
    }

    console.log(allUsers)
    for(let i = 0; i < allUsers.value.length; i++){
        allUsers1.value.push({
            id: allUsers.value[i].id,
            login: allUsers.value[i].firstname + " " + allUsers.value[i].lastname,
            email: allUsers.value[i].email
        })
        console.log(allUsers1)
    }

    try {
        await ax.get('http://localhost:8080/api/user/find/' + props.userId)
            .then(response => user.value = response.data)
    } catch (e) {
        console.log(e)
    }
    
    try {
        await ax.get('http://localhost:8080/search/' + props.userId)
            .then(response => chats.value = response.data)
    } catch (e) {
        console.log(e)
    }
    for(let i = 0; i < chats.value.length; i++){
        try{await ax.get('http://localhost:8080/api/user/find/' + (chats.value[i].userId))
            .then(response => chatUsers.value.push({
                id: response.data.id,
                login: response.data.firstname + " " + response.data.lastname,
                email: response.data.email,
                chatId: chats.value[i].chatId,
                lastMessage: chats.value[i].lastMessageText
            }))
        }catch(e){
            console.log(e)
        }
    }
})

async function updateChat(idCh, us) {
    chatId.value = idCh
    chatUser = us
    try {
        await ax.get('http://localhost:8080/chat/' + chatId.value + '?id=' + props.userId)
            .then(response => messages.value = response.data)
    } catch (e) {
        console.log(e)
    }
}

function formatLdots(str, n){
    if(str && str.length > n){
        return str.slice(0, n-3) + '...'
    }else{
        return str
    }
}

function formatArr(str, n){
    let ss = []
    let s = str.split(' ')
    let i = 0
    let j = 0
    let cur = ''
    let latex = false
    while(i < s.length){
        if(s[i] == '$'){
            if(latex){
                latex = false
                ss.push(
                    {
                        id: j,
                        content: cur.slice(0),
                        isLatex: true
                    }
                )
                j++
                cur = ''
            }else{
                latex = true
                if(cur != ''){
                    ss.push(
                        {
                            id: j,
                            content: cur.slice(0),
                            isLatex: false
                        }
                    )
                    j++
                    cur = ''
                }
            }
        }else if(cur.length + s[i].length <= n){
            cur += ' ' + s[i]
        }else if(!latex){
            ss.push(
                {
                    id: j,
                    content: cur.slice(0),
                    isLatex: false
                }
            )
            j++
            cur = s[i]
        }
        i++
    }
    ss.push(
        {
            id: j,
            content: cur.slice(0),
            isLatex: false
        }
    )
    return ss
}

function position(id){
    if(id == props.userId){
        return "ui right floated compact blue inverted segment"
    }else{
        return "ui left floated compact segment"
    }
}

async function post(){
    try{
        await ax.post('http://localhost:8080/chat/' + chatId.value, {
            id: messages.value.length,
            userId: props.userId,
            chatId: chatId.value,
            messageText: textMessage.value,
            messageDate: new Date()
        })
        await ax.get('http://localhost:8080/chat/' + chatId.value + '?id=' + props.userId)
            .then(response => messages.value = response.data)
    }catch(e){
        console.log(e)
    }
    textMessage.value = ''
    socket.send(JSON.stringify('msg'))
}

async function showProf(user, type){
    if(type == 'chat'){
        try{
            await ax.get('http://localhost:8080/api/user/find/' + user.id)
            .then(response => user = response.data)
        }catch(e){
            console.log(e)
        }
    }
    profUser.value = user
    profType.value = type
    openProfile.value = true
}

async function updateProfApp(ch){
    openProfile.value = false
    if(ch){
        if(profType.value == "own"){
            try {
                await ax.get('http://localhost:8080/api/user/find/' + props.userId)
                    .then(response => user.value = response.data)
            } catch (e) {
                console.log(e)
            }
        }else if(profType.value == "stranger"){
            try {
                await ax.get('http://localhost:8080/search/' + props.userId)
                    .then(response => chats.value = response.data)
                await ax.get('http://localhost:8080/api/user/find/' + (chats.value[chats.value.length-1].userId))
                    .then(response => chatUsers.value.push({
                        id: response.data.id,
                        login: response.data.firstname + " " + response.data.lastname,
                        email: response.data.email,
                        chatId: chats.value[chats.value.length-1].chatId,
                        lastMessage: ''
                    }), )
            } catch (e) {
                console.log(e)
            }
        }
    }
}

const openFind = ref(false)

function showFind(){
    openFind.value = true
}

async function select(id){
    openFind.value = false
    if (id != -1){
        let user = {}
        try {
            await ax.get('http://localhost:8080/api/user/find/' + id)
                .then(response => user = response.data)
        } catch (e) {
            console.log(e)
        }
        showProf(user, 'stranger')
    }
}

function quit() {
    router.push({name: 'Home'})
}

const stageSize = {
  width: 66,
  height: 66
};

const dark = ref(false)
const menuClass = computed(() =>{
    return dark.value ? "ui attached stackable inverted menu" : "ui attached stackable menu"
})
const segmClassSec = computed(() =>{
    return dark.value ? "ui inverted secondary segment" : "ui segment"
})
const segmClassTert = computed(() =>{
    return dark.value ? "ui inverted tertiary segment" : "ui segment"
})
const segmClassBasic = computed(() =>{
    return dark.value ? "ui inverted secondary basic segment" : "ui basic segment"
})
const inputClass = computed(() =>{
    return dark.value ? "ui icon inverted input" : "ui icon input"
})
function switchDark(){
    dark.value = !dark.value
}

document.body.style.overflow = 'hidden'
</script>

<style scoped>
.box {
  position: fixed;
  top:0;
  right: 0;
  bottom: 0;
  left: 0;
}

.top_menu {
  position: fixed;
  top: 0px;
  width: 100%;
}

.grid{
    position: auto;
    width: 100%;
    margin-top: 41px;
    display: flex;
    justify-content: flex-start;
    flex-direction: row;
}

.left_part{
    width: 40%;
}

.left_list{
    height: 475px;
    overflow: auto;
    scrollbar-width: thin;
    width: 100%;
}

.right_part{
    width: 60%;
}

.right_chat_title{
    width: 100%;
    height: 496px;
}

.title{
    display: flex;
    justify-content: flex-start;
    flex-direction: column;
    width: 100%;
    margin-top: 20px;
    margin-bottom: 30px;
    height: 20px;
}

.right_chat{
    background-color: rgb(243, 243, 243);
    display: flex;
    justify-content: flex-start;
    flex-direction: column;
    width: 100%;
    height: 434px;
    overflow: auto;
    scrollbar-width: thin;
}

.message{
    margin-top: 5px;
    margin-bottom: 5px;
}

.clickable{
    cursor: pointer;
}
</style>