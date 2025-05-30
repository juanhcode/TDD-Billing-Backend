package com.tdd.billing.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stores")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Store {

    public Store(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String url;

    @Column(length = 255)
    private String email;

    @Column(length = 100)
    private String contact;

    @Column(length = 50)
    private String nit;

    @Column(columnDefinition = "text")
    private String logo;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private Boolean status = true;

    @Column(nullable = false)
    private String address;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", email='" + email + '\'' +
                ", contact='" + contact + '\'' +
                ", nit='" + nit + '\'' +
                ", logo='" + logo + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", address='" + address + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
