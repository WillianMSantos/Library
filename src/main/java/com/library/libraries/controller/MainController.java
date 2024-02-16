package com.library.libraries.controller;

import com.library.libraries.dto.LoginDto;
import com.library.libraries.dto.RegistrationRequestDto;
import com.library.libraries.dto.TokenResponseDto;
import com.library.libraries.model.User;
import com.library.libraries.security.JwtTokenUtil;
import com.library.libraries.service.imp.UserServiceImp;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.library.libraries.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;


@RestController
@RequestMapping(ApiPaths.MainCtrl.CTRL)
@CrossOrigin
@Api(value = "MainController", description = "Authentication operations")
public class MainController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserServiceImp userServiceImp;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public MainController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                          AuthenticationManager authenticationManager, ModelMapper modelMapper,
                          UserServiceImp userServiceImp) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.userServiceImp = userServiceImp;
    }

    @ApiOperation(value = "User login", notes = "User authentication and token generation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully authenticated"),
            @ApiResponse(code = 401, message = "Authentication failed")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponseDto> login(@ApiParam(value = "LoginRequest object", required = true) @RequestBody LoginDto request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            final User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + request.getUsername()));
            final String token = jwtTokenUtil.generateToken(user);
            return ResponseEntity.ok(new TokenResponseDto(user.getUsername(), token));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed", e);
        }
    }

    @ApiOperation(value = "User registration", notes = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User successfully registered"),
            @ApiResponse(code = 400, message = "Invalid registration request")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<Boolean> signUp(@ApiParam(value = "RegistrationRequest object", required = true)
                                          @Valid @RequestBody RegistrationRequestDto registrationRequest) {
        Boolean result = userServiceImp.register(registrationRequest);
        return ResponseEntity.ok(result);
    }
}