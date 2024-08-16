package com.engtalkmo.domain.member.dto;

import com.engtalkmo.domain.article.entity.Article;

public record MemberResponse(

) {
    public static MemberResponse from(Article article) {
        return null;
    }
}
