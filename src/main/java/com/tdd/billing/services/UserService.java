package com.tdd.billing.services;

import com.tdd.billing.dto.UpdateUserDTO;
import com.tdd.billing.dto.UserResponseDTO;
import com.tdd.billing.entities.User;
import com.tdd.billing.entities.UserRole;
import com.tdd.billing.repositories.UserRepository;
import com.tdd.billing.mappers.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> listarUsuariosActivos() {
        return userRepository.findByStatusTrue();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }


    public User updateUser(Long id, UpdateUserDTO dto, String photoUrl) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualiza campos básicos
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setStatus(dto.isStatus());

        // Si se cambia el password
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // Rol
        if (dto.getRole() != null) {
            user.setRole(UserRole.valueOf(dto.getRole()));
        }

        // Foto (si fue subida)
        if (photoUrl != null && !photoUrl.isBlank()) {
            user.setPhotoUrl(photoUrl);
        }

        return userRepository.save(user);
    }



    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setStatus(false);
    }

    public User authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
    }



    public List<UserResponseDTO> getUsersByStoreId(Long storeId) {
        return userRepository.findByStoreId(storeId).stream()
                .map(UserMapper::toDTO)
                .toList();
    }

}
