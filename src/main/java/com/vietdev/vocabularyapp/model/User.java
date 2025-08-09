package com.vietdev.vocabularyapp.model; // Thay đổi package cho đúng

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password; // Mật khẩu này sẽ được mã hóa

    @Column(nullable = false, unique = true)
    private String email;

    // Mối quan hệ một-nhiều với bảng UserWord
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserWord> userWords;
}