package com.vietdev.vocabularyapp.model; // Thay đổi package cho đúng

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate; // Sử dụng LocalDate là đủ cho ngày ôn tập
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_words")
public class UserWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    private LocalDateTime addedAt;

    // --- CÁC TRƯỜNG MỚI CHO SRS ---

    /** Ngày dự kiến ôn tập tiếp theo. */
    @Column(nullable = false)
    private LocalDate nextReviewDate;

    /** Khoảng thời gian giữa các lần ôn (tính bằng ngày). */
    private int reviewInterval = 0;

    /** Hệ số dễ dàng (Easiness Factor), bắt đầu từ 2.5. */
    private double easinessFactor = 2.5;

    /** Số lần ôn tập đúng liên tiếp. */
    private int repetitions = 0;

    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
        // Khi một từ mới được thêm, lịch ôn tập là ngay lập tức
        nextReviewDate = LocalDate.now();
    }
}