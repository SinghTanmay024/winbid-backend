package com.winbid.backend.model;

import lombok.Data;

@Data
public class BidResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private Double bidAmount;

    public BidResponse(Bid bid) {
        this.id = bid.getId();
        this.userId = bid.getUser().getId();
        this.productId = bid.getProduct().getId();
        this.bidAmount = bid.getBidAmount();
    }

    // Getters and Setters
}
