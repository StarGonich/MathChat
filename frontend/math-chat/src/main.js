import { createApp } from 'vue'
import SuiVue from 'semantic-ui-vue'
import VueLatex from 'vatex'
import VueKonva from 'vue-konva';
import App from './App.vue'
import router from './router'
import 'semantic-ui-css/semantic.min.css';

window.$ = window.jQuery = require('jquery')
require('semantic-ui-css/semantic.css')
require('semantic-ui-css/semantic.js')

const app = createApp(App)
app.use(router)
app.use(SuiVue)
app.use(VueLatex)
app.use(VueKonva)
app.mount('#app')