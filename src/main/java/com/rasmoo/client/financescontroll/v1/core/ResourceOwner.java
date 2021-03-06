package com.rasmoo.client.financescontroll.v1.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.rasmoo.client.financescontroll.entity.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ResourceOwner implements UserDetails {

    private final User user;

    public ResourceOwner(final User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<SimpleGrantedAuthority> roles = new ArrayList<>();

        roles.add(new SimpleGrantedAuthority(this.user.getRole()));

        return roles;
    }

    @Override
    public String getPassword() {
        return this.user.getCredencial().getSenha();
    }

    @Override
    public String getUsername() {
        return this.user.getCredencial().getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
