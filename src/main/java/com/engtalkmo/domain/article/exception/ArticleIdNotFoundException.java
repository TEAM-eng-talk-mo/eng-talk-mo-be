package com.engtalkmo.domain.article.exception;

import com.engtalkmo.global.error.exception.EntityNotFoundException;

public class ArticleIdNotFoundException extends EntityNotFoundException {

    public ArticleIdNotFoundException(Long id) {
        super(id + " is not found");
    }
}
