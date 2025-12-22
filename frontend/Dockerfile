# Используем официальный Node образ
FROM node:22

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем package.json и lock-файл
COPY package*.json ./

# Устанавливаем зависимости
RUN npm install

# Копируем всё остальное приложение внутрь контейнера
COPY . .

# Запускаем режим разработки
WORKDIR /app/math-chat
CMD ["npm", "run", "serve", "--", "--port", "9000"]