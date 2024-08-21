package com.engtalkmo.domain.article.api;

import com.engtalkmo.domain.article.dto.AddArticleRequest;
import com.engtalkmo.domain.article.dto.ArticleResponse;
import com.engtalkmo.domain.article.dto.UpdateArticleRequest;
import com.engtalkmo.domain.article.repository.BlogRepository;
import com.engtalkmo.domain.article.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Slf4j
public class BlogApi {

    private final BlogService blogService;
    private final BlogRepository blogRepository;

    @PostMapping
    public ResponseEntity<Long> createArticle(
            @AuthenticationPrincipal User user,
            @RequestBody AddArticleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(blogService.create(request, user.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> findArticles() {
        return ResponseEntity.ok()
                .body(blogRepository.findAll()
                        .stream()
                        .map(ArticleResponse::from)
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(ArticleResponse.from(blogRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("not found: " + id))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateArticle(
            @PathVariable Long id,
            @RequestBody UpdateArticleRequest request) {
        return ResponseEntity.ok()
                .body(blogService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        blogService.delete(id);
        return ResponseEntity.ok()
                .build();
    }
}
