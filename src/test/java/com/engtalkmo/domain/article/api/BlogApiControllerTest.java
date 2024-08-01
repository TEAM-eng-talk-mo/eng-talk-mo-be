package com.engtalkmo.domain.article.api;

import com.engtalkmo.domain.article.domain.Article;
import com.engtalkmo.domain.article.dto.AddArticleRequest;
import com.engtalkmo.domain.article.dto.UpdateArticleRequest;
import com.engtalkmo.domain.article.repository.BlogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired private WebApplicationContext context;
    @Autowired private BlogRepository blogRepository;

    @BeforeEach
    void beforeEach() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();
    }

    @DisplayName("findArticles: 블로그 글 생성")
    @Test
    void createArticle() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest request = new AddArticleRequest(title, content);

        // when & then
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();
        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles).extracting(Article::getTitle).containsExactly(title);
        assertThat(articles).extracting(Article::getContent).containsExactly(content);
    }

    @DisplayName("findArticles: 블로그 글 목록 조회 성공")
    @Test
    void findArticles() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when
        ResultActions result = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(title))
                .andExpect(jsonPath("$[0].content").value(content));
    }

    @DisplayName("findArticle: 블로그 글 조회 성공")
    @Test
    void findArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article saveArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when
        ResultActions result = mockMvc.perform(get(url, saveArticle.getId())
                .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content));
    }

    @DisplayName("deleteArticle: 블로그 글 수정 성공")
    @Test
    void updateArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        final String title = "spring";
        final String content = "spring id good";

        Article saveArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        final String newTitle = "Spring Boot";
        final String newContent = "Spring Boot is good!!";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        // when & then
        mockMvc.perform(put(url, saveArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Article article = blogRepository.findById(saveArticle.getId()).get();
        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }

    @DisplayName("deleteArticle: 블로그 글 삭제 성공")
    @Test
    void deleteArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article saveArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when & then
        mockMvc.perform(delete(url, saveArticle.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty();
    }
}