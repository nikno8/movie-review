package com.nikno8.movies.controllers;

import com.nikno8.movies.entities.Movie;
import com.nikno8.movies.entities.User;
import com.nikno8.movies.services.UserService;
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
    @Autowired
    private UserService userService;

    @PostMapping("/{userId}/watchlist/{movieId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> addMovieToWatchlist(@PathVariable String userId, @PathVariable String movieId) {
        if (watchlistService.addMovieToWatchlist(userId, movieId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{userId}/watchlist/{movieId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> removeMovieFromWatchlist(@PathVariable String userId, @PathVariable String movieId) {
        if (watchlistService.removeMovieFromWatchlist(userId, movieId)) {
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

    @DeleteMapping("/ban/{login}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String login) {
        if (userService.deleteUser(login)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/ban/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }
}
