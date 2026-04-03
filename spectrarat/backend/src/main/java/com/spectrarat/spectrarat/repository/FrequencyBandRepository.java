package com.spectrarat.spectrarat.repository;

import com.spectrarat.spectrarat.model.FrequencyBand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrequencyBandRepository extends JpaRepository<FrequencyBand, Long> {
}
