package com.example.btpn.models;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tidak perlu diisi, otomatis karena auto increment pada database
    @Column(name = "items_id")
    private Integer itemId;

    @Column(name = "items_name")
    private String itemName;

    @Column(name = "items_code")
    private String itemCode;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "price")
    private Float price;

    @Column(name = "is_available")
    private Integer isAvailable;

    @CreatedDate
    @Column(name = "last_re_stock")
    private LocalDateTime lastReStock;

    @OneToMany(mappedBy = "items")
    private List<Orders> orders;
}
