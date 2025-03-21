package com.winbid.backend.service;

import com.winbid.backend.model.Bid;
import com.winbid.backend.model.Product;
import com.winbid.backend.model.User;
import com.winbid.backend.model.Winner;
import com.winbid.backend.repositories.BidRepository;
import com.winbid.backend.repositories.ProductRepository;
import com.winbid.backend.repositories.WinnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private WinnerRepository winnerRepository;

    @Autowired
    private ProductRepository productRepository;

    // ✅ Place a bid (Create)
    public Bid placeBid(Bid bid) {
        System.out.println("Inside the function");

        // Load the product from the database using its ID
        Product product = productRepository.findById(bid.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        System.out.println(product);

        // Increment total bids for the product
        product.setTotalBids(product.getTotalBids() + 1);

        // Save the bid
        bidRepository.save(bid);

        // Check if the total number of bids for the product matches totalBids
        Long bidCount = bidRepository.countByProductId(product.getId());
        if (bidCount.equals(Long.valueOf(product.getTotalBids()))) {
            assignWinner(product);
        }

        return bid;
    }

    private void assignWinner(Product product) {
        // Get all the bids for the product
        List<Bid> bids = bidRepository.findByProductId(product.getId());

        // If there are no bids, do nothing
        if (bids.isEmpty()) {
            return;
        }

        // Randomly pick a winner
        int randomIndex = new Random().nextInt(bids.size());
        Bid winningBid = bids.get(randomIndex);

        // Get the user who placed the winning bid
        User winner = winningBid.getUser();

        // Save the winner
        Winner winnerEntity = new Winner();
        winnerEntity.setProduct(product);
        winnerEntity.setUser(winner);
        winnerEntity.setWinDate(LocalDateTime.now());
        winnerEntity.setWinningBidAmount(winningBid.getBidAmount());

        winnerRepository.save(winnerEntity);
    }

    // ✅ Get all bids
    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    // ✅ Get bid by ID
    public Optional<Bid> getBidById(Long id) {
        return bidRepository.findById(id);
    }

    // ✅ Update bid by ID
    public Optional<Bid> updateBid(Long id, Bid updatedBid) {
        return bidRepository.findById(id).map(existingBid -> {
            existingBid.setBidAmount(updatedBid.getBidAmount());
            return bidRepository.save(existingBid);
        });
    }

    // ✅ Delete bid by ID
    public boolean deleteBid(Long id) {
        if (bidRepository.existsById(id)) {
            bidRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ✅ Delete all bids
    public void deleteAllBids() {
        bidRepository.deleteAll();
    }
}
