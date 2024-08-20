package com.engtalkmo.domain.article.api;

import com.engtalkmo.domain.article.dto.AddArticleRequest;
import com.engtalkmo.domain.article.dto.ArticleResponse;
import com.engtalkmo.domain.article.dto.UpdateArticleRequest;
import com.engtalkmo.domain.article.repository.BlogRepository;
import com.engtalkmo.domain.article.service.BlogService;
import com.engtalkmo.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Slf4j
public class BlogApiController {

    private final BlogService blogService;
    private final BlogRepository blogRepository;

    @PostMapping
    public ResponseEntity<Long> createArticle(
            @AuthenticationPrincipal Member member,
            @RequestBody AddArticleRequest request) {
        log.info("Member={}", member);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(blogService.create(request, member.getUsername()));
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
        blogRepository.deleteById(id);
        return ResponseEntity.ok()
                .build();
    }
}
