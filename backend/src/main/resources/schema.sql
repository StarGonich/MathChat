CREATE TABLE users (
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    firstname character varying(50) NOT NULL,
    lastname character varying(50) NOT NULL,
    email character varying(50) NOT NULL UNIQUE,
    password character varying(255) NOT NULL
--    image_id bigint REFERENCES images(id)
);

--CREATE TABLE images (
--    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
--    content_type character varying(255),
--    size bigint,
--    name character varying(255),
--    original_filename character varying(255),
--    bytes blob
--)

CREATE TABLE chats (
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id_min bigint NOT NULL REFERENCES users(id),
    user_id_max bigint NOT NULL REFERENCES users(id),
    last_message_id bigint -- Здесь нет NOT NULL,
    -- то есть сначала создаётся чат, а потом уже сообщение
);

CREATE TABLE messages (
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id bigint NOT NULL REFERENCES users(id),
    chat_id bigint NOT NULL REFERENCES chats(id),
    message_text text NOT NULL,
    date timestamp with time zone
);

-- Теперь добавляем внешний ключ в messages
ALTER TABLE chats ADD FOREIGN KEY (last_message_id) REFERENCES messages(id);