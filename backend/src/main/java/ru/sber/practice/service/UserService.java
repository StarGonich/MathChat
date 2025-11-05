package ru.sber.practice.service;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sber.practice.dto.*;
import ru.sber.practice.model.User;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

/**
 * Сервис для управления пользователями.
 * Обеспечивает основные операции для сущности User.
 */
public interface UserService {
    /**
     * Метод для регистрации нового пользователя.
     * Во время регистрации также идёт отправка письма на почту пользователя для его активации.
     * Пользователю присваивается уникальный token для этой активации.
     * Token выдаётся на время.
     *
     * @param signUpDTO DTO форма, которая нужна для регистрации. Информация о пользователе,
     *                  которого нужно зарегистрировать.
     * @return возвращаем пользователя или null, если пользователь уже зарегистрирован.
     */
    User register(SignUpDTO signUpDTO);
    /**
     * Метод для нахождения всех пользователей.
     *
     * @return список всех пользователей.
     */
    List<UserDTO> findAllUsers();
    /**
     * Метод для активации зарегистрированного пользователя.
     *
     * @param token токен, по которому находим пользователя, которого надо активировать. Его
     *              получаем после регистрации пользователя.
     * @return true если активация удалась, иначе false, если такого токена нет (срок истёк либо токен неправильный).
     */
    boolean activateUser(UUID token);
    /**
     * Метод для восстановления пароля пользователя.
     * Во время этого метода идёт отправка письма на почту пользователя.
     * Пользователю присваивается уникальный token для этой активации.
     * Token выдаётся на время
     *
     * @param emailDTO DTO форма, почта пользователя.
     * @return true если удалось отправить письмо, иначе false, если пользователя нету или он не активирован.
     */
    boolean passwordForgotten(EmailDTO emailDTO);
    /**
     * Метод для смены пароля пользователя.
     *
     * @param token токен, по которому находим пользователя, у которого надо сменить пароль. Его
     *              получаем после метода passwordForgotten.
     * @return true если удалось сменить пароль, иначе false, если пользователя нету или он не активирован.
     */
    boolean changePassword(UUID token, PasswordDTO passwordDTO);
    /**
     * Метод для нахождения пользователя по его id.
     *
     * @param userId id пользователя.
     * @return сущность user пользователя или null, если пользователя с таким id нет.
     */
    User findById(Long userId);
    /**
     * Метод для обновления информации о пользователе (ник, имя или фамилия).
     *
     * @param userId id пользователя.
     * @param user DTO форма, информация, которую нужно обновить у пользователя.
     * @return сущность user пользователя или null, если пользователя с таким id нет.
     */
    User updateUser(Long userId, UpdatableUserDTO user);
    /**
     * Метод для смены фотографии аватарки пользователя.
     *
     * @param userId id пользователя.
     * @param file фотография, которую нужно поставить.
     * @return название файла, сохранённого в S3 хранилище
     */
    String changeAvatar(Long userId, MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
