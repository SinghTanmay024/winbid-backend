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
public class WinnerService {

    @Autowired
    private WinnerRepository winnerRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ProductRepository productRepository;

    private final Random random = new Random();

    // ✅ Get all winners
    public List<Winner> getAllWinners() {
        return winnerRepository.findAll();
    }

    // ✅ Get a winner by ID
    public Optional<Winner> getWinnerById(Long id) {
        return winnerRepository.findById(id);
    }

    // ✅ Delete a winner by ID
    public boolean deleteWinner(Long id) {
        if (winnerRepository.existsById(id)) {
            winnerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ✅ Delete all winners
    public void deleteAllWinners() {
        winnerRepository.deleteAll();
    }

    // ✅ Business logic: Select a random winner when the bid count reaches expected count
    public Optional<Winner> determineWinner(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            return Optional.empty(); // Product not found
        }

        Product product = productOpt.get();

        // Get all bids for the product
        List<Bid> bids = bidRepository.findByProductId(productId);

        // Ensure total bids match the expected count
        if (bids.size() != product.getTotalBids()) {
            return Optional.empty(); // Not enough bids to decide a winner
        }

        // Randomly pick a winner
        Bid winningBid = bids.get(random.nextInt(bids.size()));
        User winningUser = winningBid.getUser();

        // Create a new Winner entry
        Winner winner = new Winner();
        winner.setProduct(product);
        winner.setUser(winningUser);
        winner.setWinDate(LocalDateTime.now());
        winner.setWinningBidAmount(winningBid.getBidAmount());

        // Save the winner record
        Winner savedWinner = winnerRepository.save(winner);
        return Optional.of(savedWinner);
    }
}
