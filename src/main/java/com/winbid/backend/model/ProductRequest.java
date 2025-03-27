package com.winbid.backend.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    private String imageUrl;

    @PositiveOrZero(message = "Total bids must be zero or positive")
    private Integer totalBids = 0;

    @Positive(message = "Bid price must be positive")
    private Double bidPrice;

    @NotNull(message = "Admin information is required")
    private AdminRequest admin;

    @Data
    public static class AdminRequest {
        @NotNull(message = "Admin ID is required")
        private Long id; // Use Long for the admin ID
    }
}

