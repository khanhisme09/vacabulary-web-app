package com.vietdev.vocabularyapp.service;

import com.vietdev.vocabularyapp.model.User;
import com.vietdev.vocabularyapp.model.UserWord;
import com.vietdev.vocabularyapp.model.Word;
import com.vietdev.vocabularyapp.repository.UserRepository;
import com.vietdev.vocabularyapp.repository.UserWordRepository;
import com.vietdev.vocabularyapp.repository.WordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserWordService {
    private final UserWordRepository userWordRepository;
    private final UserRepository userRepository;
    private final WordRepository wordRepository;

    public UserWordService(UserWordRepository userWordRepository, UserRepository userRepository, WordRepository wordRepository) {
        this.userWordRepository = userWordRepository;
        this.userRepository = userRepository;
        this.wordRepository = wordRepository;
    }

    public void saveWordForUser(String username, Long wordId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new RuntimeException("Word not found"));

        UserWord userWord = new UserWord();
        userWord.setUser(user);
        userWord.setWord(word);

        userWordRepository.save(userWord);
    }
    // PHƯƠNG THỨC MỚI
    public List<Word> findWordsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Từ danh sách UserWord, chúng ta chỉ lấy ra đối tượng Word
        return user.getUserWords().stream()
                .map(UserWord::getWord)
                .collect(Collectors.toList());
    }

    // Trong class UserWordService
    @Transactional // Đảm bảo thao tác được thực hiện hoàn chỉnh
    public void deleteWordForUser(String username, Long wordId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tìm bản ghi UserWord cụ thể cần xóa
        UserWord userWordToRemove = user.getUserWords().stream()
                .filter(uw -> uw.getWord().getId().equals(wordId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User has not saved this word"));

        // Xóa khỏi danh sách của user (Hibernate sẽ quản lý việc xóa khỏi DB)
        user.getUserWords().remove(userWordToRemove);
        userWordRepository.delete(userWordToRemove);
    }

    public Optional<Word> getRandomWordForUser(String username) {
        List<Word> userWords = findWordsByUsername(username);
        if (userWords.isEmpty()) {
            return Optional.empty();
        }
        Random rand = new Random();
        return Optional.of(userWords.get(rand.nextInt(userWords.size())));
    }
}