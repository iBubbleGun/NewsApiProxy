package edu.hillel.newsapiproxy.controller;

import edu.hillel.newsapiproxy.services.NewsApiProxyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/proxy")
public class NewsApiProxyController {

    private final NewsApiProxyService newsApiProxyService;

    public NewsApiProxyController(NewsApiProxyService newsApiProxyService) {
        this.newsApiProxyService = newsApiProxyService;
    }

    @GetMapping("/getTopNews")
    public List<String> getTopNews(@RequestParam String keyword, @RequestParam String lang) {
        return newsApiProxyService.getTopNews(keyword, lang).join();
    }

    @GetMapping("/getAllNews")
    public List<String> getAllNews(@RequestParam String keyword, @RequestParam String lang) {
        return newsApiProxyService.getAllNews(keyword, lang).join();
    }
}
