package com.vietdev.vocabularyapp.controller;

import com.vietdev.vocabularyapp.service.UserWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user/words")
public class UserWordController {

    @Autowired private UserWordService userWordService;

    @PostMapping("/{wordId}")
    public ResponseEntity<?> saveWord(@PathVariable Long wordId, Principal principal) {
        // Principal chứa thông tin về người dùng đã được xác thực (ở đây là username)
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String username = principal.getName();
        userWordService.saveWordForUser(username, wordId);

        return ResponseEntity.ok("Word saved successfully for user " + username);
    }
    // Trong class UserWordController
    @DeleteMapping("/{wordId}")
    public ResponseEntity<?> deleteWord(@PathVariable Long wordId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        try {
            userWordService.deleteWordForUser(principal.getName(), wordId);
            return ResponseEntity.ok("Word removed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}