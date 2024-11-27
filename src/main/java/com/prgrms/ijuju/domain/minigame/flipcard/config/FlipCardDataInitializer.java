package com.prgrms.ijuju.domain.minigame.flipcard.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ijuju.domain.minigame.flipcard.dto.request.FlipCardDto;
import com.prgrms.ijuju.domain.minigame.flipcard.entity.FlipCard;
import com.prgrms.ijuju.domain.minigame.flipcard.exception.FlipCardInitializationException;
import com.prgrms.ijuju.domain.minigame.flipcard.repository.FlipCardRepository;
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
public class FlipCardDataInitializer implements CommandLineRunner {
    private static final String DATA_FILE_PATH = "minigame-data/flip-card.json";

    private final FlipCardRepository flipCardRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void run(String... args) {
        try {
            initializeFlipCardData();
        } catch (Exception e) {
            throw new FlipCardInitializationException("FlipCard 데이터 초기화 중 오류 발생", e);
        }
    }

    private void initializeFlipCardData() throws IOException {
        if (isDataAlreadyExists()) {
            log.info("FlipCard 데이터가 이미 존재합니다.");
            return;
        }

        List<FlipCardDto> flipCardDtos = loadFlipCardData();
        saveFlipCardData(flipCardDtos);
        log.info("FlipCard 데이터 초기화 완료");
    }

    private boolean isDataAlreadyExists() {
        return flipCardRepository.count() > 0;
    }

    private List<FlipCardDto> loadFlipCardData() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(DATA_FILE_PATH);
        return objectMapper.readValue(
                classPathResource.getInputStream(),
                new TypeReference<List<FlipCardDto>>() {}
        );
    }

    private void saveFlipCardData(List<FlipCardDto> flipCardDtos) {
        List<FlipCard> flipCards = flipCardDtos.stream()
                .map(FlipCardDto::toEntity)
                .toList();
        flipCardRepository.saveAll(flipCards);
    }
}
