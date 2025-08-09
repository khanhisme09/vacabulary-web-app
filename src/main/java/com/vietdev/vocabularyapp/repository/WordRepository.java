package com.vietdev.vocabularyapp.repository; // Thay đổi package cho đúng

import com.vietdev.vocabularyapp.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    // Spring Data JPA sẽ tự động hiểu phương thức này
    // và tạo câu lệnh SELECT * FROM words WHERE word = ?
    Optional<Word> findByWord(String word);
}