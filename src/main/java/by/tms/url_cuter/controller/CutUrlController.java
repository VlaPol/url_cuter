package by.tms.url_cuter.controller;

import by.tms.url_cuter.entity.TranslateRecord;
import by.tms.url_cuter.service.CutUrlService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/cuturl/v1/")
public class CutUrlController {

    CutUrlService cutUrlService;

    private static final Path path = Paths.get("http://localhost:8080/cuturl/v1/");

    public CutUrlController(CutUrlService cutUrlService) {
        this.cutUrlService = cutUrlService;
    }

    @PostMapping("/shorturl")
    String getShortUrl(@RequestBody String url){

        TranslateRecord translateRecord = cutUrlService.getShortNameFromUrl(url);
        System.out.println(path.toString() + translateRecord.getShortName());
        return path.toString() + translateRecord.getShortName();
    }

    @PostMapping("/realurl")
    String getRealUrlByShort(@RequestBody String shortUrl){

        TranslateRecord translateRecord = cutUrlService.getUrlFromShortName(shortUrl);
        System.out.println(translateRecord.getUrl());
        return translateRecord.getUrl();
    }
}
