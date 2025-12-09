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
import { ref, computed, onMounted } from 'vue';
import SideBar from './SideBar.vue';
import ChatWindow from './ChatWindow.vue';
import axios from 'axios'
const ax = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true
})

const props = defineProps({
  thisUserId: {
    type: String,
    required: true
  }
});

const thisUser = ref({})

async function findThisUser() {
  try {
    let rawUser = {}
    await ax.get('http://localhost:8080/api/user/find/' + props.thisUserId)
      .then(response => rawUser = response.data)
    thisUser.value = {
      id: rawUser.id,
      username: rawUser.username,
      name: rawUser.firstname + " " + rawUser.lastname,
      email: rawUser.email,
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

const contacts = ref([
  {
    id: 0,
    name: 'Alice Johnson',
    avatar: 'AJ',
    online: true,
    lastMessage: 'Hey! How are you?',
    unreadCount: 2,
    lastMessageTime: '2m ago'
  },
  {
    id: 1,
    name: 'Alice Johnson',
    avatar: 'AJ',
    online: true,
    lastMessage: 'Hey! How are you?',
    unreadCount: 2,
    lastMessageTime: '2m ago'
  },
  {
    id: 2,
    name: 'Bob Smith',
    avatar: 'BS',
    online: true,
    lastMessage: 'Can you check the equation?',
    unreadCount: 0,
    lastMessageTime: '1h ago'
  },
  {
    id: 3,
    name: 'Charlie Brown',
    avatar: 'CB',
    online: false,
    lastMessage: 'Thanks for the help!',
    unreadCount: 0,
    lastMessageTime: '3h ago'
  },
  {
    id: 4,
    name: 'Diana Prince',
    avatar: 'DP',
    online: true,
    lastMessage: 'See you tomorrow',
    unreadCount: 5,
    lastMessageTime: '5h ago'
  },
  {
    id: 5,
    name: 'Eve Anderson',
    avatar: 'EA',
    online: false,
    lastMessage: 'Got it!',
    unreadCount: 0,
    lastMessageTime: '1d ago'
  }
]);

const selectedContactId = ref(-1);

async function findContacts() {
  try {
    let rawChats = []
    await ax.get('http://localhost:8080/search/' + props.thisUserId)
      .then(response => rawChats = response.data)
    contacts.value = []
    console.log(rawChats)
    for(let i = 0; i < rawChats.length; i++){
      contacts.value.push({
        id: i,
        userId: rawChats[i].userId,
        chatId: rawChats[i].chatId,
        username: rawChats[i].username,
        name: rawChats[i].firstname + " " + rawChats[i].lastname,
        lastMessage: rawChats[i].lastMessageText,
        lastMessageTime: "",
        avatar: ""
      })
      if(rawChats[i].firstname && rawChats[i].lastname){
        contacts.value[i].avatar = rawChats[i].firstname.slice(0, 1) + rawChats[i].lastname.slice(0, 1)
      }else{
        contacts.value[i].avatar = "NN"
      }
      if(!rawChats[i].lastMessageText){
        contacts.value[i].lastMessage = "Диалог пустой"
      }
      if(rawChats[i].messageDate){
        contacts.value[i].lastMessageTime = rawChats[i].messageDate.slice(11, 16)
      }
    }
  } catch (e) {
    console.log(e)
  }
}

const createContact = async (id) => {
  try{
    await ax.post('http://localhost:8080/chat/create/'+ props.thisUserId + "?with=" + id)
    findContacts()
  }catch(e){
    console.log(e)
  }
}

/*const allMessages = ref({
  0: [
    {
      id: 1,
      sender: 'Alice Johnson',
      text: 'Hey! How are you?',
      time: '10:30 AM',
      isOwn: false
    }
  ],
  1: [
    {
      id: 1,
      sender: 'Alice Johnson',
      text: 'Hey! How are you?',
      time: '10:30 AM',
      isOwn: false
    },
    {
      id: 2,
      sender: 'You',
      text: 'I\'m good! Working on some math problems.',
      time: '10:32 AM',
      isOwn: true
    },
    {
      id: 3,
      sender: 'Alice Johnson',
      text: 'Need any help with them?',
      time: '10:33 AM',
      isOwn: false
    }
  ],
  2: [
    {
      id: 1,
      sender: 'Bob Smith',
      text: 'Can you check this equation?',
      time: '9:15 AM',
      isOwn: false
    },
    {
      id: 2,
      sender: 'Bob Smith',
      latex: 'E = mc^2',
      time: '9:15 AM',
      isOwn: false
    },
    {
      id: 3,
      sender: 'You',
      text: 'Looks perfect!',
      time: '9:20 AM',
      isOwn: true
    }
  ],
  3: [
    {
      id: 1,
      sender: 'You',
      text: 'Let me know if you need anything else',
      time: '8:00 AM',
      isOwn: true
    },
    {
      id: 2,
      sender: 'Charlie Brown',
      text: 'Thanks for the help!',
      time: '8:05 AM',
      isOwn: false
    }
  ],
  4: [
    {
      id: 1,
      sender: 'Diana Prince',
      text: 'See you tomorrow',
      time: '6:30 AM',
      isOwn: false
    },
    {
      id: 2,
      sender: 'Diana Prince',
      latex: '\\int_0^\\infty e^{-x^2} dx = \\frac{\\sqrt{\\pi}}{2}',
      time: '6:31 AM',
      isOwn: false
    }
  ],
  5: [
    {
      id: 1,
      sender: 'You',
      text: 'Don\'t forget about the meeting',
      time: 'Yesterday',
      isOwn: true
    },
    {
      id: 2,
      sender: 'Eve Anderson',
      text: 'Got it!',
      time: 'Yesterday',
      isOwn: false
    }
  ]
})*/

onMounted(async () => {
  findThisUser()
  findContacts()
})

const selectedContact = computed(() => {
  return contacts.value[selectedContactId.value];
})

/*const messages = computed(() => {
  return allMessages.value[selectedContactId.value] || [];
})*/

const selectContact = async (contactId) => {
  selectedContactId.value = contactId;
  
  let contact = contacts.value[contactId];
  contact.unreadCount = 0;

  findMessages(contactId)
}

const messages = ref([])

async function findMessages(contactId){
  messages.value = []
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
      await ax.get('http://localhost:8080/chat/'+ chatId + "?id=" +  props.thisUserId)
        .then(response => rawMessages = response.data)
      for(let i = 0; i < rawMessages.length; i++){
        messages.value.push({
          id: i,
          senderId: rawMessages[i].userId,
          sender: "",
          text: rawMessages[i].messageText,
          time: "",
          isOwn: false
        })
        if(rawMessages[i].userId == props.thisUserId){
          messages.value[i].isOwn = true
          messages.value[i].sender = "Вы";
        }else{
          messages.value[i].sender = contacts.value[contactId].name
        }
        if(curY == rawMessages[i].messageDate.slice(0, 4) && 1 == 0){
          if(curM == rawMessages[i].messageDate.slice(5, 7) && curD == rawMessages[i].messageDate.slice(8, 10)){
            messages.value[i].time = rawMessages[i].messageDate.slice(11, 16)
          }else{
            messages.value[i].time = rawMessages[i].messageDate.slice(8, 10) + '.' + rawMessages[i].messageDate.slice(5, 7)
          }
        }else{
          messages.value[i].time = rawMessages[i].messageDate.slice(8, 10) + '.' + rawMessages[i].messageDate.slice(5, 7) + '.' + rawMessages[i].messageDate.slice(2, 4)
        }
        console.log(rawMessages[i].messageDate)
      }
    }catch(e){
      console.log(e)
    }
  }
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

  const postMessage = {
    userId: props.thisUserId,
    messageText: message.text
  }

  await ax.post('http://localhost:8080/chat/'+ contacts.value[selectedContactId.value].chatId, postMessage)
}

function quit() {
  router.push({name: 'Home'})
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