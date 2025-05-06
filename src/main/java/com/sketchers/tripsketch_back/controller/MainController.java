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

    @GetMapping("/api/trip-destinations")
    public ResponseEntity<?> getTripDestinations(@AuthenticationPrincipal PrincipalUser principalUser, @RequestParam(required = false) String searchKeyword){
        return  ResponseEntity.ok(mainService.getTripDestinations(searchKeyword));
    }
}
