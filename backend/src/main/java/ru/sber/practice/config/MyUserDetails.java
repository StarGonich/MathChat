package ru.sber.practice.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.sber.practice.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MyUserDetails implements UserDetails, OAuth2User {
    private User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail() == null ? user.getProviderId() : user.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
  
    @Override
    public String getName() {
        return user.getUsername();
    }
}
