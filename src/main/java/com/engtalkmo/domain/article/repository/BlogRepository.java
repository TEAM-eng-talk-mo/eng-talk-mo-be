package com.engtalkmo.domain.article.repository;

import com.engtalkmo.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
