package com.engtalkmo.domain.article.service;

import com.engtalkmo.domain.article.domain.Article;
import com.engtalkmo.domain.article.dto.AddArticleRequest;
import com.engtalkmo.domain.article.dto.ArticleListViewResponse;
import com.engtalkmo.domain.article.dto.UpdateArticleRequest;
import com.engtalkmo.domain.article.exception.ArticleIdNotFoundException;
import com.engtalkmo.domain.article.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<ArticleListViewResponse> findAll() {
        return blogRepository.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
    }

    public Article findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new ArticleIdNotFoundException(id));
    }
}
