package com.engtalkmo.domain.article.dto;

import com.engtalkmo.domain.article.domain.Article;

public record ArticleResponse(Long articleId, String title, String content) {

    public static ArticleResponse from(Article article) {
        return new ArticleResponse(article.getId(), article.getTitle(), article.getContent());
    }
}
