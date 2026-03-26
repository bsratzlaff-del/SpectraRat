package com.spectrarat.spectrarat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrarat.spectrarat.model.FrequencyBand;

@Repository
public interface FrequencyBandRepository extends JpaRepository<FrequencyBand, Long> {
}