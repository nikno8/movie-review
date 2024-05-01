

package com.nikno8.movies.services;

import com.nikno8.movies.Movie;
import com.nikno8.movies.Review;
import com.nikno8.movies.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

//    public Review createReview(String reviewBody, int rating, String imdbId) {
//        Review review = new Review(reviewBody, rating, imdbId); // Ensure constructor or setter methods are correct
//        return reviewRepository.save(review);
//    }
    public Review createReview(String reviewBody, int rating, String imdbId) {
        // Create and save the review
        Review review = new Review(reviewBody, rating, imdbId);
        review = reviewRepository.save(review);

        // Now, update the corresponding movie document to include this new review's ID
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("imdbId").is(imdbId)),
                new Update().push("reviewIds", review),
                Movie.class
        );

        return review;
    }

    public List<Review> getReviewsByImdbId(String imdbId) {
        return reviewRepository.findByImdbId(imdbId);
    }


    public double calculateAverageRating(String imdbId) {
        List<Review> reviews = reviewRepository.findByImdbId(imdbId);
        if (reviews.isEmpty()) {
            return 0;
        }
        double average = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        return average;
    }

}

