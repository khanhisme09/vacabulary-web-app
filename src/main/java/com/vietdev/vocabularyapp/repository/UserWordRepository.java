package com.vietdev.vocabularyapp.repository;
import com.vietdev.vocabularyapp.model.UserWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWordRepository extends JpaRepository<UserWord, Long> {
    // Có thể thêm các phương thức truy vấn tùy chỉnh ở đây trong tương lai
}