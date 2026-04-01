package com.spectrarat.spectrarat.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "purchase_records")
@Data // Generates getters/setters automatically
public class PurchaseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long businessId;
    private String receiverModel;
    private String capsuleType;
    private Integer quantity;
    private String assignedBand;
    private Double totalPrice;
    private LocalDate purchaseDate;
}