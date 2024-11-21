package com.prgrms.ijuju.point.service;

import com.prgrms.ijuju.point.dto.request.PointRequestDTO;
import com.prgrms.ijuju.point.dto.response.PointDetailsDTO;
import com.prgrms.ijuju.point.dto.response.PointResponseDTO;
import com.prgrms.ijuju.point.entity.PointDetails;
import com.prgrms.ijuju.point.entity.PointStatus;
import com.prgrms.ijuju.point.entity.PointType;
import com.prgrms.ijuju.point.repository.PointDetailsRepository;
import com.prgrms.ijuju.point.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointService {
    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointDetailsRepository pointDetailsRepository;

    @Transactional
    public PointResponseDTO updatePoints(PointRequestDTO request) {
        // 포인트 조회
        Point point = pointRepository.findByMemberId(request.getMemberId())
                .orElse(new Point(request.getMemberId(), 0L, LocalDateTime.now()));

        // 포인트 업데이트
        if (request.getPointType() == PointType.MINI_GAME) {
            point.setCurrentPoints(point.getCurrentPoints() + request.getPointAmount());
        } else if (request.getPointType() == PointType.STOCK) {
            point.setCurrentPoints(point.getCurrentPoints() - request.getPointAmount());
        } else if (request.getPointType() == PointType.EXCHANGE) {
            point.setCurrentCoins(point.getCurrentCoins() + (request.getPointAmount() / 100));
        }

        // 포인트 저장
        pointRepository.save(point);

        // 포인트 상세 저장
        PointDetails pointDetails = new PointDetails();
        pointDetails.setPoint(point);
        pointDetails.setMemberId(request.getMemberId());
        pointDetails.setPointType(request.getPointType());
        pointDetails.setPointAmount(request.getPointAmount());
        pointDetails.setPointStatus(request.getPointAmount() > 0 ? PointStatus.EARNED : PointStatus.USED);
        pointDetails.setCreatedAt(LocalDateTime.now());
        pointDetailsRepository.save(pointDetails);

        // 포인트 상세 조회
        List<PointDetailsDTO> history = pointDetailsRepository.findByMemberIdAndPointType(
                        request.getMemberId(), request.getPointType())
                .stream()
                .map(detail -> new PointDetailsDTO(detail))
                .collect(Collectors.toList());

        return new PointResponseDTO(point.getCurrentPoints(), point.getCurrentCoins(), history);
    }
}
