#### По итогу, что есть:
 
Регистрация, принимает Body из всех полей user
```
localhost:8080/register
```

## REST API

Просто список всех юзеров, эксперемент
```
localhost:8080/api/user/findAll
```

Нахождение сообщений по chatId
```
localhost:8080/api/messenger/chat/{chatId}
```

Нахождение чатов по userId:
```
localhost:8080/api/messenger/{userId}
```

*Мда, с путями запросов небольшая трабла, но это уже косметическая часть*