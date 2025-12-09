package ru.sber.practice.service;

/**
 * Сервис для отправки сообщений по почте.
 */
public interface MailSenderService {
    /**
     * Метод для отправки письма на почту.
     *
     * @param emailTo адрес почты, куда надо отправить письмо.
     * @param subject тема письма.
     * @param message сообщение письма.
     */
    void send(String emailTo, String subject, String message);
}