CREATE TABLE IF NOT EXISTS users (
    id bigint NOT NULL PRIMARY KEY,
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

CREATE TABLE IF NOT EXISTS chats (
    id bigint NOT NULL PRIMARY KEY,
    first_user_id bigint NOT NULL,
    second_user_id bigint NOT NULL,
    last_message_id bigint, -- Здесь нет NOT NULL,
    -- то есть сначала создаётся чат, а потом уже сообщение
    CONSTRAINT UC_FUserSUser UNIQUE (first_user_id, second_user_id)
);

CREATE TABLE IF NOT EXISTS messages (
    id bigint NOT NULL PRIMARY KEY,
    user_id bigint NOT NULL,
    chat_id bigint NOT NULL,
    message_text text NOT NULL,
    message_creation_date timestamp with time zone NOT NULL
);

-- Внешние ключи
--ALTER TABLE chats ADD FOREIGN KEY (first_user_id) REFERENCES users(id);
--ALTER TABLE chats ADD FOREIGN KEY (second_user_id) REFERENCES users(id);
--ALTER TABLE chats ADD FOREIGN KEY (last_message_id) REFERENCES messages(id);
--
--ALTER TABLE messages ADD FOREIGN KEY (user_id) REFERENCES users(id);
--ALTER TABLE messages ADD FOREIGN KEY (chat_id) REFERENCES chats(id);

CREATE SEQUENCE IF NOT EXISTS username_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS users_seq;
CREATE SEQUENCE IF NOT EXISTS chats_seq;
CREATE SEQUENCE IF NOT EXISTS messages_seq;

CREATE OR REPLACE FUNCTION set_user_id()
RETURNS TRIGGER AS
'
BEGIN
NEW.id := nextval(''users_seq'');
RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION set_chat_id()
RETURNS TRIGGER AS
'
BEGIN
NEW.id := nextval(''chats_seq'');
RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION set_message_id()
RETURNS TRIGGER AS
'
BEGIN
NEW.id := nextval(''messages_seq'');
RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER user_id
BEFORE INSERT ON users
FOR EACH ROW
EXECUTE FUNCTION set_user_id();

CREATE OR REPLACE TRIGGER chat_id
BEFORE INSERT ON chats
FOR EACH ROW
EXECUTE FUNCTION set_chat_id();

CREATE OR REPLACE TRIGGER message_id
BEFORE INSERT ON messages
FOR EACH ROW
EXECUTE FUNCTION set_message_id();