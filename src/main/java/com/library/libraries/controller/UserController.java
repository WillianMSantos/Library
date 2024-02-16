package com.library.libraries.controller;

import com.library.libraries.dto.UserDto;
import com.library.libraries.dto.UserPasswordDto;
import com.library.libraries.service.imp.UserServiceImp;
import io.swagger.annotations.*;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;


@RestController
@RequestMapping(ApiPaths.UserCtrl.CTRL)
@CrossOrigin
@Api(value = "UserController", description = "User management operations")
public class UserController {

    @Autowired
    private UserServiceImp userServiceImp;

    @ApiOperation(value = "Find user by username", notes = "Retrieve user details by their username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the user"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @GetMapping("/{username}")
    public ResponseEntity<UserDto> findByUserName(@ApiParam(value = "Username to retrieve the user", required = true)
                                                  @PathVariable String username) {
        try {
            UserDto userDto = userServiceImp.findByUserName(username);
            return ResponseEntity.ok(userDto);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    @ApiOperation(value = "Update user", notes = "Update user details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully updated"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @PutMapping("/{username}")
    public ResponseEntity<Boolean> updateUser(@ApiParam(value = "Username of the user to be updated", required = true)
                                              @PathVariable String username,
                                              @ApiParam(value = "Updated user information", required = true)
                                              @Valid @RequestBody UserDto userDto) {
        try {
            Boolean result = userServiceImp.update(username, userDto);
            return ResponseEntity.ok(result);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    @ApiOperation(value = "Change user password", notes = "Allows a user to change their password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Password successfully changed"),
            @ApiResponse(code = 400, message = "Invalid password change request")
    })
    @PatchMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(@ApiParam(value = "Password change request object", required = true)
                                                  @Valid @RequestBody UserPasswordDto userPasswordDto) {
        try {
            Boolean result = userServiceImp.changePassword(userPasswordDto);
            return ResponseEntity.ok(result);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error changing password", e);
        }
    }
}