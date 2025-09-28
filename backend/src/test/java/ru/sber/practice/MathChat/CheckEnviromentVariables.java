package ru.sber.practice.MathChat;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CheckEnviromentVariables {

    @Test
    void testGitHubOAuthEnvVariables() throws IOException {
        // Загружаем переменные напрямую из .env файла
        Properties envProperties = new Properties();
        envProperties.load(new FileInputStream(".env"));

        String clientId = envProperties.getProperty("GITHUB_CLIENT_ID");
        String clientSecret = envProperties.getProperty("GITHUB_CLIENT_SECRET");

        // Эти проверки УПАДУТ если переменных нет в .env файле
        assertNotNull(clientId, "GITHUB_CLIENT_ID должен быть установлен в .env файле");
        assertNotNull(clientSecret, "GITHUB_CLIENT_SECRET должен быть установлен в .env файле");
        assertFalse(clientId.trim().isEmpty(), "GITHUB_CLIENT_ID не должен быть пустым");
        assertFalse(clientSecret.trim().isEmpty(), "GITHUB_CLIENT_SECRET не должен быть пустым");
    }
}