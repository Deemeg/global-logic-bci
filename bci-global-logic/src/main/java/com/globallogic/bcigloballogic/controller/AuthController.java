package com.globallogic.bcigloballogic.controller;

import com.globallogic.bcigloballogic.model.dto.LoginDto;
import com.globallogic.bcigloballogic.model.dto.PhoneDto;
import com.globallogic.bcigloballogic.model.dto.RegisterDto;
import com.globallogic.bcigloballogic.model.entity.PhoneEntity;
import com.globallogic.bcigloballogic.model.entity.UserEntity;
import com.globallogic.bcigloballogic.model.exception.DuplicatedUserException;
import com.globallogic.bcigloballogic.model.response.LoginResponse;
import com.globallogic.bcigloballogic.model.response.SignUpResponse;
import com.globallogic.bcigloballogic.repository.PhoneRepository;
import com.globallogic.bcigloballogic.repository.UserRepository;
import com.globallogic.bcigloballogic.util.DateFormatter;
import com.globallogic.bcigloballogic.util.JwtUtils;
import com.globallogic.bcigloballogic.util.UserDataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository userRepository;

    private final PhoneRepository phoneRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtil;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public AuthController(UserRepository userRepository, PhoneRepository phoneRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("sign-up")
    public ResponseEntity<SignUpResponse> register(@RequestBody RegisterDto registerDto) {
        validateRegisterDto(registerDto);
        List<PhoneEntity> phoneEntities = Collections.emptyList();

        UserEntity userEntity = UserEntity.builder()
                .email(registerDto.getEmail())
                .name(registerDto.getName())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .isActive(false)
                .created(Timestamp.from(Instant.now()))
                .lastLogin(Timestamp.from(Instant.now()))
                .build();
        UserEntity newUser = userRepository.save(userEntity);

        if(registerDto.getPhones() != null){
            phoneEntities = registerDto.getPhones().stream().map(phoneDto ->
                    PhoneEntity.builder()
                            .countrycode(phoneDto.countrycode)
                            .citycode(phoneDto.citycode)
                            .number(phoneDto.number)
                            .user(newUser)
                            .build()
            ).collect(Collectors.toList());

            phoneRepository.saveAll(phoneEntities);
        }

        String token = jwtUtil.generateToken(newUser.getEmail());

        SignUpResponse signUpResponse = SignUpResponse.builder()
                .id(newUser.getId())
                .lastLogin("")
                .isActive(newUser.getIsActive())
                .created(DateFormatter.formatTimeStamp(newUser.getCreated()))
                .token(token)
                .build();

        return new ResponseEntity<>(signUpResponse, HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto) {
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(login);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Optional<UserEntity> userEntity = userRepository.findUserEntityByEmail(loginDto.getEmail());
        UserEntity user = userEntity.get();
        user.setLastLogin(Timestamp.from(Instant.now()));
        user.setIsActive(true);

        userRepository.save(user);
        String token = jwtUtil.generateToken(loginDto.getEmail());

        LoginResponse loginResponse = LoginResponse.builder()
                .lastLogin(DateFormatter.formatTimeStamp(user.getLastLogin()))
                .name(user.getName())
                .token(token)
                .created(DateFormatter.formatTimeStamp(user.getCreated()))
                .phones(user.getPhones().stream().map(phoneEntity ->
                        PhoneDto.fromEntity(phoneEntity)
                ).collect(Collectors.toList()))
                .isActive(user.getIsActive())
                .email(user.getEmail())
                .password(user.getPassword())
                .id(user.getId())
                .build();

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    private void validateRegisterDto(RegisterDto registerDto) {
        UserDataValidator.emailPattern(registerDto.getEmail());
        UserDataValidator.passwordPattern(registerDto.getPassword());
        if (userRepository.findUserEntityByEmail(registerDto.getEmail()).isPresent()) {
            throw new DuplicatedUserException("the email is already in use");
        }
    }
}
