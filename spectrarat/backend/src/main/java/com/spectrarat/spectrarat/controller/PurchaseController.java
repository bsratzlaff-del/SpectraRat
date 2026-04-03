package com.spectrarat.spectrarat.controller;

import com.spectrarat.spectrarat.model.Customer;
import com.spectrarat.spectrarat.model.PurchaseRecord;
import com.spectrarat.spectrarat.repository.CustomerRepository;
import com.spectrarat.spectrarat.repository.PurchaseRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseRepository purchaseRepository;
    private final CustomerRepository customerRepository;
    public PurchaseController(PurchaseRepository purchaseRepository, CustomerRepository customerRepository) {
        this.purchaseRepository = purchaseRepository;
        this.customerRepository = customerRepository;
    }

    // This handles individual items (if you still use that)
    @PostMapping
    public PurchaseRecord createPurchase(@RequestBody PurchaseRecord purchase) {
        return purchaseRepository.save(purchase);
    }

    @PostMapping("/batch")
    public List<PurchaseRecord> createPurchases(@RequestBody List<PurchaseRecord> purchases) {
        return purchaseRepository.saveAll(purchases);
    }

    @GetMapping("/business/{id}")
    public List<PurchaseRecord> getHistoryByBusiness(@PathVariable Long id) {
        return purchaseRepository.findByBusinessId(id);
    }

    @GetMapping("/user/{username}")
    public List<PurchaseRecord> getPurchasesByUsername(@PathVariable String username) {
        Optional<Customer> customerOpt = customerRepository.findByUsername(username);

        if (customerOpt.isPresent()) {
            Long businessId = customerOpt.get().getId();
            return purchaseRepository.findByBusinessId(businessId);
        }

        return Collections.emptyList();
    }
}