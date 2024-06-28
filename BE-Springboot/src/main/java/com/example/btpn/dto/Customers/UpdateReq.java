package com.example.btpn.dto.Customers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReq {
    private String customerName;
    private String customerAddress;
    private String customerPhone;
}
