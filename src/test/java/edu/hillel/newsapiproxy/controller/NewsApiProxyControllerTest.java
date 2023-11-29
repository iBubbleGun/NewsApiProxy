package edu.hillel.newsapiproxy.controller;

import edu.hillel.newsapiproxy.services.NewsApiProxyService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NewsApiProxyController.class)
public class NewsApiProxyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private NewsApiProxyService newsApiProxyService;

    @Test
    public void testGetTopNews() throws Exception {
        final CompletableFuture<List<String>> future = new CompletableFuture<>();
        future.complete(getTestNews().join().subList(0, 2));

        Mockito.when(newsApiProxyService.getTopNews("test_keyword", "test_lang"))
                .thenReturn(future);
        mvc.perform(get("/proxy/getTopNews?keyword=test_keyword&lang=test_lang"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetAllNews() throws Exception {
        Mockito.when(newsApiProxyService.getAllNews("test_keyword", "test_lang"))
                .thenReturn(getTestNews());
        mvc.perform(get("/proxy/getAllNews?keyword=test_keyword&lang=test_lang"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    private @Unmodifiable @NotNull CompletableFuture<List<String>> getTestNews() {
        String news1 = "Test 1.";
        String news2 = "Test 2";
        String news3 = "Test 3";
        String news4 = "Test 4";
        String news5 = "Test 5";
        final List<String> testNewsList = List.of(news1, news2, news3, news4, news5);
        final CompletableFuture<List<String>> future = new CompletableFuture<>();
        future.complete(testNewsList);
        return future;
    }
}
