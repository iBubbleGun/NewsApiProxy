package edu.hillel.newsapiproxy.services;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import edu.hillel.newsapiproxy.wrapper.NewsApiWrapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class NewsApiProxyService {

    private final NewsApiClient newsApiClient;

    public NewsApiProxyService(NewsApiClient newsApiClient) {
        this.newsApiClient = newsApiClient;
    }

    public CompletableFuture<List<String>> getTopNews(String keyword, String language) {
        final CompletableFuture<List<String>> topNews = new CompletableFuture<>();
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .q(keyword)
                        .language(language)
                        .build(),
                getCallback(topNews)
        );
        return topNews;
    }

    public CompletableFuture<List<String>> getAllNews(String keyword, String language) {
        final CompletableFuture<List<String>> allNews = new CompletableFuture<>();
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q(keyword)
                        .language(language)
                        .build(),
                getCallback(allNews)
        );
        return allNews;
    }

    @Contract(value = "_ -> new", pure = true)
    private NewsApiClient.@NotNull ArticlesResponseCallback getCallback(CompletableFuture<List<String>> future) {
        return new NewsApiClient.ArticlesResponseCallback() {
            @Override
            public void onSuccess(ArticleResponse response) {
                final NewsApiWrapper wrappedResponse = new NewsApiWrapper(response);
                if (!wrappedResponse.getArticles().isEmpty()) {
                    final List<String> targetNewsList = wrappedResponse.getArticles().stream()
                            .map(article -> "[TITLE]: " + article.getTitle() + ".\n" +
                                    "[TEXT]: " + article.getContent() + ".")
                            .collect(Collectors.toList());
                    future.complete(targetNewsList);
                } else {
                    future.complete(List.of("Not a single news was found."));
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                future.complete(Collections.singletonList(throwable.getMessage()));
            }
        };
    }
}
