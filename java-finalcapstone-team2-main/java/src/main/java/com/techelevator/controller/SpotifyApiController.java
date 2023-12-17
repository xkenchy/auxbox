package com.techelevator.controller;

import com.techelevator.domain.spotify.SpotifyTokenResponse;
import com.techelevator.services.SpotifyApiService;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
public class SpotifyApiController {

    private SpotifyApiService spotifyApiService;

    public SpotifyApiController(SpotifyApiService spotifyApiService) {
        this.spotifyApiService = spotifyApiService;
    }

    @GetMapping("/receive-token")
    @ResponseBody
    public void processAccessCode(@RequestParam String code, HttpServletResponse httpServletResponse) {
        SpotifyTokenResponse response = spotifyApiService.getToken(code);
        if (response != null) {
            httpServletResponse.setHeader("Location", "http://localhost:8080/landing");
            httpServletResponse.setStatus(302);
        }
    }

}
