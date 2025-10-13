package ru.sber.practice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.sber.practice.config.MyUserDetails;
import ru.sber.practice.model.AuthProvider;
import ru.sber.practice.model.User;
import ru.sber.practice.repository.UserRepository;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        // Получаем атрибуты из GitHub
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String)attributes.get("email");
        String avatarUrl = (String)attributes.get("avatar_url");
        log.info("Данные о пользователе: {}, {}", email, avatarUrl);
        String providerId = oAuth2User.getName(); // ID пользователя в GitHub
        log.info("ID пользователя в GitHub: {}", providerId);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = new User();
        if (optionalUser.isEmpty()) {
            user.setEmail(email);
            user.setProvider(AuthProvider.GITHUB);
            user.setProviderId(providerId);
            user.setImageUrl(avatarUrl);
            user = userRepository.save(user);
        } else {
            user = optionalUser.get();
        }

        return new MyUserDetails(user);
    }
}
