package com.winbid.backend.model;


import jakarta.validation.constraints.*;
import jdk.jfr.ContentType;
import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    // Change from String to MultipartFile
    private MultipartFile imageFile;


    @PositiveOrZero(message = "Total bids must be zero or positive")
    private Integer totalBids = 0;

    @Positive(message = "Bid price must be positive")
    private Double bidPrice;

    @NotNull(message = "Admin information is required")
    private Long userId;
}

