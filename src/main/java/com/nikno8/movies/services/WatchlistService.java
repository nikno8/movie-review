package com.nikno8.movies.services;

import com.nikno8.movies.entities.Movie;
import com.nikno8.movies.entities.User;
import com.nikno8.movies.repositories.MovieRepository;
import com.nikno8.movies.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WatchlistService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;

    public boolean addMovieToWatchlist(String userId, String movieId) {
        Optional<User> userOpt = userRepository.findById(new ObjectId(userId));
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<String> watchList = user.getWatchList();
            if (watchList == null) {
                watchList = new ArrayList<>();
            }
            watchList.add(movieId);
            user.setWatchList(watchList);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public List<Movie> getWatchlist(String userId) {
        Optional<User> userOpt = userRepository.findById(new ObjectId(userId));
        if (userOpt.isPresent()) {
            return userOpt.get().getWatchList().stream()
                    .map(movieId -> movieRepository.findMovieByImdbId(movieId).orElse(null))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
