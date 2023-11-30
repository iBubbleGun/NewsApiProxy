package edu.hillel.newsapiproxy.wrapper;

import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import org.jetbrains.annotations.Unmodifiable;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NewsApiWrapperTest {

    private ArticleResponse articleResponse;
    private NewsApiWrapper newsApiWrapper;

    @Before
    public void setUp() {
        this.articleResponse = new ArticleResponse();
        setInitialArticles();
        this.newsApiWrapper = new NewsApiWrapper(articleResponse);
    }

    @Test
    public void testChangeArticlesTitle() throws NoSuchFieldException, IllegalAccessException {
        String expected_title1 = getTitleAdds().concat("Title-1");
        String actual_title1 = newsApiWrapper.getArticles().get(0).getTitle();
        assertEquals(expected_title1, actual_title1);

        String expected_title2 = getTitleAdds().concat("Title-2");
        String actual_title2 = newsApiWrapper.getArticles().get(1).getTitle();
        assertEquals(expected_title2, actual_title2);

        String expected_title3 = getTitleAdds().concat("Title-3");
        String actual_title3 = newsApiWrapper.getArticles().get(2).getTitle();
        assertEquals(expected_title3, actual_title3);
    }

    private void setInitialArticles() {
        articleResponse.setArticles(getTestArticlesList());
    }

    private @Unmodifiable List<Article> getTestArticlesList() {
        final Article a1 = new Article();
        final Article a2 = new Article();
        final Article a3 = new Article();
        a1.setTitle("Title-1");
        a2.setTitle("Title-2");
        a3.setTitle("Title-3");
        return List.of(a1, a2, a3);
    }

    private String getTitleAdds() throws NoSuchFieldException, IllegalAccessException {
        final Field field = NewsApiWrapper.class.getDeclaredField("TITLE_ADDS");
        field.setAccessible(true);
        return String.valueOf(field.get(newsApiWrapper));
    }
}
