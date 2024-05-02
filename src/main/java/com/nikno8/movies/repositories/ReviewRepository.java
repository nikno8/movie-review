package com.nikno8.movies.repositories;

import com.nikno8.movies.entities.Review;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, ObjectId> {
    List<Review> findByImdbId(String imdbId);  // Method to find reviews by IMDb ID
}
