package com.engtalkmo.domain.article.service;

import com.engtalkmo.domain.article.domain.Article;
import com.engtalkmo.domain.article.dto.AddArticleRequest;
import com.engtalkmo.domain.article.dto.UpdateArticleRequest;
import com.engtalkmo.domain.article.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    public Long create(AddArticleRequest request) {
        Article article = blogRepository.save(request.toEntity());
        return article.getId();
    }

    @Transactional
    public Long update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        article.update(request.title(), request.content());
        return article.getId();
    }
}
