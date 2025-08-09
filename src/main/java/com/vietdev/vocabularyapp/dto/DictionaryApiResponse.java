package com.vietdev.vocabularyapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class DictionaryApiResponse {
    private String word;
    private String phonetic;
    private List<Phonetic> phonetics;
    private List<Meaning> meanings;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Phonetic {
        private String text;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Meaning {
        private String partOfSpeech;
        private List<Definition> definitions;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Definition {
        private String definition;
        private String example;
    }
}
