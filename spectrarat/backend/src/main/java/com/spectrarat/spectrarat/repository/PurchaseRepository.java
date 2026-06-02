package com.spectrarat.spectrarat.repository;

import com.spectrarat.spectrarat.model.PurchaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseRecord, Long> {
    // This allows you to find all purchases made by a specific business
    List<PurchaseRecord> findByBusinessId(Long businessId);
    
}