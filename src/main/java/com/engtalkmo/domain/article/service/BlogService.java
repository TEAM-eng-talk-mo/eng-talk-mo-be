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

    public Long create(AddArticleRequest dto) {
        Article article = blogRepository.save(dto.toEntity());
        return article.getId();
    }

    @Transactional
    public Long update(long id, UpdateArticleRequest dto) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        article.update(dto.title(), dto.content());
        return article.getId();
    }
}
