CREATE TABLE users (
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username varchar UNIQUE,
    firstname character varying(50),
    lastname character varying(50),
    email character varying(50) UNIQUE,
    password character varying(255),
    token uuid,
    is_enabled boolean NOT NULL DEFAULT false,
    token_creation_date timestamp with time zone,
    provider character varying(36) DEFAULT 'LOCAL',  -- LOCAL, GITHUB, GOOGLE
    provider_id varchar,
    image_url character varying(255)
);

CREATE TABLE chats (
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_user_id bigint NOT NULL,
    second_user_id bigint NOT NULL,
    last_message_id bigint, -- Здесь нет NOT NULL,
    -- то есть сначала создаётся чат, а потом уже сообщение
    CONSTRAINT UC_FUserSUser UNIQUE (first_user_id, second_user_id)
);

CREATE TABLE messages (
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id bigint NOT NULL,
    chat_id bigint NOT NULL,
    message_text text NOT NULL,
    message_creation_date timestamp with time zone NOT NULL
);

-- Внешние ключи
ALTER TABLE chats ADD FOREIGN KEY (first_user_id) REFERENCES users(id);
ALTER TABLE chats ADD FOREIGN KEY (second_user_id) REFERENCES users(id);
ALTER TABLE chats ADD FOREIGN KEY (last_message_id) REFERENCES messages(id);

ALTER TABLE messages ADD FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE messages ADD FOREIGN KEY (chat_id) REFERENCES chats(id);

CREATE SEQUENCE IF NOT EXISTS username_seq START WITH 1000 INCREMENT BY 1