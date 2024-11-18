package com.prgrms.ijuju.example.repository;

import com.prgrms.ijuju.example.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<Sample, String> {
}