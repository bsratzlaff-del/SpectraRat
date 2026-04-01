package com.spectrarat.spectrarat.controller;

import com.spectrarat.spectrarat.model.PurchaseRecord;
import com.spectrarat.spectrarat.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseRepository purchaseRepository;

    // This handles individual items (if you still use that)
    @PostMapping
    public PurchaseRecord createPurchase(@RequestBody PurchaseRecord purchase) {
        return purchaseRepository.save(purchase);
    }

    // THIS FIXES THE 404: It adds the /batch door
    @PostMapping("/batch")
    public List<PurchaseRecord> createPurchases(@RequestBody List<PurchaseRecord> purchases) {
        return purchaseRepository.saveAll(purchases);
    }

    @GetMapping("/business/{id}")
    public List<PurchaseRecord> getHistoryByBusiness(@PathVariable Long id) {
        return purchaseRepository.findByBusinessId(id);
    }
}