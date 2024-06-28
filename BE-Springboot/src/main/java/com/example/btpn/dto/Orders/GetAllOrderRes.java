package com.example.btpn.dto.Orders;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GetAllOrderRes {
    private Integer totalRecord;
    private Integer orderId;
    private String orderCode;
    private LocalDateTime orderDate;
    private Float totalPrice;
    private String customerName;
    private String itemName;
    private Integer quantity;
}