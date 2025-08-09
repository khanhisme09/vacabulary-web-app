package com.vietdev.vocabularyapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Sử dụng BCrypt để mã hóa mật khẩu, đây là tiêu chuẩn hiện nay
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CSRF cho các API (sẽ tìm hiểu kỹ hơn sau).
                // Đối với các ứng dụng web truyền thống dùng form, bạn nên bật nó.
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Cho phép tất cả mọi người truy cập các đường dẫn này
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll() // API đăng ký
                        .requestMatchers(HttpMethod.GET, "/api/words/search").permitAll() // API tra từ

                        // Tất cả các request còn lại đều cần phải được xác thực (đăng nhập)
                        .anyRequest().authenticated()
                )
                // Cấu hình form đăng nhập (sẽ được sử dụng ở Tuần 4)
                .formLogin(form -> form
                        .loginPage("/login") // Đường dẫn đến trang đăng nhập tùy chỉnh
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}