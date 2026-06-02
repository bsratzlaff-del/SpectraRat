package com.spectrarat.spectrarat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrarat.spectrarat.model.Receiver;

@Repository
public interface ReceiverRepository extends JpaRepository<Receiver, Long> {
}