package com.engtalkmo.domain.token.api;

import com.engtalkmo.domain.token.dto.CreateAccessTokenRequest;
import com.engtalkmo.domain.token.dto.CreateAccessTokenResponse;
import com.engtalkmo.domain.token.service.TokenCreateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenApi {

    private final TokenCreateService tokenCreateService;

    @PostMapping
    public ResponseEntity<CreateAccessTokenResponse> createAccessToken(
            @RequestBody @Valid CreateAccessTokenRequest request) {
        String accessToken = tokenCreateService.createAccessToken(request.refreshToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(accessToken));
    }
}
