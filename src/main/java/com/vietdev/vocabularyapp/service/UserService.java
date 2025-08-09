package com.vietdev.vocabularyapp.service;

import com.vietdev.vocabularyapp.dto.RegistrationRequest;
import com.vietdev.vocabularyapp.model.User;
import com.vietdev.vocabularyapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(RegistrationRequest registrationRequest) {
        // Kiểm tra xem username hoặc email đã tồn tại chưa (có thể thêm sau)
        User newUser = new User();
        newUser.setUsername(registrationRequest.getUsername());
        // Rất quan trọng: Mã hóa mật khẩu trước khi lưu
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        newUser.setEmail(registrationRequest.getEmail());
        return userRepository.save(newUser);
    }
}