package com.vietdev.vocabularyapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    // Phương thức mới cho trang đăng nhập
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Phương thức mới cho trang đăng ký
    @GetMapping("/register")
    public String register() {
        return "register";
    }
}