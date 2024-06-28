package com.example.btpn.dto.Customers;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GetByIdRes {
    private Integer customerId;
    private String customerName;
    private String customerAddress;
    private String customerCode;
    private String customerPhone;
    private Integer isActive;
    private LocalDateTime lastOrderDate;
    private String pic;
}
