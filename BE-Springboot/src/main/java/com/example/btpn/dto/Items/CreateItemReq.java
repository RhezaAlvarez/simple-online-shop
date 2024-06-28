package com.example.btpn.dto.Items;

import lombok.Data;

@Data
public class CreateItemReq {
    private String itemName;
    private Integer stock;
    private Float price;
}