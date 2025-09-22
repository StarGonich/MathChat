import axios from 'axios'

const api = axios.create({
    url: 'https://localhost:8090',
    proxy: false  
})

/*const cors = require('cors');
const express = require('express');
const app = express();
app.use(cors({
  origin: 'https://localhost:8090', 
  credentials: true
}));*/

export default api