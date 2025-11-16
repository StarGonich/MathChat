<template>
    <div class="ui text container">
         <h2 class="ui image header">
            <img src="./static/images/icon.webp" class="image">
            <div class="content">
                Подтверждение почты
            </div>
        </h2>
        <form class="ui raised center aligned container segment">
            <div class="ui message">
                На вашу почту пришло письмо! Для подтверждения откройте ссылку в письме.
            </div>
            <h2 class="ui center header">{{ formatTime(time) }}</h2>
            <div class="ui clearing divider"></div>
            <button class = "ui button primary" @click="post" :disabled="time > 0">Отправить письмо еще раз</button>
            <button class = "ui button" @click="quit">Вернуться ко входу</button>
        </form>
    </div>
</template>

<script setup>
import router from '@/router'
import { ref, onMounted } from 'vue'

let time = ref(150)

function formatTime(t){
    let s = ''
    s += Math.floor(t/60) + ':'
    if (t % 60 < 10){
        s += '0'
    }
    s += t%60
    return s 
}

onMounted(() => {
    setTimeout(tick, 1000)
})

function tick() {
    if (time.value > 0){
        time.value--
        setTimeout(tick, 1000)
    }
}

function post(){
    time.value = 150
    tick()
}

function quit(){
    router.push({name: 'Home'})
}

document.body.style.overflow = 'hidden'
</script>