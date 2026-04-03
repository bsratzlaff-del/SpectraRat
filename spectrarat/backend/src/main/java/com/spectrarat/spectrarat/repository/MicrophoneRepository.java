package com.spectrarat.spectrarat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrarat.spectrarat.model.Microphone;

@Repository
public interface MicrophoneRepository extends JpaRepository<Microphone, Long> {
}