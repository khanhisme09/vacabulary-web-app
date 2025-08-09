package com.vietdev.vocabularyapp.controller; // Thay đổi package cho đúng

import com.vietdev.vocabularyapp.model.Word;
import com.vietdev.vocabularyapp.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/words") // Tiền tố chung cho tất cả các endpoint trong controller này
public class WordController {

    private final WordService wordService;

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchWord(@RequestParam("q") String query) {
        Optional<Word> wordOptional = wordService.searchWord(query);

        if (wordOptional.isPresent()) {
            // Nếu tìm thấy, trả về dữ liệu của từ và status 200 OK
            return ResponseEntity.ok(wordOptional.get());
        } else {
            // Nếu không tìm thấy, trả về status 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }
}