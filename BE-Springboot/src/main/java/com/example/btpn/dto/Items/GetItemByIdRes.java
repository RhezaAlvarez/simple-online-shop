package com.example.btpn.dto.Items;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GetItemByIdRes {
    private Integer itemId;
    private String itemName;
    private String itemCode;
    private Integer stock;
    private Float price;
    private Integer isAvailable;
    private LocalDateTime lastReStock;
}