package com.vietdev.vocabularyapp.model; // Thay đổi package cho đúng

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "words") // Tên của bảng trong CSDL
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String word; // Từ vựng, ví dụ: "hello"

    private String phonetic; // Phiên âm, ví dụ: /həˈloʊ/

    @Column(columnDefinition = "TEXT")
    private String definition; // Định nghĩa

    @Column(columnDefinition = "TEXT")
    private String example; // Câu ví dụ
}