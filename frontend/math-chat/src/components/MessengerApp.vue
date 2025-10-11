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
                        <p>{{formatLdots(user.login, 40)}}</p>
                        <p>{{formatLdots(user.email, 40)}}</p>
                        <button @click="updateChat(user.id, user.chatId)">Открыть диалог</button>
                    </div>
                </div>
            </div>
            <div class="ten wide column">
                <div class="ui fluid segment" v-if="chatId.id != -1">
                    <h2 class="ui center header">{{chatUser.login}}</h2>
                    <div v-for="message in messages" :key="message.id">
                        <div class="ui left aligned segment" v-if="message.userId != userId">
                            <vue-latex v-if="message.isLatex" :expression="message.messageText" />
                            <div v-else>
                                <p v-for="line in formatArr(message.messageText, 45)" :key="line.id">
                                    {{ line.text }}
                                </p>
                            </div>
                        </div>
                        <div class="ui right aligned segment" v-else>
                            <vue-latex v-if="message.isLatex" :expression="message.messageText" />
                            <div v-else>
                                <p v-for="line in formatArr(message.messageText, 45)" :key="line.id">
                                    {{ line.text }}
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="ui fluid action input">
                        <input type="text" v-model="textMessage" placeholder="Сообщение">
                        <button class="ui button" @click="post">Отправить</button>
                        <div class="ui checkbox">
                            <input type="checkbox" v-model="isLatex">
                            <label>LaTeX</label>
                        </div>
                    </div>
                </div>
            </div>
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
const isLatex = ref(false)

const emit = defineEmits(['quitEvent'])

onMounted(async () => {
    try {
        await axios.get('http://localhost:8080/api/user/findAll')
            .then(response => allUsers.value = response.data)
    } catch (e) {
        console.log(e)
        allUsers.value = [
            {
                id: 0,
                firstname: 'Алиса',
                lastname: 'Артемьева',
                email: 'a@mail.ru'
            },
            {
                id: 1,
                firstname: 'Боб',
                lastname: 'Бутчер',
                email: 'b@mail.ru'
            },
            {
                id: 2,
                firstname: 'Витя',
                lastname: 'Величайший',
                email: 'v@mail.ru'
            },
            {
                id: 3,
                firstname: 'Длинный',
                lastname:  'Очень длииииииииииииииииииииииинный ник',
                email:  'dddddddddddddddddddddddddddddddddddddddd@mail.ru'
            },
            {
                id: 4,
                firstname: 'Глеб',
                lastname: 'Горячий',
                email: 'g@mail.ru'
            },
            {
                id: 5,
                firstname: 'Егор',
                lastname: 'Елесин',
                email: 'e@mail.ru'
            },
            {
                id: 6,
                firstname: 'Ёжик',
                lastname: 'Ёлочный',
                email: 'yo@mail.ru'
            },
            {
                id: 7,
                firstname: 'Жора',
                lastname: 'Жирный',
                email: 'j@mail.ru'
            },
            {
                id: 8,
                firstname: 'Зина',
                lastname: 'Зиновьева',
                email: 'z@mail.ru'
            },
            {
                id: 9,
                firstname: 'Игорь',
                lastname: 'Иванов',
                email: 'i@mail.ru'
            },
            {
                id: 10,
                firstname: 'Йорик',
                lastname: 'Йог',
                email: 'y@mail.ru'
            },
            {
                id: 11,
                firstname:'Кирилл',
                lastname: 'Капустин',
                email: 'k@mail.ru'
            },
            {
                id: 12,
                firstname: 'Лида',
                lastname: 'Лосева',
                email: 'l@mail.ru'
            },
            {
                id: 13,
                firstname: 'Матвей',
                lastname: 'Мальцев',
                email:  'm@mail.ru'
            },
            {
                id: 14,
                firstname: 'Никита',
                lastname: 'Носа',
                email: 'n@mail.ru'
            },
            {
                id: 15,
                firstname: 'Олег',
                lastname: 'Оботуров',
                email: 'o@mail.ru'
            }
        ]
    }
    for(let i = 0; i < allUsers.value.length; i++){
        allUsers1.value.push({
            id: allUsers.value[i].id,
            login: allUsers.value[i].firstname + " " + allUsers.value[i].lastname,
            email: allUsers.value[i].email
        })
    }
    user = allUsers1.value[props.userId]
    try {
        await axios.get('http://localhost:8080/api/messenger/' + props.userId)
            .then(response => chats.value = response.data)
    } catch (e) {
        console.log(e)
        chats.value = [
            {
                id: 0,
                userIdMin: 0,
                userIdMax: 1
            },
            {
                id: 1,
                userIdMin: 0,
                userIdMax: 2
            },
            {
                id: 2,
                userIdMin: 0,
                userIdMax: 3
            },
            {
                id: 3,
                userIdMin: 0,
                userIdMax: 4
            },
            {
                id: 4,
                userIdMin: 0,
                userIdMax: 5
            },
            {
                id: 5,
                userIdMin: 0,
                userIdMax: 6
            }
        ]
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
        if(chatId == 0){
            messages.value = [
                {
                    id: 0,
                    chatId: 0,
                    userId: 0,
                    messageText: 'Привет! Как планы на вечер?',
                    isLatex: false
                },
                {
                    id: 1,
                    chatId: 0,
                    userId: 1,
                    messageText: 'Привет! Пока свободен. А что?',
                    isLatex: false
                },
                {
                    id: 2,
                    chatId: 0,
                    userId: 0,
                    messageText: 'Да думаю сходить в тот новый бар на Пестеля. Соскучилась по хорошему бургеру)',
                    isLatex: false
                },
                {
                    id: 3,
                    chatId: 0,
                    userId: 1,
                    messageText: 'О, я как раз про него читал! Иду?',
                    isLatex: false
                },
                {
                    id: 4,
                    chatId: 0,
                    userId: 0,
                    messageText: 'Конечно! Встречаемся в семь у входа?',
                    isLatex: false
                },
                {
                    id: 5,
                    chatId: 0,
                    userId: 1,
                    messageText: 'Договорились. Только я с работы могу немного задержаться.',
                    isLatex: false
                },
                {
                    id: 6,
                    chatId: 0,
                    userId: 0,
                    messageText: 'Ничего страшного. Я как раз успею заскочить домой. Тогда в семь!',
                    isLatex: false
                },
                {
                    id: 7,
                    chatId: 0,
                    userId: 1,
                    messageText: 'Ага! Увидимся',
                    isLatex: false
                }
            ]
        }else if (chatId == 1){
            messages.value = [
                {
                    id: 0,
                    chatId: 1,
                    userId: 0,
                    messageText: 'Мария, добрый день. Выслали презентацию клиенту?',
                    isLatex: false
                },
                {
                    id: 1,
                    chatId: 1,
                    userId: 2,
                    messageText: 'Добрый день, Сергей Петрович! Только что отправила. Жду ответа.',
                    isLatex: false
                },
                {
                    id: 2,
                    chatId: 1,
                    userId: 0,
                    messageText: 'Хорошо. По итогам вчерaшнего созвона нужно внести правки в смету. Посмотрите, пожалуйста, пункты 3.1 и 4.5.',
                    isLatex: false
                },
                {
                    id: 3,
                    chatId: 1,
                    userId: 2,
                    messageText: 'Хорошо, я уже открыла файл. По пункту 4.5 у меня вопрос: мы учитываем доставку?',
                    isLatex: false
                },
                {
                    id: 4,
                    chatId: 1,
                    userId: 0,
                    messageText: 'Да, учитываем. Добавьте отдельной строкой.',
                    isLatex: false
                },
                {
                    id: 5,
                    chatId: 1,
                    userId: 2,
                    messageText: 'Поняла. Исправлю и вышлю итоговую версию до 17:00.',
                    isLatex: false
                },
                {
                    id: 6,
                    chatId: 1,
                    userId: 0,
                    messageText: 'Отлично. Спасибо.',
                    isLatex: false
                }
            ]
        }else if (chatId == 2){
            messages.value = [
                {
                    id: 0,
                    chatId: 2,
                    userId: 0,
                    messageText: 'Ты жив вообще?',
                    isLatex: false
                },
                {
                    id: 1,
                    chatId: 2,
                    userId: 3,
                    messageText: 'Еле-еле. На работе аврал. Я уже три дня во сне вижу Excel-таблицы.',
                    isLatex: false
                },
                {
                    id: 2,
                    chatId: 2,
                    userId: 0,
                    messageText: 'Кошмар! Спасать тебя в субботу? Приезжаю с пиццей и сериалами.',
                    isLatex: false
                },
                {
                    id: 3,
                    chatId: 2,
                    userId: 3,
                    messageText: 'Ты ангел! Только без сериалов про врачей, а то усну.',
                    isLatex: false
                },
                {
                    id: 4,
                    chatId: 2,
                    userId: 0,
                    messageText: 'Драконы и железный трон ок?',
                    isLatex: false
                },
                {
                    id: 5,
                    chatId: 2,
                    userId: 3,
                    messageText: 'Идеально! Я за колой и чипсами.',
                    isLatex: false
                },
                {
                    id: 6,
                    chatId: 2,
                    userId: 0,
                    messageText: 'Договорились! Только дождись меня, не усни за компом.',
                    isLatex: false
                },
                {
                    id: 7,
                    chatId: 2,
                    userId: 3,
                    messageText: 'Обещаю ничего не обещать. Пока!',
                    isLatex: false
                }
            ]
        }else{
            messages.value = []
	}
    }
}

function formatLdots(str, n){
    if(str.length > n){
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
    while(i < s.length){
        if(cur.length + s[i].length <= n){
            cur += ' ' + s[i]
        }else{
            ss.push(
                {
                    id: j,
                    text: cur.slice(0)
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
            text: cur.slice(0)
        }
    )
    return ss
}

function post(){
    messages.value.push({
        id: messages.value.length,
        userId: props.userId,
        chatId: chatId,
        messageText: textMessage.value,
        isLatex: isLatex.value
    })
    textMessage.value = ''
}

function quit() {
    emit('quitEvent', 'auth')
}
</script>