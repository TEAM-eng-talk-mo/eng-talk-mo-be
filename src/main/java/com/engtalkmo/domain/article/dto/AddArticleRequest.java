package com.engtalkmo.domain.article.dto;

import com.engtalkmo.domain.article.domain.Article;

public record AddArticleRequest(String title, String content) {

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}
