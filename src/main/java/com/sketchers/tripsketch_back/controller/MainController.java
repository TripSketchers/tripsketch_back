package com.sketchers.tripsketch_back.controller;

import com.sketchers.tripsketch_back.security.PrincipalUser;
import com.sketchers.tripsketch_back.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @GetMapping("/api/main/trip-destinations")
    public ResponseEntity<?> getTripDestinations(@AuthenticationPrincipal PrincipalUser principalUser, @RequestParam(required = false) String searchKeyword) {
        return ResponseEntity.ok(mainService.getTripDestinations(searchKeyword));
    }

    @GetMapping("/api/main/upcoming-trip")
    public ResponseEntity<?> getUpcomingTrip(@AuthenticationPrincipal PrincipalUser principalUser) {
        int userId = principalUser.getUser().getUserId();
        return ResponseEntity.ok(mainService.getUpcomingTrip(userId));
    }

    @GetMapping("/api/main/recent-albums")
    public ResponseEntity<?> getRecentAlbums(@AuthenticationPrincipal PrincipalUser principalUser) {
        int userId = principalUser.getUser().getUserId();
        return ResponseEntity.ok(mainService.getRecentAlbums(userId));
    }

    @GetMapping("/api/main/popular-trips")
    public ResponseEntity<?> getPopularTrips() {
        return ResponseEntity.ok(mainService.getPopularTrips());
    }
}
