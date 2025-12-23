<template>
  <div class="four wide column sidebar-column">
    <div class="sidebar">
      <div class="ui attached stackable menu">
        <a class="item" @click="showProfile = true">
          <i class="user icon"></i>
          <b>{{ props.thisUser.avatar }}</b>
        </a>
        <div class="item">
          <h3 class="ui header">{{addContact ? "Новый чат" : "Контакты"}}</h3>
        </div>
        <div class="right item">
          <i v-if="!addContact" class="plus icon" @click="addContact = true"></i> 
          <i v-else class="x icon" @click="addContact = false"></i>
        </div>
      </div>
      
      <div class="ui fluid icon input search-input">
        <input 
          type="text" 
          placeholder="Поиск контактов..." 
          v-model="searchQuery"
        >
        <i class="search icon"></i>
      </div>
      
      <div v-if="!addContact" class="contacts-list">
        <ContactItem
          v-for="contact in filteredContacts"
          :key="contact.id"
          :contact="contact"
          :isSelected="contact.id === selectedContactId"
          @click="$emit('selectContact', contact.id)"
        />
        <div v-if="filteredContacts.length == 0" class="center-vert">
          <div>
            <h3 class="ui header">Контактов нет</h3>
            <button class="ui labeled icon primary button" @click="addContact = true">
              <i class="search icon"></i>
              Глобальный поиск
            </button>
          </div>
        </div>
      </div>
      <div v-else class="contacts-list">
        <GlobalItem
          v-for="contact in filteredGlobal"
          :key="contact.id"
          :contact="contact"
          @createContact="createContact(contact.userId, contact.id)"
        />
      </div>
    </div>
  </div>
  <MyProfile v-if="showProfile" :user="props.thisUser"
    @closeWindow="showProfile = false" @updateProfile="$emit('updateProfile')" @quit="$emit('quit')"/>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import ContactItem from './ContactItem.vue';
import MyProfile from './MyProfile.vue';
import GlobalItem from './GlobalItem.vue';
import axios from 'axios'
const ax = axios.create({
    baseURL: process.env.VUE_APP_SERVER_URL,
    withCredentials: true
})

const baseURL = process.env.VUE_APP_SERVER_URL

const props = defineProps({
  thisUser: {
    type: Object,
    required: true
  },
  contacts: {
    type: Array,
    required: true
  },
  selectedContactId: {
    type: Number,
    required: true
  }
});

const emit = defineEmits(['selectContact', 'createContact', 'updateProfile', 'quit']);

const showProfile = ref(false)
const addContact = ref(false)

const searchQuery = ref('');

const filteredContacts = computed(() => {
  if (!searchQuery.value) {
    return props.contacts;
  }
  
  const query = searchQuery.value.toLowerCase();
  return props.contacts.filter(contact =>
    contact.name.toLowerCase().includes(query)
  );
});

const globalContacts = ref([])
let contactsSet = new Set()

async function initSet(){
  if(contactsSet.length != props.contacts.length){
    for(let i = 0; i < props.contacts.length; i++){
      contactsSet.add(props.contacts[i].userId)
    }
    findGlobal()
    findFilterGlobal()
  }
}

async function createContact(id, index){
  contactsSet.add(id)
  globalContacts.value[index].inContacts = true
  emit('createContact', id)
}

async function findGlobal() {
  try{
    let rawChats = []
    let globalContactsCopy = []
    await ax.get(baseURL + '/api/user/findAll')
      .then(response => rawChats = response.data.content)
    for(let i = 0; i < rawChats.length; i++){
      globalContactsCopy.push({
        id: i,
        userId: rawChats[i].id,
        username: rawChats[i].username,
        name: rawChats[i].firstname + " " + rawChats[i].lastname,
        email: rawChats[i].email,
        avatar: "",
        inContacts: contactsSet.has(rawChats[i].id)
      })
      if(rawChats[i].firstname && rawChats[i].lastname){
        globalContactsCopy[i].avatar = rawChats[i].firstname.slice(0, 1) + rawChats[i].lastname.slice(0, 1)
      }else{
        globalContactsCopy[i].avatar = "NN"
      }
    }
    globalContacts.value = globalContactsCopy
    console.log(globalContacts.value)
  }catch(e){
    console.log(e)
  }
}

const filteredGlobal = ref(globalContacts.value)

async function findFilterGlobal() {
  if (!searchQuery.value) {
    filteredGlobal.value = globalContacts.value;
  }else{
    const query = searchQuery.value.toLowerCase();
    if(query){
      try{
        let rawChats = []
        let filteredContactsCopy = []
        await ax.get(baseURL + '/search/global/' + query)
          .then(response => rawChats = response.data.content)
        console.log(rawChats)
        for(let i = 0; i < rawChats.length; i++){
          filteredContactsCopy.push({
            id: i,
            userId: rawChats[i].userId,
            username: rawChats[i].username,
            name: rawChats[i].firstname + " " + rawChats[i].lastname,
            email: "",
            avatar: "",
            inContacts: contactsSet.has(rawChats[i].userId)
          })
          if(rawChats[i].firstname && rawChats[i].lastname){
            filteredContactsCopy[i].avatar = rawChats[i].firstname.slice(0, 1) + rawChats[i].lastname.slice(0, 1)
          }else{
            filteredContactsCopy[i].avatar = "NN"
          }
        }
        filteredGlobal.value = filteredContactsCopy
      }catch(e){
        console.log(e)
      }
    }
  }
}

onMounted(async () => {
  initSet()
})

watch(props.contacts, initSet)
watch(addContact, findGlobal)
watch(searchQuery, findFilterGlobal)
</script>

<style scoped>
.sidebar-column {
  border-right: 1px solid #ddd;
  background: #f9fafb;
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: hidden;
}

.sidebar {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid #ddd;
  background: white;
}

.sidebar-header h3 {
  margin: 0;
  color: #000000;
}

.search-input {
  margin: 15px 0px 0px 0px;
  margin-bottom: 10px;
}

.contacts-list {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
}

.contacts-list::-webkit-scrollbar {
  width: 6px;
}

.contacts-list::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.contacts-list::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.contacts-list::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

.center-vert{
  display: grid;
  place-items: center;
  height: 100%;
}
</style>
