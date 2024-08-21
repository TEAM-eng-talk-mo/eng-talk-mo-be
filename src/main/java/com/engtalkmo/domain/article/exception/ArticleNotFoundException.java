package com.engtalkmo.domain.article.exception;

import com.engtalkmo.global.error.exception.EntityNotFoundException;

public class ArticleNotFoundException extends EntityNotFoundException {

    public ArticleNotFoundException(Long id) {
        super(id + " is not found");
    }
}
