package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizdata.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizdata.entity.OXQuizData;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizdata.repository.OXQuizDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OXQuizDataInitializer implements CommandLineRunner {

    private static final String DATA_FILE_PATH = "minigame-data/ox-quiz.json";

    private final OXQuizDataRepository oxQuizDataRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        if (oxQuizDataRepository.count() > 0) {
            log.info("OX Quiz 데이터가 이미 존재합니다.");
            return;
        }

        List<OXQuizData> quizzes = loadQuizData();
        oxQuizDataRepository.saveAll(quizzes);
        log.info("OX Quiz 데이터 초기화 완료");
    }

    private List<OXQuizData> loadQuizData() throws IOException {
        ClassPathResource resource = new ClassPathResource(DATA_FILE_PATH);
        return objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {});
    }
}