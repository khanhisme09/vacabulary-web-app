package com.vietdev.vocabularyapp.controller;

import com.vietdev.vocabularyapp.dto.ReviewResponse;
import com.vietdev.vocabularyapp.model.Word;
import com.vietdev.vocabularyapp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // API để bắt đầu một phiên ôn tập
    @GetMapping("/session")
    public ResponseEntity<List<Word>> getReviewSession(Principal principal) {
        List<Word> wordsToReview = reviewService.getWordsForReview(principal.getName())
                .stream()
                .map(userWord -> userWord.getWord()) // Chỉ trả về thông tin của từ
                .collect(Collectors.toList());
        return ResponseEntity.ok(wordsToReview);
    }

    // API để nhận phản hồi từ người dùng
    @PostMapping("/response")
    public ResponseEntity<?> processReviewResponse(@RequestBody ReviewResponse response, Principal principal) {
        try {
            reviewService.processReview(principal.getName(), response.getWordId(), response.getQuality());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}