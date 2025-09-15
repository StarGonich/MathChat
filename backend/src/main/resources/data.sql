INSERT INTO users(firstname, lastname, email, password) VALUES ('Алексей', 'Величайший', 'alexey8837@gmail.com', 'aboba');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Матвей', 'Ковалёв', 'matveykov@gmail.com', 'aboba2');
INSERT INTO users(firstname, lastname, email, password) VALUES ('Егор', 'Мальцев', 'egor@gmail.com', 'aboba3');

INSERT INTO chats(id, user_id_min, user_id_max) VALUES (1, 1, 2);
INSERT INTO chats(id, user_id_min, user_id_max) VALUES (2, 1, 3);
INSERT INTO chats(id, user_id_min, user_id_max) VALUES (3, 2, 3);

INSERT INTO messages VALUES (1, 1, 1, 'Привет, Матвей, я Алексей', '2025-07-03 18:41:12.798644+03');
INSERT INTO messages VALUES (2, 1, 2, 'Привет, Егор, я Алексей', '2025-07-03 18:42:12.798644+03');
INSERT INTO messages VALUES (3, 2, 3, 'Привет, Егор, я Матвей', '2025-07-03 18:43:12.798644+03');

