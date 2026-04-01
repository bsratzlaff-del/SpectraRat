package com.spectrarat.spectrarat.controller;

import com.spectrarat.spectrarat.model.PurchaseRecord;
import com.spectrarat.spectrarat.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@CrossOrigin(origins = "http://localhost:4200") // <--- CRITICAL: Allows Angular to talk to Java
public class PurchaseController {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @PostMapping
    public PurchaseRecord createPurchase(@RequestBody PurchaseRecord purchase) {
        // This saves the "Cart" data directly to your PostgreSQL/MySQL database
        return purchaseRepository.save(purchase);
    }

    @GetMapping("/business/{id}")
    public List<PurchaseRecord> getHistoryByBusiness(@PathVariable Long id) {
        return purchaseRepository.findByBusinessId(id);
    }
}