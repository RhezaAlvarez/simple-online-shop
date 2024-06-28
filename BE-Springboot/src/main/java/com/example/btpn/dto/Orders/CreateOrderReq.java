package com.example.btpn.dto.Orders;

import lombok.Data;

@Data
public class CreateOrderReq {
    private String customerName;
    private String itemName;
    private Integer quantity;
}