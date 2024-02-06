package com.globallogic.bcigloballogic.service;

import com.globallogic.bcigloballogic.model.dto.RegisterDto;
import com.globallogic.bcigloballogic.model.entity.UserEntity;
import com.globallogic.bcigloballogic.model.exception.DuplicatedUserException;
import com.globallogic.bcigloballogic.repository.UserRepository;
import com.globallogic.bcigloballogic.util.UserDataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email).orElseThrow( ()  -> new UsernameNotFoundException("User not found with email: " + email));

        return new User(userEntity.getEmail(), userEntity.getPassword(), Collections.emptyList());
    }
}
