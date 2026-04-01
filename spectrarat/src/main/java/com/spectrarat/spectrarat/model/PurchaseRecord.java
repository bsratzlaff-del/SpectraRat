package com.spectrarat.spectrarat.model;

import jakarta.persistence.*;
import java.time.LocalDate;     
import lombok.Data;            

@Entity
@Data
public class PurchaseRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long businessId;
    private String modelName;
    private String bandName;
    private Double price;
    private LocalDate purchaseDate;
}