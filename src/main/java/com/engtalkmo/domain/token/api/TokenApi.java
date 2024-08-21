package com.engtalkmo.domain.token.api;

import com.engtalkmo.domain.token.dto.CreateAccessTokenRequest;
import com.engtalkmo.domain.token.dto.CreateAccessTokenResponse;
import com.engtalkmo.domain.token.service.RefreshTokenService;
import com.engtalkmo.domain.token.service.TokenCreateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TokenApi {

    private final TokenCreateService tokenCreateService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/token")
    public ResponseEntity<CreateAccessTokenResponse> createAccessToken(
            @RequestBody @Valid CreateAccessTokenRequest request) {
        String accessToken = tokenCreateService.createAccessToken(request.refreshToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(accessToken));
    }

    @DeleteMapping("/refresh-token")
    public ResponseEntity<Void> deleteRefreshToken() {
        refreshTokenService.delete();
        return ResponseEntity.ok()
                .build();
    }
}
