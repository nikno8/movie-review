
package com.nikno8.movies.controllers;

import com.nikno8.movies.entities.Review;
import com.nikno8.movies.services.ReviewService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Review> createReview(@RequestBody Map<String, String> payload){
        String reviewBody = payload.get("reviewBody");
        int rating = Integer.parseInt(payload.get("rating"));
        String imdbId = payload.get("imdbId");

        Review review = reviewService.createReview(reviewBody, rating, imdbId);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/movie/{imdbId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<Review>> getReviewsByImdbId(@PathVariable String imdbId) {
        List<Review> reviews = reviewService.getReviewsByImdbId(imdbId);
        if (reviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/average/{imdbId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Double> getAverageRating(@PathVariable String imdbId) {
        double averageRating = reviewService.calculateAverageRating(imdbId);
        return new ResponseEntity<>(averageRating, HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> deleteReview(@PathVariable String reviewId) {
        System.out.println("Received request to delete review with ID: " + reviewId);
        ObjectId objectId = new ObjectId(reviewId);  // Convert the string to ObjectId
        reviewService.deleteReview(objectId);
        return ResponseEntity.ok().build();
    }
}
