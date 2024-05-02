

package com.nikno8.movies.services;

import com.nikno8.movies.entities.Movie;
import com.nikno8.movies.entities.Review;
import com.nikno8.movies.repositories.ReviewRepository;
import org.bson.types.ObjectId;
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

    public void deleteReview(ObjectId reviewId) {
        reviewRepository.deleteById(reviewId);
        mongoTemplate.updateMulti(
                new Query(),
                new Update().pull("reviewIds", reviewId),
                Movie.class
        );
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

