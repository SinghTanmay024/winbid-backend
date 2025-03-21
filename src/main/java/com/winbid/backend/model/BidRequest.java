package com.winbid.backend.model;

import lombok.Data;

@Data
public class BidRequest {
    private Long userId;
    private Long productId;
    private Double bidAmount;
}
