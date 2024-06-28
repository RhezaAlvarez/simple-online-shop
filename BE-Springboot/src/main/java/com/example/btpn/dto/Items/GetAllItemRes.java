package com.example.btpn.dto.Items;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GetAllItemRes {
    private Integer totalRecord;
    private Integer itemsId;
    private String itemsName;
    private String itemsCode;
    private Integer stock;
    private Float price;
    private Integer isAvailable;
    private LocalDateTime lastReStock;
}
