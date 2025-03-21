package com.winbid.backend.repositories;

import com.winbid.backend.model.Bid;
import com.winbid.backend.model.Product;
import com.winbid.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByProductId(Long productId);
    Long countByProductId(Long productId);

    boolean existsByUserAndProduct(User user, Product product);

    long countByProduct(Product product);
}