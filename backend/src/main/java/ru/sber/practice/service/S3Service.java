package ru.sber.practice.service;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Сервис для работы с объектным хранилищем S3 (MinIO).
 * Обеспечивает основные операции для загрузки и скачивания файлов.
 */
public interface S3Service {
    /**
     * Метод для загрузки файла в S3 хранилище.
     * Файлу присваивается уникальный ключ, состоящий из UUID и оригинального имени файла.
     *
     * @param file файл, который нужно загрузить в хранилище
     * @return уникальный ключ файла в хранилище или null, если загрузка не удалась
     * @throws IOException если произошла ошибка ввода-вывода при чтении файла
     * @throws ServerException если сервер S3 вернул ошибку
     * @throws InsufficientDataException если получено недостаточно данных от сервера
     * @throws ErrorResponseException если сервер вернул ошибку в ответе
     * @throws NoSuchAlgorithmException если алгоритм хеширования не найден
     * @throws InvalidKeyException если неверный ключ доступа
     * @throws InvalidResponseException если получен неверный ответ от сервера
     * @throws XmlParserException если ошибка парсинга XML
     * @throws InternalException если внутренняя ошибка клиента S3
     */
    String uploadFile(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    /**
     * Метод для скачивания файла из S3 хранилища.
     * Возвращает содержимое файла в виде массива байтов.
     *
     * @param key уникальный ключ файла в хранилище
     * @return содержимое файла в виде массива байтов или null, если файл не найден
     * @throws ServerException если сервер S3 вернул ошибку
     * @throws InsufficientDataException если получено недостаточно данных от сервера
     * @throws ErrorResponseException если сервер вернул ошибку в ответе
     * @throws IOException если произошла ошибка ввода-вывода при чтении файла
     * @throws NoSuchAlgorithmException если алгоритм хеширования не найден
     * @throws InvalidKeyException если неверный ключ доступа
     * @throws InvalidResponseException если получен неверный ответ от сервера
     * @throws XmlParserException если ошибка парсинга XML
     * @throws InternalException если внутренняя ошибка клиента S3
     */
    byte[] downloadFile(String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}