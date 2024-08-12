package com.engtalkmo.domain.article.dto;

import com.engtalkmo.domain.article.domain.Article;

import java.time.LocalDateTime;

public record ArticleViewResponse(
        Long id,
        String title,
        String content,
        LocalDateTime createdDate) {

    public static ArticleViewResponse from(Article article) {
        return new ArticleViewResponse(
                article.getId(), article.getTitle(), article.getContent(), article.getCreatedDate());
    }
}
