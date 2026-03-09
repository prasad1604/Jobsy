package com.Prasad.Jobsy2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Prasad.Jobsy2.dto.GigDTO;
import com.Prasad.Jobsy2.service.GigService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gigs")
public class GigController {

    private final GigService gigService;

    @PostMapping
    public ResponseEntity<?> createGig(@RequestBody GigDTO gigDTO) {

        try {
            GigDTO createdGig = gigService.createGig(gigDTO);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(createdGig);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<GigDTO>> getAllGigs() {

        List<GigDTO> gigs = gigService.getAllGigs();

        return ResponseEntity.ok(gigs);
    }

    @GetMapping("/{gigId}")
    public ResponseEntity<?> getGigById(@PathVariable Long gigId) {

        try {
            GigDTO gigDTO = gigService.getGigById(gigId);

            return ResponseEntity.ok(gigDTO);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/my/all")
    public ResponseEntity<List<GigDTO>> getAllMyGigs() {

        List<GigDTO> gigs = gigService.getMyGigs();

        return ResponseEntity.ok(gigs);
    }

    @GetMapping("/my")
    public ResponseEntity<List<GigDTO>> getMyGigs() {

        List<GigDTO> gigs = gigService.getMyActiveGigs();

        return ResponseEntity.ok(gigs);
    }

    @DeleteMapping("/{gigId}")
    public ResponseEntity<?> deleteGig(@PathVariable Long gigId) {

        try {
            boolean deleted = gigService.deleteGig(gigId);

            if (deleted) {
                return ResponseEntity.ok("Gig deleted successfully");
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Gig not found");
            }

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{gigId}")
    public ResponseEntity<?> updateGig(
            @PathVariable Long gigId,
            @RequestBody GigDTO gigDTO) {

        try {

            GigDTO updatedGig = gigService.updateGig(gigId, gigDTO);

            return ResponseEntity.ok(updatedGig);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/restore/{gigId}")
    public ResponseEntity<?> restoreGig(@PathVariable Long gigId) {

        try {

            GigDTO restoredGig = gigService.restoreGig(gigId);

            return ResponseEntity.ok(restoredGig);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

}
