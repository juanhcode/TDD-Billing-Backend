package com.tdd.billing.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private UserRole role;

    @Column(nullable = false, name = "photo_url")
    private String photoUrl;

    @Column(nullable = false)
    private boolean status = true;

    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", photoUrl='" + photoUrl + '\'' +
                ", status=" + status +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
