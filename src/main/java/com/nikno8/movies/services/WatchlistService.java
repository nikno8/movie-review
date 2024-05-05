package com.nikno8.movies.services;

import com.nikno8.movies.entities.Movie;
import com.nikno8.movies.entities.User;
import com.nikno8.movies.repositories.MovieRepository;
import com.nikno8.movies.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        User user = userRepository.findById(new ObjectId(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<String> watchList = user.getWatchList();
        if (watchList == null) {
            watchList = new ArrayList<>();
        }
        if (!watchList.contains(movieId)) {
            watchList.add(movieId);
            user.setWatchList(watchList);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public List<Movie> getWatchlist(String userId) {
        return userRepository.findById(new ObjectId(userId))
                .map(user -> user.getWatchList().stream()
                        .map(movieId -> movieRepository.findMovieByImdbId(movieId))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public boolean removeMovieFromWatchlist(String userId, String movieId) {
        User user = userRepository.findById(new ObjectId(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<String> watchList = user.getWatchList();
        if (watchList != null && watchList.remove(movieId)) {
            userRepository.save(user);
            return true;
        }
        return false;
    }


}
