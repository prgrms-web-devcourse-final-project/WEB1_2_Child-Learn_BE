package com.prgrms.ijuju.domain.example.repository;

import com.prgrms.ijuju.domain.example.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<Sample, String> {
}