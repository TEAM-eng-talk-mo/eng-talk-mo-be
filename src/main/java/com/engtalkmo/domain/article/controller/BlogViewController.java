package com.engtalkmo.domain.article.controller;

import com.engtalkmo.domain.article.domain.Article;
import com.engtalkmo.domain.article.dto.ArticleListViewResponse;
import com.engtalkmo.domain.article.dto.ArticleViewResponse;
import com.engtalkmo.domain.article.exception.ArticleNotFoundException;
import com.engtalkmo.domain.article.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class BlogViewController {

    private final BlogRepository blogRepository;

    @GetMapping
    public String getArticles(Model model) {
        model.addAttribute("articles",
                blogRepository.findAll().stream()
                        .map(ArticleListViewResponse::new)
                        .toList());
        return "articleList";
    }

    @GetMapping("/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("id"));
        model.addAttribute("article", ArticleViewResponse.from(article));
        return "article";
    }
}
