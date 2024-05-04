package com.nikno8.movies.controllers;

import com.nikno8.movies.entities.Movie;
import com.nikno8.movies.services.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private WatchlistService watchlistService;

    @PostMapping("/{userId}/watchlist/{movieId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> addMovieToWatchlist(@PathVariable String userId, @PathVariable String movieId) {
        if (watchlistService.addMovieToWatchlist(userId, movieId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{userId}/watchlist")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<Movie>> getWatchlist(@PathVariable String userId) {
        List<Movie> watchlist = watchlistService.getWatchlist(userId);
        return ResponseEntity.ok(watchlist);
    }
}
