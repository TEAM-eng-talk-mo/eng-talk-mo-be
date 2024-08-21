package com.engtalkmo.domain.article.service;

import com.engtalkmo.domain.article.dto.AddArticleRequest;
import com.engtalkmo.domain.article.dto.ArticleListViewResponse;
import com.engtalkmo.domain.article.dto.UpdateArticleRequest;
import com.engtalkmo.domain.article.entity.Article;
import com.engtalkmo.domain.article.exception.ArticleNotFoundException;
import com.engtalkmo.domain.article.repository.BlogRepository;
import com.engtalkmo.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    public Long create(AddArticleRequest dto, String username) {
        Article article = dto.toEntity(username);
        return blogRepository.save(article).getId();
    }

    public List<ArticleListViewResponse> findAll() {
        return blogRepository.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
    }

    public Article findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @Transactional
    public Long update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(id));
        authorizeArticleAuthor(article);
        article.update(request.title(), request.content());
        return article.getId();
    }

    public void delete(long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(id));
        authorizeArticleAuthor(article);
        blogRepository.delete(article);
    }

    private void authorizeArticleAuthor(Article article) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (isAuthorizeArticleAuthor(article, username)) {
            throw new IllegalArgumentException("not authorized");
        }
    }

    private static boolean isAuthorizeArticleAuthor(Article article, String username) {
        return !article.getAuthor().equals(username);
    }
}
