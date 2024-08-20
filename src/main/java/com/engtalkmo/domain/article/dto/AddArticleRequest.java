package com.engtalkmo.domain.article.dto;

import com.engtalkmo.domain.article.entity.Article;

public record AddArticleRequest(String title, String content) {

    public Article toEntity(String author) {
        return Article.builder()
                .author(author)
                .title(title)
                .content(content)
                .build();
    }
}
