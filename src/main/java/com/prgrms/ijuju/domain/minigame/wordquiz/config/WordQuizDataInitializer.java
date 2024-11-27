package com.prgrms.ijuju.domain.minigame.wordquiz.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ijuju.domain.minigame.wordquiz.dto.request.WordQuizDbDto;
import com.prgrms.ijuju.domain.minigame.wordquiz.entity.WordQuiz;
import com.prgrms.ijuju.domain.minigame.wordquiz.exception.WordQuizInitializationException;
import com.prgrms.ijuju.domain.minigame.wordquiz.repository.WordQuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WordQuizDataInitializer implements CommandLineRunner {
    private static final String DATA_FILE_PATH = "minigame-data/korean-words-game.json";

    private final WordQuizRepository wordQuizRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void run(String... args) {
        try {
            initializeWordQuizData();
        } catch (Exception e) {
            throw new WordQuizInitializationException("WordQuiz 데이터 초기화 중 오류 발생", e);
        }
    }

    private void initializeWordQuizData() throws IOException {
        if (isDataAlreadyExists()) {
            log.info("WordQuiz 데이터가 이미 존재합니다.");
            return;
        }

        List<WordQuizDbDto> wordQuizDbDtos = loadWordQuizData();
        saveWordQuizData(wordQuizDbDtos);
        log.info("WordQuiz 데이터 초기화 완료");
    }

    private boolean isDataAlreadyExists() {
        return wordQuizRepository.count() > 0;
    }

    private List<WordQuizDbDto> loadWordQuizData() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(DATA_FILE_PATH);
        return objectMapper.readValue(
                classPathResource.getInputStream(),
                new TypeReference<List<WordQuizDbDto>>() {}
        );
    }

    private void saveWordQuizData(List<WordQuizDbDto> wordQuizDbDtos) {
        List<WordQuiz> wordQuizs = wordQuizDbDtos.stream()
                .map(WordQuizDbDto::toEntity)
                .toList();
        wordQuizRepository.saveAll(wordQuizs);
    }
}
