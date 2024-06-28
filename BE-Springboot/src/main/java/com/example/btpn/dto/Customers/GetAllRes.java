package com.example.btpn.dto.Customers;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GetAllRes {
    private Integer totalRecord;
    private Integer customerId;
    private String customerName;
    private String customerAddress;
    private String customerCode;
    private Integer isActive;
    private LocalDateTime lastOrderDate;
}
