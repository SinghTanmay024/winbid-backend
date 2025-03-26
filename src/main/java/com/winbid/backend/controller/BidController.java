package com.winbid.backend.controller;

import com.winbid.backend.model.User;
import com.winbid.backend.model.Bid;
import com.winbid.backend.model.BidRequest;
import com.winbid.backend.model.BidResponse;
import com.winbid.backend.repositories.BidRepository;
import com.winbid.backend.service.BidService;
import com.winbid.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    @Autowired
    private BidService bidService;

    @Autowired
    private UserService userService;

    @Autowired
    private BidRepository bidRepository;

    // ✅ Create a new bid (Place a bid)
    @PostMapping
    public ResponseEntity<BidResponse> placeBid(@RequestBody BidRequest bidRequest) {
        Bid savedBid = bidService.placeBid(bidRequest);
        return ResponseEntity.ok(new BidResponse(savedBid));
    }

    // ✅ Get all bids
    @GetMapping
    public ResponseEntity<List<Bid>> getAllBids() {
        List<Bid> bids = bidService.getAllBids();
        return ResponseEntity.ok(bids);
    }

    // ✅ Get a specific bid by ID
    @GetMapping("/{id}")
    public ResponseEntity<Bid> getBidById(@PathVariable Long id) {
        Optional<Bid> bid = bidService.getBidById(id);
        return bid.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Update an existing bid by ID
    @PutMapping("/{id}")
    public ResponseEntity<Bid> updateBid(@PathVariable Long id, @RequestBody Bid updatedBid) {
        Optional<Bid> bid = bidService.updateBid(id, updatedBid);
        return bid.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Delete a bid by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBid(@PathVariable Long id) {
        boolean deleted = bidService.deleteBid(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ✅ Delete all bids (Optional)
    @DeleteMapping
    public ResponseEntity<Void> deleteAllBids() {
        bidService.deleteAllBids();
        return ResponseEntity.noContent().build();
    }
}