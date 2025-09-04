CREATE TABLE users (
    id integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
    firstname character varying(50) NOT NULL,
    lastname character varying(50) NOT NULL,
    email character varying(50) NOT NULL UNIQUE,
    password character varying(255) NOT NULL
);

CREATE TABLE chats (
    id integer NOT NULL PRIMARY KEY
);

CREATE TABLE messages (
    id integer NOT NULL,
    user_id integer NOT NULL REFERENCES users(id),
    chat_id integer NOT NULL REFERENCES chats(id),
    message_text text NOT NULL,
    date timestamp with time zone
);