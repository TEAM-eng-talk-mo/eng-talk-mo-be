package com.engtalkmo.domain.article.dto;

import com.engtalkmo.domain.article.entity.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ArticleViewResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdDate;

    public ArticleViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.createdDate = article.getCreatedDate();
    }

}
