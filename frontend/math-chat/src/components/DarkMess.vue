<template>
    <div class="top_menu">
        <div class="ui attached stackable inverted menu">
            <div class="ui container">
                <a class="item" @click="showProf(user, 'own')">
                    <i class="user icon"></i> {{user.login}} {{user.email}}
                </a>
                <a class="right item"  @click="quit">
                    <i class="sign-out icon"></i> Выход
                </a>
            </div>
        </div>
    </div>
    <div class="grid">
        <div class="left_part">
            <div class="ui inverted segment">
                <div class="ui basic inverted segment">
                    <div class="ui search">
                        <div class="ui icon input" @click="showFind">
                            <input class="prompt" type="text" placeholder="Найти юзера...">
                            <i class="search icon"></i>
                        </div>
                        <div class="results"></div>
                    </div>
                </div>
                <div class="left_list">
                    <div v-for="user in chatUsers" :key="user.id">
                        <div class="ui grey inverted segment">
                            <div class="clickable">
                                <div class="ui two column grid" @click="updateChat(user.chatId)">
                                    <div class="four wide column">
                                        <img src="./static/images/user.png" class="image" height="60px">
                                    </div>
                                    <div class="twelve wide column">
                                        <p>
                                            {{formatLdots(user.login, 25)}}<br/>
                                            {{formatLdots(user.email, 25)}}<br/>
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
            <div class="ui fluid inverted segment">
                <div class="right_chat_title">
                    <h2 class="ui center header" @click.prevent="showProf(chatUser, 'chat')">
                        <div class="clickable">
                            {{chatUser.login}}
                        </div>
                    </h2>
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
</template>

<script setup>
import axios from 'axios'
import { ref, onMounted } from 'vue'
import ProfileApp from './ProfileApp.vue'
import FindApp from './FindApp.vue'

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

let openProfile = ref(false)
let profUser = ref({})
let profType = ref('none')

const emit = defineEmits(['quitEvent'])

onMounted(async () => {
    try {
        await axios.get('http://localhost:8080/api/user/findAll')
            .then(response => allUsers.value = response.data)
    } catch (e) {
        console.log(e)
        allUsers.value = [
            {
                id: 1,
                firstname: 'Алиса',
                lastname: 'Артемьева',
                email: 'a@mail.ru'
            },
            {
                id: 2,
                firstname: 'Боб',
                lastname: 'Бутчер',
                email: 'b@mail.ru'
            },
            {
                id: 3,
                firstname: 'Витя',
                lastname: 'Величайший',
                email: 'v@mail.ru'
            },
            {
                id: 4,
                firstname: 'Длинный',
                lastname:  'Очень длииииииииииииииииииииииинный ник',
                email:  'dddddddddddddddddddddddddddddddddddddddd@mail.ru'
            },
            {
                id: 5,
                firstname: 'Глеб',
                lastname: 'Горячий',
                email: 'g@mail.ru'
            },
            {
                id: 6,
                firstname: 'Егор',
                lastname: 'Елесин',
                email: 'e@mail.ru'
            },
            {
                id: 7,
                firstname: 'Ёжик',
                lastname: 'Ёлочный',
                email: 'yo@mail.ru'
            },
            {
                id: 8,
                firstname: 'Жора',
                lastname: 'Жирный',
                email: 'j@mail.ru'
            },
            {
                id: 9,
                firstname: 'Зина',
                lastname: 'Зиновьева',
                email: 'z@mail.ru'
            },
            {
                id: 10,
                firstname: 'Игорь',
                lastname: 'Иванов',
                email: 'i@mail.ru'
            },
            {
                id: 11,
                firstname: 'Йорик',
                lastname: 'Йог',
                email: 'y@mail.ru'
            },
            {
                id: 12,
                firstname:'Кирилл',
                lastname: 'Капустин',
                email: 'k@mail.ru'
            },
            {
                id: 13,
                firstname: 'Лида',
                lastname: 'Лосева',
                email: 'l@mail.ru'
            },
            {
                id: 14,
                firstname: 'Матвей',
                lastname: 'Мальцев',
                email:  'm@mail.ru'
            },
            {
                id: 15,
                firstname: 'Никита',
                lastname: 'Носа',
                email: 'n@mail.ru'
            },
            {
                id: 16,
                firstname: 'Олег',
                lastname: 'Оботуров',
                email: 'o@mail.ru'
            }
        ]
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
        await axios.get('http://localhost:8080/api/user/find/' + props.userId)
            .then(response => user.value = response.data)
    } catch (e) {
        console.log(e)
        user.value = allUsers1.value[props.userId-1]
    }
    try {
        await axios.get('http://localhost:8080/search/' + props.userId)
            .then(response => chats.value = response.data)
    } catch (e) {
        console.log(e)
        chats.value = [
            {
                id: 1,
                userIdMin: 1,
                userIdMax: 2
            },
            {
                id: 2,
                userIdMin: 1,
                userIdMax: 3
            },
            {
                id: 3,
                userIdMin: 1,
                userIdMax: 4
            },
            {
                id: 4,
                userIdMin: 1,
                userIdMax: 5
            },
            {
                id: 5,
                userIdMin: 1,
                userIdMax: 6
            },
            {
                id: 6,
                userIdMin: 1,
                userIdMax: 7
            }
        ]
    }
    for(let i = 0; i < chats.value.length; i++){
        try{await axios.get('http://localhost:8080/api/user/find/' + (chats.value[i].userId))
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

async function updateChat(idCh) {
    chatId.value = idCh
    try {
        await axios.get('http://localhost:8080/api/user/find/' + (chats.value[idCh].userId))
            .then(response => chatUser.value = {
                id: response.data.id,
                login: response.data.firstname + " " + response.data.lastname,
                email: response.data.email,
                chatId: idCh,
                lastMessage: ''
            })
        await axios.get('http://localhost:8080/chat/' + chatId.value)
            .then(response => messages.value = response.data)
    } catch (e) {
        console.log(e)
        if(chatId.value == 0){
            messages.value = [
                {
                    id: 0,
                    chatId: 0,
                    userId: 0,
                    messageText: 'Привет! Как планы на вечер?'
                },
                {
                    id: 1,
                    chatId: 0,
                    userId: 1,
                    messageText: 'Привет! Пока свободен. А что?'
                },
                {
                    id: 2,
                    chatId: 0,
                    userId: 0,
                    messageText: 'Да думаю сходить в тот новый бар на Пестеля. Соскучилась по хорошему бургеру)'
                },
                {
                    id: 3,
                    chatId: 0,
                    userId: 1,
                    messageText: 'О, я как раз про него читал! Иду?'
                },
                {
                    id: 4,
                    chatId: 0,
                    userId: 0,
                    messageText: 'Конечно! Встречаемся в семь у входа?'
                },
                {
                    id: 5,
                    chatId: 0,
                    userId: 1,
                    messageText: 'Договорились. Только я с работы могу немного задержаться.'
                },
                {
                    id: 6,
                    chatId: 0,
                    userId: 0,
                    messageText: 'Ничего страшного. Я как раз успею заскочить домой. Тогда в семь!'
                },
                {
                    id: 7,
                    chatId: 0,
                    userId: 1,
                    messageText: 'Ага! Увидимся'
                }
            ]
        }else if (chatId.value == 1){
            messages.value = [
                {
                    id: 0,
                    chatId: 1,
                    userId: 0,
                    messageText: 'Мария, добрый день. Выслали презентацию клиенту?'
                },
                {
                    id: 1,
                    chatId: 1,
                    userId: 2,
                    messageText: 'Добрый день, Сергей Петрович! Только что отправила. Жду ответа.'
                },
                {
                    id: 2,
                    chatId: 1,
                    userId: 0,
                    messageText: 'Хорошо. По итогам вчерaшнего созвона нужно внести правки в смету. Посмотрите, пожалуйста, пункты 3.1 и 4.5.'
                },
                {
                    id: 3,
                    chatId: 1,
                    userId: 2,
                    messageText: 'Хорошо, я уже открыла файл. По пункту 4.5 у меня вопрос: мы учитываем доставку?'
                },
                {
                    id: 4,
                    chatId: 1,
                    userId: 0,
                    messageText: 'Да, учитываем. Добавьте отдельной строкой.'
                },
                {
                    id: 5,
                    chatId: 1,
                    userId: 2,
                    messageText: 'Поняла. Исправлю и вышлю итоговую версию до 17:00.'
                },
                {
                    id: 6,
                    chatId: 1,
                    userId: 0,
                    messageText: 'Отлично. Спасибо.'
                }
            ]
        }else if (chatId.value == 2){
            messages.value = [
                {
                    id: 0,
                    chatId: 2,
                    userId: 0,
                    messageText: 'Ты жив вообще?'
                },
                {
                    id: 1,
                    chatId: 2,
                    userId: 3,
                    messageText: 'Еле-еле. На работе аврал. Я уже три дня во сне вижу Excel-таблицы.'
                },
                {
                    id: 2,
                    chatId: 2,
                    userId: 0,
                    messageText: 'Кошмар! Спасать тебя в субботу? Приезжаю с пиццей и сериалами.'
                },
                {
                    id: 3,
                    chatId: 2,
                    userId: 3,
                    messageText: 'Ты ангел! Только без сериалов про врачей, а то усну.'
                },
                {
                    id: 4,
                    chatId: 2,
                    userId: 0,
                    messageText: 'Драконы и железный трон ок?'
                },
                {
                    id: 5,
                    chatId: 2,
                    userId: 3,
                    messageText: 'Идеально! Я за колой и чипсами.'
                },
                {
                    id: 6,
                    chatId: 2,
                    userId: 0,
                    messageText: 'Договорились! Только дождись меня, не усни за компом.'
                },
                {
                    id: 7,
                    chatId: 2,
                    userId: 3,
                    messageText: 'Обещаю ничего не обещать. Пока!'
                }
            ]
        }else{
            messages.value = []
	}
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
        await axios.post('http://localhost:8080/chat/' + chatId.value, {
            id: messages.value.length,
            userId: props.userId,
            chatId: chatId.value,
            messageText: textMessage.value,
            messageDate: new Date()
        })
        await axios.get('http://localhost:8080/chat/' + chatId.value)
            .then(response => messages.value = response.data)
    }catch(e){
        console.log(e)
    }
    textMessage.value = ''
}

async function showProf(user, type){
    if(type == 'chat'){
        try{
            await axios.get('http://localhost:8080/api/user/find/' + user.id)
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
                await axios.get('http://localhost:8080/api/user/find/' + props.userId)
                    .then(response => user.value = response.data)
            } catch (e) {
                console.log(e)
            }
        }else if(profType.value == "stranger"){
            try {
                await axios.get('http://localhost:8080/search/' + props.userId)
                    .then(response => chats.value = response.data)
                await axios.get('http://localhost:8080/api/user/find/' + (chats.value[chats.value.length-1].userId))
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
            await axios.get('http://localhost:8080/api/user/find/' + id)
                .then(response => user = response.data)
        } catch (e) {
            console.log(e)
        }
        showProf(user, 'stranger')
    }
}

function quit() {
    emit('quitEvent', 'auth')
}
</script>

<style scoped>
.top_menu {
  position: fixed;
  top: 0px;
  width: 100%;
  height: 82px;
}

.grid{
    display: flex;
    justify-content: flex-start;
    flex-direction: row;
}

.left_part{
    height: 720px;
    width: 40%;
}

.left_list{
    height: 455px;
    overflow: auto;
    scrollbar-width: thin;
    width: 100%;
}

.right_part{
    height: 720px;
    width: 60%;
}

.right_chat_title{
    width: 100%;
    height: 501px;
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