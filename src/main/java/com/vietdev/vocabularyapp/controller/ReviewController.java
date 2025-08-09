package com.vietdev.vocabularyapp.controller;

import com.vietdev.vocabularyapp.model.Word;
import com.vietdev.vocabularyapp.service.UserWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private UserWordService userWordService;

    @GetMapping("/next-word")
    public ResponseEntity<?> getNextWord(Principal principal) {
        Optional<Word> wordOptional = userWordService.getRandomWordForUser(principal.getName());
        // Trả về từ nếu tìm thấy, ngược lại trả về 404 Not Found
        return wordOptional.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}