<template>
  <div class="messenger-app">
    <div class="ui grid" style="height: 100vh; margin: 0;">
      <SideBar 
        :thisUser="thisUser"
        :contacts="contacts"
        :selectedContactId="selectedContactId"
        @selectContact="selectContact"
        @createContact="createContact"
        @updateProfile="findThisUser"
        @quit="quit"
      />
      <ChatWindow 
        :contact="selectedContact"
        :messages="messages"
        @sendMessage="sendMessage"
      />
    </div>
  </div>
</template>

<script setup>
import router from '@/router'
import { ref, computed, onMounted, onUnmounted } from 'vue';
import SideBar from './SideBar.vue';
import ChatWindow from './ChatWindow.vue';
import axios from 'axios'
const ax = axios.create({
  baseURL: process.env.VUE_APP_SERVER_URL,
  withCredentials: true
})

const baseURL = process.env.VUE_APP_SERVER_URL;

const props = defineProps({
  thisUserId: {
    type: String,
    required: true
  }
});

let socket = {}

const thisUser = ref({})

async function findThisUser() {
  try {
    let rawUser = {}
    await ax.get(baseURL + '/api/user/find/' + props.thisUserId)
      .then(response => rawUser = response.data)
    thisUser.value = {
      id: rawUser.id,
      username: rawUser.username,
      name: rawUser.firstname + " " + rawUser.lastname,
      email: rawUser.email,
      imageUrl: rawUser.imageUrl
    }
    if(rawUser.firstname && rawUser.lastname){
      thisUser.value.avatar = rawUser.firstname.slice(0, 1) + rawUser.lastname.slice(0, 1)
    }else{
      thisUser.value.avatar = "NN"
    }
  } catch (e) {
    console.log(e)
  }
}

const contacts = ref([]);

const selectedContactId = ref(-1);

async function findContacts() {
  try {
    let rawChats = []
    await ax.get(baseURL + '/search/' + props.thisUserId)
      .then(response => rawChats = response.data.content)
    rawChats = rawChats.sort((chat1, chat2) => chat1.lastMessageTime > chat2.lastMessageTime ? 1 : -1)
    let contactsCopy = []
    console.log(rawChats)
    for(let i = 0; i < rawChats.length; i++){
      contactsCopy.push({
        id: i,
        userId: rawChats[i].userId,
        chatId: rawChats[i].chatId,
        username: rawChats[i].username,
        name: rawChats[i].firstname + " " + rawChats[i].lastname,
        lastMessage: rawChats[i].lastMessageText,
        lastMessageTime: "",
        lastMessageOwner: rawChats[i].lastMessageOwner,
        avatar: "",
        imageUrl: rawChats[i].imageUrl,
        online: rawChats[i].online,
        unreadCount: rawChats[i].unreadCount,
        lastMessageStatus: "NONE"
      })
      if(rawChats[i].firstname && rawChats[i].lastname){
        contactsCopy[i].avatar = rawChats[i].firstname.slice(0, 1) + rawChats[i].lastname.slice(0, 1)
      }else{
        contactsCopy[i].avatar = "NN"
      }
      if(!rawChats[i].lastMessageText){
        contactsCopy[i].lastMessage = "Диалог пустой"
      }
      if(rawChats[i].messageDate){
        contactsCopy[i].lastMessageTime = rawChats[i].messageDate.slice(11, 16)
      }
      if(rawChats[i].lastMessageOwner == props.thisUserId){
        if(contactsCopy[i].unreadCount != 0){
          contactsCopy[i].unreadCount = 0
          contactsCopy[i].lastMessageStatus = "UNREAD"
        }else{
          contactsCopy[i].lastMessageStatus = "READ"
        }
      }
      console.log(rawChats[i].lastMessageOwner)
    }
    contacts.value = contactsCopy
  } catch (e) {
    console.log(e)
    error()
  }
}

function findContact(id){
  for(let i = 0; i < contacts.value.length; i++){
    if(contacts.value[i].userId == id){
      return i
    }
  }
  return -1
}

const createContact = async (id) => {
  try{
    await ax.post(baseURL + '/chat/create/'+ props.thisUserId + "?with=" + id)
  }catch(e){
    console.log(e)
  }
  findContacts()

  let msg = {
    sender: props.thisUserId,
    to: id,
    action: 'CONTACT'
  }
  socket.send(JSON.stringify(msg))
}

onMounted(async () => {
  socket = await new WebSocket(process.env.VUE_APP_WEBSOCKET_URL)
  // socket = await new WebSocket('ws://localhost:80/ws')

  socket.onmessage = async (event) => {
    let msg = JSON.parse(event.data)
    console.log('Принято:\n' + JSON.stringify(msg))
    if (msg.to == props.thisUserId){
      if (msg.action == 'MESSAGE'){
        findContacts()
        if(selectedContactId.value > -1 && contacts.value[selectedContactId.value].userId == msg.sender){
          findMessages(selectedContactId.value)
        }
      }else if (msg.action == 'CONTACT'){
        findContacts()
      }else if (msg.action == 'READ'){
        let id = findContact(msg.sender)
        contacts.value[id].lastMessageStatus = "READ";
      }
    }else if (msg.to == 'CONTACTS'){
      let senderId = findContact(msg.sender)
      if(senderId != -1){
        if(msg.action == 'ONLINE'){
          contacts.value[senderId].online = true
        }else if(msg.action == 'OFFLINE'){
          contacts.value[senderId].online = false
        }else if(msg.action == 'PROFILE'){
          findContacts()
        }
      }
    }
  }
  socket.onopen = async () => {
    let msg = {
      sender: props.thisUserId,
      to: 'CONTACTS',
      action: 'ONLINE'
    }
    socket.send(JSON.stringify(msg))
    console.log('Отослано:\n' + JSON.stringify(msg))
    try{
      await ax.get(baseURL + '/api/user/online/' + props.thisUserId).then(
        resp => console.log(resp.data)
      )
    }catch(e){
      console.log(e)
    }
  }

  findThisUser()
  findContacts()
})

onUnmounted(() => {
  let msg = {
    sender: props.thisUserId,
    to: 'CONTACTS',
    action: 'OFFLINE'
  }
  socket.send(JSON.stringify(msg))
  console.log('Отослано:\n' + JSON.stringify(msg))
})

const selectedContact = computed(() => {
  return contacts.value[selectedContactId.value];
})

const selectContact = async (contactId) => {
  selectedContactId.value = contactId;
  
  let contact = contacts.value[contactId];
  if(contact.unreadCount != 0){
    contact.unreadCount = 0;
    try{
      await ax.patch(baseURL + '/chat/' + contact.chatId, {userId: props.thisUserId, newCount: 0})
        .then(resp => {console.log(resp)})
    }catch(e){
      console.log(e)
    }

    let msg = {
      sender: props.thisUserId,
      to: contacts.value[selectedContactId.value].userId,
      action: 'READ'
    }
    socket.send(JSON.stringify(msg))
  }

  findMessages(contactId)
}

const messages = ref([])

async function findMessages(contactId){
  let messagesCopy = []
  if(contactId > -1){
    let chatId = contacts.value[contactId].chatId
    let curY = new Date().getFullYear()
    let curM = new Date().getMonth() + 1
    if (curM < 10){
      curM = "0" + curM
    }
    let curD = new Date().getDate()
    try{
      let rawMessages = []
      await ax.get(baseURL + '/chat/'+ chatId + "?id=" +  props.thisUserId)
        .then(response => {console.log(JSON.stringify(response)); rawMessages = response.data.content})
      console.log(JSON.stringify(rawMessages))
      for(let i = 0; i < rawMessages.length; i++){
        messagesCopy.push({
          id: i,
          senderId: rawMessages[i].userId,
          sender: "",
          text: rawMessages[i].messageText,
          time: "",
          isOwn: false
        })
        if(rawMessages[i].userId == props.thisUserId){
          messagesCopy[i].isOwn = true
          messagesCopy[i].sender = "Вы";
        }else{
          messagesCopy[i].sender = contacts.value[contactId].name
        }
        if(curY == rawMessages[i].messageDate.slice(0, 4)){
          if(curM == rawMessages[i].messageDate.slice(5, 7) && curD == rawMessages[i].messageDate.slice(8, 10)){
            messagesCopy[i].time = rawMessages[i].messageDate.slice(11, 16)
          }else{
            messagesCopy[i].time = rawMessages[i].messageDate.slice(8, 10) + '.' + rawMessages[i].messageDate.slice(5, 7)
          }
        }else{
          messagesCopy[i].time = rawMessages[i].messageDate.slice(8, 10) + '.' + rawMessages[i].messageDate.slice(5, 7) + '.' + rawMessages[i].messageDate.slice(2, 4)
        }
        console.log(rawMessages[i].messageDate)
      }
    }catch(e){
      console.log(e)
      error()
    }
  }
  messages.value = messagesCopy
}

const sendMessage = async (message) => {
  const newMessage = {
    id: messages.value.length + 1,
    senderId: props.thisUserId,
    sender: 'Вы',
    text: message.text,
    time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
    isOwn: true
  }
  
  messages.value.push(newMessage);
  
  const contact = contacts.value[selectedContactId.value];
  contact.lastMessage = message.text;
  contact.lastMessageTime = 'Прямо сейчас';
  contact.lastMessageStatus = "UNREAD";

  const postMessage = {
    userId: props.thisUserId,
    messageText: message.text
  }

  
  try{
    await ax.post(baseURL + '/chat/'+ contacts.value[selectedContactId.value].chatId, postMessage)
  }catch(e){
    console.log(e)
  }

  let msg = {
    sender: props.thisUserId,
    to: contacts.value[selectedContactId.value].userId,
    action: 'MESSAGE'
  }
  socket.send(JSON.stringify(msg))
}

async function quit() {
  let msg = {
    sender: props.thisUserId,
    to: 'CONTACTS',
    action: 'OFFLINE'
  }
  socket.send(JSON.stringify(msg))
  console.log('Отослано:\n' + JSON.stringify(msg))

  try{
    await ax.get(baseURL + '/api/user/offline/' + props.thisUserId).then(
      resp => console.log(resp.data)
    )
  }catch(e){
    console.log(e)
  }

  router.push({name: 'Home'})
}

function error(){
  router.push({name: 'Error'})
}
</script>

<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    position: fixed;
    top: 0px;
    left: 0px;
    height: 100vh;
    width: 100%;
    font-family: 'Lato', 'Helvetica Neue', Arial, Helvetica, sans-serif;
    overflow: hidden;
}

.messenger-app {
    position: fixed;
    top: 0px;
    left: 0px;
    height: 100vh;
    width: 100%;
    overflow: hidden;
}

.ui.grid {
    height: 100%;
}

.ui.grid > .column:not(.row) {
    padding: 0;
}
</style>