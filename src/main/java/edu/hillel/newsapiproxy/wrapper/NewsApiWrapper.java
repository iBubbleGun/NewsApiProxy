package edu.hillel.newsapiproxy.wrapper;

import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NewsApiWrapper extends ArticleResponse {

    private static final String TITLE_ADDS = "Glory to Ukraine! ";
    private List<Article> articles = new ArrayList<>();

    public NewsApiWrapper(ArticleResponse response) {
        changeArticlesTitle(response);
    }

    @Override
    public List<Article> getArticles() {
        return articles;
    }

    private void changeArticlesTitle(@NotNull ArticleResponse response) {
        final List<Article> wrappedArticles = response.getArticles();
        if (!wrappedArticles.isEmpty()) {
            wrappedArticles.forEach(article -> {
                final String title = article.getTitle();
                article.setTitle(TITLE_ADDS.concat(title));
            });
            this.articles = wrappedArticles;
        }
    }
}
