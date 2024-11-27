package com.prgrms.ijuju.domain.minigame.flipcard.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ijuju.domain.minigame.flipcard.dto.request.FlipCardDto;
import com.prgrms.ijuju.domain.minigame.flipcard.entity.FlipCard;
import com.prgrms.ijuju.domain.minigame.flipcard.repository.FlipCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FlipCardDataInitializer implements CommandLineRunner {

    private final FlipCardRepository flipCardRepository;

    @Override
    public void run(String... args) throws Exception {
        if (flipCardRepository.count() > 0) {
            log.info("FlipCard 데이터가 이미 존재합니다.");
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            ClassPathResource classPathResource = new ClassPathResource("minigame-data/flip-card.json");
            File file = classPathResource.getFile();

            List<FlipCardDto> flipCardDtos = objectMapper.readValue(file, new TypeReference<List<FlipCardDto>>() {
            });
            List<FlipCard> flipCards = flipCardDtos.stream()
                    .map(FlipCardDto::toEntity)
                    .toList();
            flipCardRepository.saveAll(flipCards);
            log.info("FlipCard 데이터 초기화 완료");
        } catch (IOException e) {
            log.error("FlipCard 데이터 초기화 실패: {}", e.getMessage());
        }
    }
}
