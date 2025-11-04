## Minio S3

До запуска backend сервера нужно сначала запустить сервер S3

```docker
docker run --name mathchat-s3 -p 9001:9001 -p 9002:9002 minio/minio server /data --address :9001 --console-address :9002
```
После запуска автоматически создается том, которое и будет хранилищем файлов, так что эта команда выполняется один раз,
и в последующие разы запускается контейнер по команде
```docker
docker start mathchat-s3
```


## Auth

```
localhost:8080/register
```
— принимает `SignUpDTO`

## Мессенджер

1) Получение "контактных" чатов (сразу же после перехода на страницу мессенджера):
```
GET localhost:8080/search/{userId}
```
— принимает число `userId` в виде **PathVariable**, возвращает `List<ContactChatDTO>`. Поиск происходит локально в виде JavaScript скрипта, до 3 символов

2) Глобальный поиск чатов(по факту пользователей) по search:
```
GET localhost:8080/search/global/{search}
```
— принимает строку `search` в виде **PathVariable**, возвращает `List<GlobalChatDTO>`. Предполагается, что длиной $\geq 4$ символов. 

3) Создание чата, в случае его отсутствия
```
POST localhost:8080/chat/create/{myUserId}?with={anotherUserId}
```
— принимается `myUserId` (ваш) в виде **PathVariable**, и `anotherUserId` (предполагается, что из глобального поиска) в виде **RequestParam**

4) Получение сообщений по чату
```
GET localhost:8080/chat/{chatId}
```
— принимает число `chatId` в виде **PathVariable**, возвращает `List<GetMessagesDTO>`

5) Отправка сообщений:
```
localhost:8080//chat/{chatId}
```
— принимает число `chatId` в виде **PathVariable** и `SendMessageDTO` в виде **RequestBody**

## Профиль

1) Обновление данных о пользователе
```
PUT localhost:8080/update/{userId}
```
— принимает число `userId` в виде **PathVariable** и `UpdatableUserDTO` в виде **RequestBody**

2) Смена аватарки
```
PUT localhost:8080/change/avatar/{userId}
```
— принимает число `userId` в виде **PathVariable** и `file` в виде **RequestParam**

## Админ (пока не реализовываем)


*Мда, с путями запросов небольшая трабла, но это уже косметическая часть*

m: что изменилось: настройка бэка(настройка портов, cors), добавление костыльного логирования (мб заменю на нормальное) и добавление костылей, чтобы сервер у меня запускался 
