
package com.nikno8.movies.controllers;

import com.nikno8.movies.Review;
import com.nikno8.movies.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Map<String, String> payload){
        String reviewBody = payload.get("reviewBody");
        int rating = Integer.parseInt(payload.get("rating"));  // Ensure that rating is passed as a string and convert it to an integer
        String imdbId = payload.get("imdbId");

        Review review = reviewService.createReview(reviewBody, rating, imdbId);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/movie/{imdbId}")
    public ResponseEntity<List<Review>> getReviewsByImdbId(@PathVariable String imdbId) {
        List<Review> reviews = reviewService.getReviewsByImdbId(imdbId);
        if (reviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/average/{imdbId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable String imdbId) {
        double averageRating = reviewService.calculateAverageRating(imdbId);
        return new ResponseEntity<>(averageRating, HttpStatus.OK);
    }

}
