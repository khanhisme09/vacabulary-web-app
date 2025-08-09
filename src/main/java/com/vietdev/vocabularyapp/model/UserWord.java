package com.vietdev.vocabularyapp.model; // Thay đổi package cho đúng

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_words")
public class UserWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // LAZY: chỉ load khi cần
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    private LocalDateTime addedAt; // Thời gian thêm từ

    @PrePersist // Hàm này sẽ tự chạy trước khi lưu vào DB
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
}