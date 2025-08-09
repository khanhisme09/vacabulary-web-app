package com.vietdev.vocabularyapp.service; // Thay đổi package cho đúng

import com.vietdev.vocabularyapp.model.Word;
import com.vietdev.vocabularyapp.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vietdev.vocabularyapp.dto.DictionaryApiResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class WordService {

    private final WordRepository wordRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public WordService(WordRepository wordRepository, RestTemplate restTemplate) {
        this.wordRepository = wordRepository;
        this.restTemplate = restTemplate;
    }

    public Optional<Word> searchWord(String term) {
        term = term.toLowerCase().trim();
        // 1. Tìm trong CSDL trước
        Optional<Word> wordFromDb = wordRepository.findByWord(term);
        if (wordFromDb.isPresent()) {
            return wordFromDb;
        }

        // 2. Nếu không có, gọi API bên ngoài
        try {
            String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + term;
            DictionaryApiResponse[] apiResponse = restTemplate.getForObject(apiUrl, DictionaryApiResponse[].class);

            if (apiResponse.length > 0) {
                // 3. Phân tích và lưu vào CSDL
                Word newWord = parseAndSaveApiResponse(apiResponse[0]);
                return Optional.of(newWord);
            }
        } catch (HttpClientErrorException.NotFound e) {
            // API không tìm thấy từ
            return Optional.empty();
        } catch (Exception e) {
            // Các lỗi khác (mất mạng, API sập...)
            System.err.println("API call error: " + e.getMessage());
            return Optional.empty();
        }

        return Optional.empty();
    }

    private Word parseAndSaveApiResponse(DictionaryApiResponse response) {
        Word word = new Word();
        word.setWord(response.getWord());

        // Lấy phiên âm (ưu tiên phonetic, sau đó đến phonetics text)
        if (response.getPhonetic() != null && !response.getPhonetic().isEmpty()) {
            word.setPhonetic(response.getPhonetic());
        } else if (response.getPhonetics() != null && !response.getPhonetics().isEmpty()) {
            response.getPhonetics().stream()
                    .filter(p -> p.getText() != null && !p.getText().isEmpty())
                    .findFirst()
                    .ifPresent(p -> word.setPhonetic(p.getText()));
        }

        // Lấy định nghĩa và ví dụ đầu tiên tìm thấy
        if (response.getMeanings() != null && !response.getMeanings().isEmpty()) {
            response.getMeanings().stream()
                    .flatMap(m -> m.getDefinitions().stream())
                    .findFirst()
                    .ifPresent(def -> {
                        word.setDefinition(def.getDefinition());
                        word.setExample(def.getExample());
                    });
        }

        return wordRepository.save(word);
    }
}