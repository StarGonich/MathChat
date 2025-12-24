import { createRouter, createWebHistory } from 'vue-router'
import AuthApp from '@/components/AuthApp.vue'
import RegistrationApp from '@/components/RegistrationApp.vue'
import SendEmailApp from '@/components/SendEmailApp.vue'
import MessengerApp_v2 from '@/components/MessengerApp_v2.vue'
import ErrorPage from '@/components/ErrorPage.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: AuthApp
  },
  {
    path: '/reg',
    name: "Register",
    component: RegistrationApp
  },
  {
    path: '/email',
    name: "SendEmail",
    component: SendEmailApp
  },
  {
    path: '/mes:thisUserId',
    name: "Messenger",
    component: MessengerApp_v2,
    props: true
  },
  {
    path: '/error',
    name: "Error",
    component: ErrorPage
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL), 
  routes
})

export default router