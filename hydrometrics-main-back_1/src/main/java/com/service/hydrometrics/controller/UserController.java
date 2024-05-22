package com.service.hydrometrics.controller;

import com.service.hydrometrics.utils.UtilsMethods;
import com.service.hydrometrics.models.DB.entity.User;
import com.service.hydrometrics.models.DTO.user.CreateUserDTO;
import com.service.hydrometrics.models.DTO.user.UserDTO;
import com.service.hydrometrics.services.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Tag(name = "User resources")
public class UserController {

    private final IUserService service;

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody @Valid CreateUserDTO createUserDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(UtilsMethods.validatorBody(bindingResult));
        }
        User user = new User(createUserDTO);
        if (service.userExist(user)) {
            return ResponseEntity.status(409).body("User already exists");
        }
        return ResponseEntity.status(202).body(service.saveUser(user));
    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(UtilsMethods.validatorBody(bindingResult));
        }
        User userUpdate = new User(userDTO);
        if (!service.userExist(userUpdate)) {
            return ResponseEntity.status(409).body("User does not exist");
        }
        return ResponseEntity.ok(service.updateUser(userUpdate));
    }

    @Transactional(readOnly = true)
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @Transactional(readOnly = true)
    @GetMapping("/get")
    public ResponseEntity<?> getUser() {
        User user = service.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
        UserDTO userDTO = new UserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @Transactional(readOnly = true)
    @GetMapping("report")
    public ResponseEntity<?> getUserReport() {
        return ResponseEntity.ok(service.getUserReports());
    }
}

