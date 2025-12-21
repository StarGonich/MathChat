ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE chats_id_seq RESTART WITH 1;
ALTER SEQUENCE messages_id_seq RESTART WITH 1;

INSERT INTO users(username, firstname, lastname, email, password, is_enabled)
VALUES ('StarGonich', 'Алексей', 'Величайший', '12321alexey8837@gmail.com', 'aboba', true);

INSERT INTO users(username, firstname, lastname, email, password, token, is_enabled)
VALUES ('NotActivated', 'Никита', 'Башков', 'notactivated@user.com', 'aboba', '12345678-1234-1234-1234-123456789012', false);

INSERT INTO users(username, firstname, lastname, email, password, is_enabled)
VALUES ('MatveyKov11', 'Матвей', 'Ковалёв', 'matveykov@gmail.com', 'aboba2', true);

INSERT INTO users(username, firstname, lastname, email, password, is_enabled)
VALUES ('Ansv33r', 'Егор', 'Мальцев', 'egor@gmail.com', 'aboba3', true);

-- Теперь можно вставлять чаты, зная, что пользователи созданы
INSERT INTO chats(first_user_id, second_user_id)
VALUES (
  (SELECT id FROM users WHERE email = '12321alexey8837@gmail.com'),
  (SELECT id FROM users WHERE email = 'matveykov@gmail.com')
);

INSERT INTO messages(user_id, chat_id, message_text, message_creation_date)
VALUES (
  (SELECT id FROM users WHERE email = '12321alexey8837@gmail.com'),
  (SELECT id FROM chats WHERE first_user_id = (SELECT id FROM users WHERE email = '12321alexey8837@gmail.com')),
  'Привет, Матвей, я Алексей',
  '2025-07-03 18:41:12.798644+03'
);