package com.tdd.billing.controllers;

import com.tdd.billing.dto.UpdateUserDTO;
import com.tdd.billing.dto.UserResponseDTO;
import com.tdd.billing.entities.User;
import com.tdd.billing.mappers.UserMapper;
import com.tdd.billing.services.S3Service;
import com.tdd.billing.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final S3Service s3Service;

    public UserController(UserService userService, S3Service s3Service) {
        this.userService = userService;
        this.s3Service = s3Service;
    }

    @PostMapping
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> listActiveUsers() {
        return ResponseEntity.ok(userService.listarUsuariosActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Upload a file to S3
    @PostMapping("/upload/{id}")
    public String uploadFile(@PathVariable Long id,@RequestPart("user")UpdateUserDTO userDetails,@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("Hola: " + userDetails);
        return s3Service.uploadFile(file);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> uploadFileAndUpdateUser(
            @PathVariable Long id,
            @RequestPart("user") UpdateUserDTO userDetails,
            @RequestParam("file") MultipartFile file) throws IOException {

        String photoUrl = s3Service.uploadFile(file);
        User updatedUser = userService.updateUser(id, userDetails, photoUrl);
        return ResponseEntity.ok(UserMapper.toDTO(updatedUser));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByStoreId(@PathVariable Long storeId) {
        List<UserResponseDTO> users = userService.getUsersByStoreId(storeId);
        return ResponseEntity.ok(users);
    }

}
