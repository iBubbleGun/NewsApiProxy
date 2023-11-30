package edu.hillel.newsapiproxy.services;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NewsApiProxyServiceTest {

    private final NewsApiClient newsApiClient = Mockito.mock(NewsApiClient.class);

    @InjectMocks
    private NewsApiProxyService newsApiProxyService = new NewsApiProxyService(newsApiClient);

    @Test
    void testGetTopNewsSuccess() {
        final ArticleResponse articleResponse = new ArticleResponse();
        articleResponse.setArticles(getTestArticlesList());

        Mockito.doAnswer((Answer<Void>) invocation -> {
            final NewsApiClient.ArticlesResponseCallback callback = invocation.getArgument(1);
            callback.onSuccess(articleResponse);
            return null;
        }).when(newsApiClient).getTopHeadlines(Mockito.any(TopHeadlinesRequest.class), Mockito.any());

        final CompletableFuture<List<String>> result = newsApiProxyService.getTopNews("test_keyword", "test_lang");

        assertEquals(getExpectedNewsList(articleResponse), result.join());
    }

    @Test
    void testGetTopNewsFailure() {
        final RuntimeException testException = new RuntimeException("Test Error");

        Mockito.doAnswer((Answer<Void>) invocation -> {
            final NewsApiClient.ArticlesResponseCallback callback = invocation.getArgument(1);
            callback.onFailure(testException);
            return null;
        }).when(newsApiClient).getTopHeadlines(Mockito.any(TopHeadlinesRequest.class), Mockito.any());

        final CompletableFuture<List<String>> result = newsApiProxyService.getTopNews("test_keyword", "test_lang");

        assertEquals(Collections.singletonList("Test Error"), result.join());
    }

    @Test
    void testGetAllNewsSuccess() {
        final ArticleResponse articleResponse = new ArticleResponse();
        articleResponse.setArticles(getTestArticlesList());

        Mockito.doAnswer((Answer<Void>) invocation -> {
            final NewsApiClient.ArticlesResponseCallback callback = invocation.getArgument(1);
            callback.onSuccess(articleResponse);
            return null;
        }).when(newsApiClient).getEverything(Mockito.any(EverythingRequest.class), Mockito.any());

        final CompletableFuture<List<String>> result = newsApiProxyService.getAllNews("test_keyword", "test_lang");

        assertEquals(getExpectedNewsList(articleResponse), result.join());
    }

    @Test
    void testGetAllNewsFailure() {
        final RuntimeException testException = new RuntimeException("Test Error");

        Mockito.doAnswer((Answer<Void>) invocation -> {
            final NewsApiClient.ArticlesResponseCallback callback = invocation.getArgument(1);
            callback.onFailure(testException);
            return null;
        }).when(newsApiClient).getEverything(Mockito.any(EverythingRequest.class), Mockito.any());

        final CompletableFuture<List<String>> result = newsApiProxyService.getAllNews("test_keyword", "test_lang");

        assertEquals(Collections.singletonList("Test Error"), result.join());
    }

    private @Unmodifiable @NotNull List<Article> getTestArticlesList() {
        final List<Article> articles = new ArrayList<>();
        IntStream.rangeClosed(1, 5).forEach(i -> {
            final Article article = new Article();
            article.setTitle("Title #" + i);
            article.setContent("Test content #" + i);
            articles.add(article);
        });
        return articles;
    }

    private @Unmodifiable @NotNull List<String> getExpectedNewsList(@NotNull ArticleResponse response) {
        return response.getArticles().stream()
                .map(article -> "[TITLE]: " + article.getTitle() + ".\n" +
                        "[TEXT]: " + article.getContent() + ".")
                .toList();
    }
}
