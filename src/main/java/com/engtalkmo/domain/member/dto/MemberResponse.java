package com.engtalkmo.domain.member.dto;

import com.engtalkmo.domain.article.domain.Article;
import com.engtalkmo.domain.article.dto.ArticleResponse;

public record MemberResponse(

) {
    public static MemberResponse from(Article article) {
        return null;
    }
}
