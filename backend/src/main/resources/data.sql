INSERT INTO users VALUES (1, 'Алексей', 'Величайший', 'alexey8837@gmail.com', 'aboba');
INSERT INTO users VALUES (2, 'Матвей', 'Ковалёв', 'matveykov@gmail.com', 'aboba2');
INSERT INTO users VALUES (3, 'Егор', 'Мальцев', 'egor@gmail.com', 'aboba3');

INSERT INTO chats VALUES (0x0000000100000002);
INSERT INTO chats VALUES (0x0000000100000003);
INSERT INTO chats VALUES (0x0000000200000003);

INSERT INTO messages VALUES (1, 1, 0x0000000100000002, 'Привет, Матвей, я Алексей', '2025-07-03 18:41:12.798644+03');
INSERT INTO messages VALUES (2, 1, 0x0000000100000003, 'Привет, Егор, я Алексей', '2025-07-03 18:42:12.798644+03');
INSERT INTO messages VALUES (3, 2, 0x0000000200000003, 'Привет, Егор, я Матвей', '2025-07-03 18:43:12.798644+03');

