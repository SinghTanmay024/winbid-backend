package com.winbid.backend.service;

import com.winbid.backend.model.*;
import com.winbid.backend.repositories.BidRepository;
import com.winbid.backend.repositories.ProductRepository;
import com.winbid.backend.repositories.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    // ✅ Place a bid (Create)
    public Bid placeBid(BidRequest bidRequest) {
        User user = userRepository.findById(bidRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(bidRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Bid bid = new Bid();
        bid.setUser(user);
        bid.setProduct(product);
        bid.setBidAmount(bidRequest.getBidAmount());

        Bid savedBid = bidRepository.save(bid);

        List<Bid> bids = bidRepository.findByProductId(product.getId());
        if(bids.size() == product.getTotalBids()){
            assignWinner(product,bids);
        }
        return savedBid;
    }


    private void assignWinner(Product product,List<Bid> bids) {
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
