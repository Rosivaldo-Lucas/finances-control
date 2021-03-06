package com.rasmoo.client.financescontroll.v1.services;

import java.util.Optional;

import com.rasmoo.client.financescontroll.entity.User;
import com.rasmoo.client.financescontroll.repository.IUserRepository;
import com.rasmoo.client.financescontroll.v1.core.ResourceOwner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserDetailsService, IUserInfoService {

    private final IUserRepository userRepository;

    @Autowired
    public UserInfoService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final Optional<User> usuario = this.userRepository.findByEmail(email);

        if (usuario.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado.");
        }

        final UserDetails userDetails = new ResourceOwner(usuario.get());

        return userDetails;
    }

    public User findAuth() throws Exception {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        final Optional<User> usuario = this.userRepository.findByEmail(auth.getName());

        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        return usuario.get();
    }
    
}
