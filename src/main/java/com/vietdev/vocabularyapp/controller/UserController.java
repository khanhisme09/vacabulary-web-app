package com.vietdev.vocabularyapp.controller;

import com.vietdev.vocabularyapp.model.Word;
import com.vietdev.vocabularyapp.service.UserWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserWordService userWordService;

    @GetMapping("/my-words")
    public String myWordsPage(Model model, Principal principal) {
        // Lấy username của người dùng đang đăng nhập
        String username = principal.getName();
        // Gọi service để lấy danh sách từ
        List<Word> userWords = userWordService.findWordsByUsername(username);

        // Đưa danh sách vào model để Thymeleaf có thể truy cập
        model.addAttribute("words", userWords);

        // Trả về tên của file template
        return "my-words";
    }

    // Trong UserController
    @GetMapping("/review")
    public String reviewPage() {
        return "review"; // Trả về file review.html
    }
}