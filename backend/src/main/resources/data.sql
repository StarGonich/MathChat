INSERT INTO users(firstname, lastname, email, password) VALUES ('Алексей', 'Величайший', '12321alexey8837@gmail.com', 'aboba');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Матвей', 'Ковалёв', 'matveykov@gmail.com', 'aboba2');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Егор', 'Мальцев', 'egor@gmail.com', 'aboba3');

INSERT INTO chats(id, user_id_min, user_id_max) VALUES (1, 1, 2);
INSERT INTO chats(id, user_id_min, user_id_max) VALUES (2, 1, 3);
INSERT INTO chats(id, user_id_min, user_id_max) VALUES (3, 2, 3);

INSERT INTO messages VALUES (1, 1, 1, 'Привет, Матвей, я Алексей', '2025-07-03 18:41:12.798644+03');
INSERT INTO messages VALUES (2, 1, 2, 'Привет, Егор, я Алексей', '2025-07-03 18:42:12.798644+03');
INSERT INTO messages VALUES (3, 2, 3, 'Привет, Егор, я Матвей', '2025-07-03 18:43:12.798644+03');

INSERT INTO users(firstname, lastname, email, password) VALUES ('Алиса', 'Артемьева', 'a@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Боб', 'Бутчер', 'b@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Витя', 'Величайший', 'v@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Длинный', 'Очень длииииииииииииииииииииииинный ник', 'dddddddddddddddddddddddddddddddddddddddd@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Глеб', 'Горячий', 'g@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Егор', 'Елесин', 'e@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Ёжик', 'Ёлочный', 'yo@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Жора', 'Жирный', 'j@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Зина', 'Зиновьева', 'z@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Игорь', 'Иванов', 'i@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Йорик', 'Йог', 'y@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Кирилл', 'Капустин', 'k@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Лида', 'Лосева', 'l@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Матвей', 'Мальцев', 'm@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Никита', 'Носа', 'n@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Олег', 'Оботуров', 'o@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Петя', 'Первый', 'p@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Роман', 'Рогозин', 'r@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Семён', 'Смирнов', 's@mail.ru', '11111');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Толик', 'Третий', 't@mail.ru', '11111');

INSERT INTO chats(id, user_id_min, user_id_max) VALUES (4, 4, 5);
INSERT INTO chats(id, user_id_min, user_id_max) VALUES (5, 4, 6);
INSERT INTO chats(id, user_id_min, user_id_max) VALUES (6, 4, 7);
INSERT INTO chats(id, user_id_min, user_id_max) VALUES (7, 4, 8);
INSERT INTO chats(id, user_id_min, user_id_max) VALUES (8, 4, 9);
INSERT INTO chats(id, user_id_min, user_id_max) VALUES (9, 4, 10);
INSERT INTO chats(id, user_id_min, user_id_max) VALUES (10, 4, 11);
INSERT INTO chats(id, user_id_min, user_id_max) VALUES (11, 4, 12);

INSERT INTO messages VALUES (4, 4, 4, 'Привет! Как планы на вечер?', '2025-07-03 14:41:12.798644+03');
INSERT INTO messages VALUES (5, 5, 4, 'Привет! Пока свободен. А что?', '2025-07-03 14:43:12.798644+03');
INSERT INTO messages VALUES (6, 4, 4, 'Да думаю сходить в тот новый бар на Пестеля. Соскучилась по хорошему бургеру)', '2025-07-03 14:44:12.798644+03');
INSERT INTO messages VALUES (7, 5, 4, 'О, я как раз про него читал! Иду?', '2025-07-03 14:45:12.798644+03');
INSERT INTO messages VALUES (8, 4, 4, 'Конечно! Встречаемся в семь у входа?', '2025-07-03 14:46:12.798644+03');
INSERT INTO messages VALUES (9, 5, 4, 'Договорились. Только я с работы могу немного задержаться.', '2025-07-03 14:47:12.798644+03');
INSERT INTO messages VALUES (10, 4, 4, 'Ничего страшного. Я как раз успею заскочить домой. Тогда в семь!', '2025-07-03 14:48:12.798644+03');
INSERT INTO messages VALUES (11, 5, 4, 'Ага! Увидимся', '2025-07-03 14:49:12.798644+03');

INSERT INTO messages VALUES (12, 4, 5, 'Мария, добрый день. Выслали презентацию клиенту?', '2025-07-03 14:41:12.798644+03');
INSERT INTO messages VALUES (13, 6, 5, 'Добрый день, Сергей Петрович! Только что отправила. Жду ответа.', '2025-07-03 14:41:22.798644+03');
INSERT INTO messages VALUES (14, 4, 5, 'Хорошо. По итогам вчерaшнего созвона нужно внести правки в смету. Посмотрите, пожалуйста, пункты 3.1 и 4.5.', '2025-07-03 14:41:32.798644+03');
INSERT INTO messages VALUES (15, 6, 5, 'Хорошо, я уже открыла файл. По пункту 4.5 у меня вопрос: мы учитываем доставку?', '2025-07-03 14:41:42.798644+03');
INSERT INTO messages VALUES (16, 4, 5, 'Да, учитываем. Добавьте отдельной строкой.', '2025-07-03 14:41:52.798644+03');
INSERT INTO messages VALUES (17, 6, 5, 'Поняла. Исправлю и вышлю итоговую версию до 17:00.', '2025-07-03 14:42:02.798644+03');
INSERT INTO messages VALUES (18, 4, 5, 'Отлично. Спасибо.', '2025-07-03 14:42:12.798644+03');

INSERT INTO messages VALUES (19, 4, 6, 'Ты жив вообще?', '2025-07-03 14:41:12.798644+03');
INSERT INTO messages VALUES (20, 7, 6, 'Еле-еле. На работе аврал. Я уже три дня во сне вижу Excel-таблицы.', '2025-07-03 14:41:22.798644+03');
INSERT INTO messages VALUES (21, 4, 6, 'Кошмар! Спасать тебя в субботу? Приезжаю с пиццей и сериалами.', '2025-07-03 14:41:32.798644+03');
INSERT INTO messages VALUES (22, 7, 6, 'Ты ангел! Только без сериалов про врачей, а то усну.', '2025-07-03 14:41:42.798644+03');
INSERT INTO messages VALUES (23, 4, 6, 'Драконы и железный трон ок?', '2025-07-03 14:41:52.798644+03');
INSERT INTO messages VALUES (24, 7, 6, 'Идеально! Я за колой и чипсами.', '2025-07-03 14:42:02.798644+03');
INSERT INTO messages VALUES (25, 4, 6, 'Договорились! Только дождись меня, не усни за компом.', '2025-07-03 14:42:12.798644+03');
INSERT INTO messages VALUES (26, 7, 6, 'Обещаю ничего не обещать. Пока!', '2025-07-03 14:42:22.798644+03');