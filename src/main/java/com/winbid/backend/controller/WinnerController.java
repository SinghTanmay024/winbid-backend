package com.winbid.backend.controller;

import com.winbid.backend.model.Winner;
import com.winbid.backend.service.WinnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/winners")
public class WinnerController {

    @Autowired
    private WinnerService winnerService;

    // ✅ Get all winners
    @GetMapping
    public ResponseEntity<List<Winner>> getAllWinners() {
        List<Winner> winners = winnerService.getAllWinners();
        return ResponseEntity.ok(winners);
    }

    // ✅ Get a winner by ID
    @GetMapping("/{id}")
    public ResponseEntity<Winner> getWinnerById(@PathVariable Long id) {
        Optional<Winner> winner = winnerService.getWinnerById(id);
        return winner.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Determine and store the winner for a product
    @PostMapping("/determine/{productId}")
    public ResponseEntity<Winner> determineWinner(@PathVariable Long productId) {
        Optional<Winner> winner = winnerService.determineWinner(productId);
        return winner.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    // ✅ Delete a winner by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWinner(@PathVariable Long id) {
        boolean deleted = winnerService.deleteWinner(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ✅ Delete all winners
    @DeleteMapping
    public ResponseEntity<Void> deleteAllWinners() {
        winnerService.deleteAllWinners();
        return ResponseEntity.noContent().build();
    }
}

