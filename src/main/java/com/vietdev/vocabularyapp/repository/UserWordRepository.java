package com.vietdev.vocabularyapp.repository;
import com.vietdev.vocabularyapp.dto.DailyActivityDTO;
import com.vietdev.vocabularyapp.model.User;
import com.vietdev.vocabularyapp.model.UserWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface UserWordRepository extends JpaRepository<UserWord, Long> {

    List<UserWord> findByUserAndNextReviewDateLessThanEqual(User user, LocalDate date);
    Optional<UserWord> findByUserAndWordId(User user, Long wordId);
    @Query(value = "SELECT CAST(added_at AS DATE) as date, COUNT(*) as count " +
            "FROM user_words " +
            "WHERE user_id = :userId " +
            "GROUP BY CAST(added_at AS DATE) " +
            "ORDER BY date ASC", nativeQuery = true)
    List<Object[]> findDailyWordAdditionsNative(Long userId);

    // Chuyển đổi kết quả từ Object[] sang DTO
    default List<DailyActivityDTO> findDailyWordAdditions(Long userId) {
        return findDailyWordAdditionsNative(userId).stream()
                .map(result -> new DailyActivityDTO(
                        ((java.sql.Date) result[0]).toLocalDate(),
                        ((Number) result[1]).longValue()
                ))
                .collect(Collectors.toList());
    }
}