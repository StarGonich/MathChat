<template>
    <div class="modal">
        <div class="ui attached stackable menu">
            <div class="ui container">
                <a class="item">
                    <p>Список пользователей</p>
                </a>
                <a class="right item"  @click="quit">
                    <i class="close icon"></i>
                </a>
            </div>
        </div>
        <div v-for="user in props.users" :key="user.id">
            <div class="ui message" @click="select(user.id)">
                <p>{{ user.username }} {{ user.firstname }} {{ user.lastname }}</p>
            </div>
        </div>
    </div>
</template>

<script setup>
const props = defineProps({
    users: {
        required: true
    }
})

const emit = defineEmits(['quitEvent', 'selectEvent'])

function quit(){
    emit('quitEvent', -1)
}

function select(id){
    emit('selectEvent', id)
}
</script>

<style scoped>
.modal {
  position: fixed;
  z-index: 999;
  top: 20%;
  height: 60%;
  left: 35%;
  width: 30%;
  background-color: #ffffff;
  border: 3px solid #000000;
  overflow: scroll;
}
</style>