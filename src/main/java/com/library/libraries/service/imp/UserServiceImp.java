package com.library.libraries.service.imp;


import com.library.libraries.dto.RegistrationRequestDto;
import com.library.libraries.dto.UserDto;
import com.library.libraries.dto.UserPasswordDto;
import com.library.libraries.model.User;
import com.library.libraries.repository.UserRepository;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.lang.reflect.Type;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp {

    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImp(ModelMapper modelMapper, UserRepository userRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          AuthenticationManager authenticationManager) {
        super();
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<UserDto>>() {}.getType();

        return modelMapper.map(users, listType);
    }

    @Transactional
    public Boolean register(RegistrationRequestDto registrationRequest) {

        userRepository.findByEmail(registrationRequest.getEmail())
                .ifPresent(u -> {
                    throw new DataIntegrityViolationException("User exists with email: " + registrationRequest.getEmail());
                });

        userRepository.findByUsername(registrationRequest.getUsername())
                .ifPresent(u -> {
                    throw new DataIntegrityViolationException("User exists with username: " + registrationRequest.getUsername());
                });

        User user = createUserFromRegistrationRequest(registrationRequest);
        userRepository.save(user);
        return true;
    }

    private User createUserFromRegistrationRequest(RegistrationRequestDto registrationRequest) {
        User user = new User();

        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setFirstname(registrationRequest.getFirstname());
        user.setLastname(registrationRequest.getLastname());
        user.setPassword(bCryptPasswordEncoder.encode(registrationRequest.getPassword()));
        return user;
    }

    public UserDto findByUserName(String username) throws NotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User doesn't exist with this name called: " + username));

        return modelMapper.map(user, UserDto.class);
    }

    public Boolean update(String username, @Valid UserDto userDto) throws NotFoundException {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        User user = optionalUser.orElseThrow(() ->
                new NotFoundException("User doesn't exist with this name called: " + username));

        modelMapper.map(userDto, user);
        userRepository.save(user);
        return true;
    }

    public Boolean changePassword(UserPasswordDto userPasswordDto) throws NotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!userPasswordDto.getUsername().equals(auth.getName())) {
            return false;
        }

        Optional<User> optionalUser = userRepository.findByUsername(userPasswordDto.getUsername());
        User user = optionalUser.orElseThrow(() ->
                new NotFoundException("User doesn't exist with this name called: " + userPasswordDto.getUsername()));

        if (!bCryptPasswordEncoder.matches(userPasswordDto.getPassword(), user.getPassword())) {
            throw new NotFoundException("A senha atual está incorreta.");
        }

        user.setRealPassword(userPasswordDto.getNewpassword());
        user.setPassword(bCryptPasswordEncoder.encode(userPasswordDto.getNewpassword()));
        userRepository.save(user);

        return true;
    }

}
