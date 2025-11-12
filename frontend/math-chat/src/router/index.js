import { createRouter, createWebHistory } from 'vue-router'
import AuthApp from '@/components/AuthApp.vue'
import RegistrationApp from '@/components/RegistrationApp.vue'
import SendEmailApp from '@/components/SendEmailApp.vue'
import MessengerApp from '@/components/MessengerApp.vue'

const routes = [
  {
    path: '/',             // URL адрес
    name: 'Home',          // Имя маршрута
    component: AuthApp        // Компонент, который отобразится
  },
  {
    path: '/reg',
    name: "Register",
    component: RegistrationApp,
    children: [
        {
            path: 'email',
            name: "SendEmail",
            component: SendEmailApp
        }
    ]
  },
  {
    path: '/mes:userId',
    name: "Messenger",
    component: MessengerApp,
    props: true
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL), 
  routes
})

export default router